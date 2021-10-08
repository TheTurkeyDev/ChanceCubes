package chanceCubes.commands;

import chanceCubes.CCubesCore;
import chanceCubes.client.ClientHelper;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.registry.player.PlayerRewardInfo;
import chanceCubes.rewards.DefaultGiantRewards;
import chanceCubes.rewards.DefaultRewards;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.NonreplaceableBlockOverride;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CCubesServerCommands
{
	public CCubesServerCommands(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		// @formatter:off
		dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("chancecubes")
				.then(
						Commands.literal("reload").executes(this::executeReload)
				)
				.then(
						Commands.literal("version").executes(this::executeVersion)
				)
				.then(
						Commands.literal("handNBT").executes(this::executeHandNBT)
				)
				.then(
						Commands.literal("handID").executes(this::executeHandID)
				)
				.then(
						Commands.literal("disableReward").then(
								Commands.argument("rewardName", new RewardArgument())
										.executes(ctx -> executeDisableReward(ctx, RewardArgument.getReward(ctx, "rewardName")))
						)
				)
				.then(
						Commands.literal("enableReward").then(
								Commands.argument("rewardName", new RewardArgument())
										.executes(ctx -> executeEnableReward(ctx, RewardArgument.getReward(ctx, "rewardName")))
						)
				)
				.then(
						Commands.literal("schematic").requires(cs -> cs.hasPermission(2)).requires(cs -> cs.getLevel().isClientSide()).then(
								Commands.literal("create").executes(this::executeSchematicCreate)
						).then(
								Commands.literal("cancel").executes(this::executeSchematicCancel)
						)
				)
				.then(
						Commands.literal("rewardsInfo").then(
								Commands.argument("action", new RewardInfoActionArgument())
										.executes(ctx -> executeRewardInfo(ctx, RewardInfoActionArgument.func_212592_a(ctx, "action")))
						)
				)
				.then(
						Commands.literal("test").executes(this::executeTest)
				)
				.then(
						Commands.literal("testRewards").executes(this::executeTestRewards)
				)
				.then(
						Commands.literal("testCustomRewards").executes(this::executeTestCustomRewards)
				)
				.then(
						Commands.literal("spawnGiantCube").then(
								Commands.argument("pos", BlockPosArgument.blockPos())
										.executes(ctx -> executeSpawnGiantCube(ctx, BlockPosArgument.getBlockPos(ctx, "pos")))
						)
				)
				.then(
						Commands.literal("spawnReward").then(
								Commands.argument("rewardName", new RewardArgument()).then(
										Commands.argument("target", EntityArgument.players())
												.executes(ctx -> executeSpawnReward(ctx, RewardArgument.getReward(ctx, "rewardName"), EntityArgument.getPlayers(ctx, "target")))
								)
						)
				)
		);
		// @formatter:on
	}

	public ServerPlayer getPlayer(CommandSourceStack source)
	{
		try
		{
			return source.getPlayerOrException();
		} catch(CommandSyntaxException e)
		{
			CCubesCore.logger.error("You should never see this. If you do you broke everything. Report to Turkey");
		}
		//Should never get here.
		return null;
	}


	public int executeReload(CommandContext<CommandSourceStack> ctx)
	{
		new Thread(() ->
		{
			GlobalCCRewardRegistry.DEFAULT.ClearRewards();
			GlobalCCRewardRegistry.GIANT.ClearRewards();
			ConfigLoader.reload();
			DefaultRewards.loadDefaultRewards();
			DefaultGiantRewards.loadDefaultRewards();
			CustomRewardsLoader.instance.loadCustomRewards();
			GlobalCCRewardRegistry.loadCustomUserRewards(ServerLifecycleHooks.getCurrentServer());
			NonreplaceableBlockOverride.loadOverrides();
			RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "Rewards Reloaded");
		}).start();
		return 0;
	}

	public int executeVersion(CommandContext<CommandSourceStack> ctx)
	{
		String ver = ModList.get().getModContainerById(CCubesCore.MODID).get().getModInfo().getVersion().toString();
		RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "Chance Cubes Version " + ver);
		return 0;
	}

	public int executeHandNBT(CommandContext<CommandSourceStack> ctx)
	{
		Player player = getPlayer(ctx.getSource());
		CompoundTag nbt = player.getInventory().getSelected().getOrCreateTag();
		RewardsUtil.sendMessageToPlayer(player, nbt.toString());
		return 0;
	}

	public int executeHandID(CommandContext<CommandSourceStack> ctx)
	{
		Player player = getPlayer(ctx.getSource());
		ItemStack stack = player.getInventory().getSelected();
		if(!stack.isEmpty())
		{
			ResourceLocation res = stack.getItem().getRegistryName();
			RewardsUtil.sendMessageToPlayer(player, res.getNamespace() + ":" + res.getPath());
			RewardsUtil.sendMessageToPlayer(player, "meta: " + stack.getDamageValue());
		}
		return 0;
	}

	public int executeDisableReward(CommandContext<CommandSourceStack> ctx, String reward)
	{
		Player player = getPlayer(ctx.getSource());
		if(GlobalCCRewardRegistry.DEFAULT.disableReward(reward))
			RewardsUtil.sendMessageToPlayer(player, reward + " Has been disabled.");
		else if(GlobalCCRewardRegistry.GIANT.disableReward(reward))
			RewardsUtil.sendMessageToPlayer(player, reward + " Has been disabled.");
		else
			RewardsUtil.sendMessageToPlayer(player, reward + " is either not currently enabled or is not a valid reward name.");
		return 0;
	}

	public int executeEnableReward(CommandContext<CommandSourceStack> ctx, String reward)
	{
		Player player = getPlayer(ctx.getSource());
		if(GlobalCCRewardRegistry.DEFAULT.enableReward(reward))
			RewardsUtil.sendMessageToPlayer(player, reward + " Has been enabled.");
		else if(GlobalCCRewardRegistry.GIANT.enableReward(reward))
			RewardsUtil.sendMessageToPlayer(player, reward + " Has been enabled.");
		else
			RewardsUtil.sendMessageToPlayer(player, reward + " is either not currently disabled or is not a valid reward name.");
		return 0;
	}

	public int executeSchematicCreate(CommandContext<CommandSourceStack> ctx)
	{
		if(RenderEvent.isCreatingSchematic())
		{
			//Possibly make own packet
			if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
			{
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
				{
					ClientHelper.openSchematicCreatorGUI(getPlayer(ctx.getSource()));
				});
			}
			else
			{
				RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "Please set both points before moving on!");
			}
		}
		else
		{
			RenderEvent.setCreatingSchematic(true);
		}
		return 0;
	}

	public int executeSchematicCancel(CommandContext<CommandSourceStack> ctx)
	{
		RenderEvent.setCreatingSchematic(false);
		SchematicUtil.selectionPoints[0] = null;
		SchematicUtil.selectionPoints[1] = null;
		return 0;
	}

	public int executeRewardInfo(CommandContext<CommandSourceStack> ctx, InfoAction action)
	{
		Player player = getPlayer(ctx.getSource());
		List<PlayerRewardInfo> defaultrewards = GlobalCCRewardRegistry.DEFAULT.getPlayerRewardRegistry(player.getStringUUID()).getPlayersRewards();
		List<PlayerRewardInfo> giantrewards = GlobalCCRewardRegistry.GIANT.getPlayerRewardRegistry(player.getStringUUID()).getPlayersRewards();
		int defaultEnabled = defaultrewards.size();
		int giantEnabled = giantrewards.size();

		List<String> playerRewards = new ArrayList<>();
		switch(action)
		{
			case DEFAULT:
				RewardsUtil.sendMessageToPlayer(player, "===DEFAULT REWARDS===");
				for(PlayerRewardInfo reward : defaultrewards)
					RewardsUtil.sendMessageToPlayer(player, reward.reward.getName());
				break;
			case GIANT:
				RewardsUtil.sendMessageToPlayer(player, "===GIANT REWARDS===");
				for(PlayerRewardInfo reward : giantrewards)
					RewardsUtil.sendMessageToPlayer(player, reward.reward.getName());
				break;
			case DEFAULT_ALL:
				RewardsUtil.sendMessageToPlayer(player, "===DEFAULT REWARDS===");
				for(String reward : GlobalCCRewardRegistry.DEFAULT.getRewardNames())
					RewardsUtil.sendMessageToPlayer(player, reward);
				break;
			case GIANT_ALL:
				RewardsUtil.sendMessageToPlayer(player, "===GIANT REWARDS===");
				for(String reward : GlobalCCRewardRegistry.GIANT.getRewardNames())
					RewardsUtil.sendMessageToPlayer(player, reward);
				break;
			case DEFAULT_DISABLED:
				RewardsUtil.sendMessageToPlayer(player, "===DEFAULT REWARDS DISABLED===");
				for(PlayerRewardInfo reward : defaultrewards)
					playerRewards.add(reward.reward.getName());
				for(String reward : GlobalCCRewardRegistry.DEFAULT.getRewardNames())
					if(!playerRewards.contains(reward))
						RewardsUtil.sendMessageToPlayer(player, reward);
				break;
			case GIANT_DISABLED:
				RewardsUtil.sendMessageToPlayer(player, "===GIANT REWARDS DISABLED===");
				for(PlayerRewardInfo reward : giantrewards)
					playerRewards.add(reward.reward.getName());
				for(String reward : GlobalCCRewardRegistry.GIANT.getRewardNames())
					if(!playerRewards.contains(reward))
						RewardsUtil.sendMessageToPlayer(player, reward);
				break;
		}

		RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "There are currently " + GlobalCCRewardRegistry.DEFAULT.getNumberOfLoadedRewards() + " regular rewards loaded and you have " + defaultEnabled + " rewards enabled");
		RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "There are currently " + GlobalCCRewardRegistry.GIANT.getNumberOfLoadedRewards() + " giant rewards loaded and you have " + giantEnabled + " rewards enabled");
		return 0;
	}

	public int executeTestRewards(CommandContext<CommandSourceStack> ctx)
	{
		CCubesSettings.testRewards = !CCubesSettings.testRewards;
		CCubesSettings.testingRewardIndex = 0;
		if(CCubesSettings.testRewards)
			RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "Reward testing is now enabled for all rewards!");
		else
			RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "Reward testing is now disabled and normal randomness is back.");
		return 0;
	}

	public int executeTestCustomRewards(CommandContext<CommandSourceStack> ctx)
	{
		CCubesSettings.testCustomRewards = !CCubesSettings.testCustomRewards;
		CCubesSettings.testingRewardIndex = 0;
		if(CCubesSettings.testCustomRewards)
			RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "Reward testing is now enabled for custom rewards!");
		else
			RewardsUtil.sendMessageToPlayer(getPlayer(ctx.getSource()), "Reward testing is now disabled and normal randomness is back.");
		return 0;
	}

	public int executeTest(CommandContext<CommandSourceStack> ctx)
	{
		return 0;
	}

	public int executeSpawnGiantCube(CommandContext<CommandSourceStack> ctx, BlockPos pos)
	{
		Level level = ctx.getSource().getLevel();

		if(RewardsUtil.isBlockUnbreakable(level, pos.offset(0, 0, 0)) && CCubesSettings.nonReplaceableBlocks.contains(level.getBlockState(pos.offset(0, 0, 0))))
			return 0;

		GiantCubeUtil.setupStructure(pos.offset(-1, -1, -1), level, true);

		level.playSound(null, pos, CCubesSounds.GIANT_CUBE_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
		return 0;
	}

	public int executeSpawnReward(CommandContext<CommandSourceStack> ctx, String rewardName, Collection<ServerPlayer> targets)
	{
		if(ctx.getSource().getEntity() != null)
		{
			CCubesCore.logger.error("Sorry, player's and entities cannot run this command!");
			return 0;
		}

		IChanceCubeReward reward = GlobalCCRewardRegistry.DEFAULT.getRewardByName(rewardName);
		if(reward == null)
			reward = GlobalCCRewardRegistry.GIANT.getRewardByName(rewardName);
		if(reward == null)
		{
			CCubesCore.logger.error(rewardName + " is not a valid reward in the spawnReward command!");
			return 0;
		}

		CCubesCore.logger.info("spawnReward command is spawning " + rewardName);

		for(ServerPlayer target : targets)
			reward.trigger(ctx.getSource().getLevel(), target.getOnPos(), target, new JsonObject());
		return 0;
	}
}