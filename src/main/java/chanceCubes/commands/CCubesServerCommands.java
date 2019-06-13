package chanceCubes.commands;

import org.apache.logging.log4j.Level;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import chanceCubes.CCubesCore;
import chanceCubes.client.gui.SchematicCreationGui;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.CustomProfileLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.NonreplaceableBlockOverride;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CCubesServerCommands
{
	public CCubesServerCommands(CommandDispatcher<CommandSource> dispatcher)
	{
		// @formatter:off
		dispatcher.register(LiteralArgumentBuilder.<CommandSource> literal("chancecubes").requires(cs -> {
			return cs.getEntity() instanceof EntityPlayerMP;
		}).then(Commands.literal("reload").executes(ctx -> this.executeReload(ctx)))
				.then(Commands.literal("version").executes(ctx -> this.executeVersion(ctx)))
				.then(Commands.literal("handNBT").executes(ctx -> this.executeHandNBT(ctx)))
				.then(Commands.literal("handID").executes(ctx -> this.executeHandID(ctx)))
				.then(Commands.literal("disableReward").then(Commands.argument("rewardName", new RewardArgument())
		                .executes(ctx -> executeDisableReward(ctx, RewardArgument.func_212592_a(ctx, "rewardName")))))
				.then(Commands.literal("enableReward").then(Commands.argument("rewardName", new RewardArgument())
		                .executes(ctx -> executeEnableReward(ctx, RewardArgument.func_212592_a(ctx, "rewardName")))))
				.then(Commands.literal("schematic").requires(cs -> cs.hasPermissionLevel(2)).requires(cs -> cs.getWorld().isRemote)
						.then(Commands.literal("create").executes(ctx -> this.executeSchematicCreate(ctx)))
						.then(Commands.literal("cancel").executes(ctx -> this.executeSchematicCancel(ctx))))
				.then(Commands.literal("rewardsInfo").executes(ctx -> this.executeRewardInfo(ctx)))
				.then(Commands.literal("test").executes(ctx -> this.executeTest(ctx)))
				.then(Commands.literal("testRewards").executes(ctx -> this.executeTestRewards(ctx)))
				.then(Commands.literal("testCustomRewards").executes(ctx -> this.executeTestCustomRewards(ctx)))
				.then(Commands.literal("spawnGiantCube").then(Commands.argument("pos", BlockPosArgument.blockPos())
		                .executes(ctx -> executeSpawnGiantCube(ctx, BlockPosArgument.getBlockPos(ctx, "pos"))))));
		// @formatter:on

		//		aliases = new ArrayList<String>();
		//		aliases.add("Chancecubes");
		//		aliases.add("chancecubes");
		//		aliases.add("ChanceCube");
		//		aliases.add("Chancecube");
		//		aliases.add("chancecube");
		//		aliases.add("CCubes");
	}

	public EntityPlayerMP getPlayer(CommandSource source)
	{
		try
		{
			return source.asPlayer();
		} catch(CommandSyntaxException e)
		{
			CCubesCore.logger.log(Level.ERROR, "You should never see this. If you do you broke everything. Report to Turkey");
		}
		//Should never get here.
		return null;
	}

	public int executeReload(CommandContext<CommandSource> ctx)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				ChanceCubeRegistry.INSTANCE.ClearRewards();
				GiantCubeRegistry.INSTANCE.ClearRewards();
				ProfileManager.clearProfiles();
				ChanceCubeRegistry.loadDefaultRewards();
				GiantCubeRegistry.loadDefaultRewards();
				CustomRewardsLoader.instance.loadCustomRewards();
				CustomRewardsLoader.instance.fetchRemoteInfo();
				ChanceCubeRegistry.loadCustomUserRewards();
				ModHookUtil.loadCustomModRewards();
				NonreplaceableBlockOverride.loadOverrides();
				ProfileManager.initProfiles();
				CustomProfileLoader.instance.loadProfiles();
				getPlayer(ctx.getSource()).sendMessage(new TextComponentString("Rewards Reloaded"));
			}
		}).start();
		return 0;
	}

	public int executeVersion(CommandContext<CommandSource> ctx)
	{
		getPlayer(ctx.getSource()).sendMessage(new TextComponentString("Chance Cubes Version " + CCubesCore.VERSION));
		return 0;
	}

	public int executeHandNBT(CommandContext<CommandSource> ctx)
	{
		EntityPlayer player = (EntityPlayer) getPlayer(ctx.getSource());
		NBTTagCompound nbt = player.inventory.getCurrentItem().getOrCreateTag();
		player.sendMessage(new TextComponentString(nbt.toString()));
		return 0;
	}

	public int executeHandID(CommandContext<CommandSource> ctx)
	{
		EntityPlayer player = (EntityPlayer) getPlayer(ctx.getSource());
		ItemStack stack = player.inventory.getCurrentItem();
		if(!stack.isEmpty())
		{
			ResourceLocation res = stack.getItem().getRegistryName();
			player.sendMessage(new TextComponentString(res.getNamespace() + ":" + res.getPath()));
			player.sendMessage(new TextComponentString("meta: " + stack.getDamage()));
		}
		return 0;
	}

	public int executeDisableReward(CommandContext<CommandSource> ctx, String reward)
	{
		EntityPlayer player = (EntityPlayer) getPlayer(ctx.getSource());

		if(ChanceCubeRegistry.INSTANCE.disableReward(reward))
			player.sendMessage(new TextComponentString(reward + " Has been temporarily disabled."));
		else
			player.sendMessage(new TextComponentString(reward + " is either not currently enabled or is not a valid reward name."));
		return 0;
	}

	public int executeEnableReward(CommandContext<CommandSource> ctx, String reward)
	{
		EntityPlayer player = (EntityPlayer) getPlayer(ctx.getSource());

		if(ChanceCubeRegistry.INSTANCE.enableReward(reward))
			player.sendMessage(new TextComponentString(reward + " Has been enabled."));
		else
			player.sendMessage(new TextComponentString(reward + " is either not currently disabled or is not a valid reward name."));
		return 0;
	}

	public int executeRewardInfo(CommandContext<CommandSource> ctx)
	{
		getPlayer(ctx.getSource()).sendMessage(new TextComponentString("There are currently " + ChanceCubeRegistry.INSTANCE.getNumberOfLoadedRewards() + " rewards loaded and " + ChanceCubeRegistry.INSTANCE.getNumberOfDisabledRewards() + " rewards disabled"));
		return 0;
	}

	public int executeTestRewards(CommandContext<CommandSource> ctx)
	{
		CCubesSettings.testRewards = !CCubesSettings.testRewards;
		CCubesSettings.testingRewardIndex = 0;
		if(CCubesSettings.testRewards)
			getPlayer(ctx.getSource()).sendMessage(new TextComponentString("Reward testing is now enabled for all rewards!"));
		else
			getPlayer(ctx.getSource()).sendMessage(new TextComponentString("Reward testing is now disabled and normal randomness is back."));
		return 0;
	}

	public int executeTestCustomRewards(CommandContext<CommandSource> ctx)
	{
		CCubesSettings.testCustomRewards = !CCubesSettings.testCustomRewards;
		CCubesSettings.testingRewardIndex = 0;
		if(CCubesSettings.testCustomRewards)
			getPlayer(ctx.getSource()).sendMessage(new TextComponentString("Reward testing is now enabled for custom rewards!"));
		else
			getPlayer(ctx.getSource()).sendMessage(new TextComponentString("Reward testing is now disabled and normal randomness is back."));
		return 0;
	}

	public int executeTest(CommandContext<CommandSource> ctx)
	{
		return 0;
	}

	public int executeSchematicCreate(CommandContext<CommandSource> ctx)
	{
		if(RenderEvent.isCreatingSchematic())
		{
			//Possibly make own packet
			if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
				Minecraft.getInstance().displayGuiScreen(new SchematicCreationGui(getPlayer(ctx.getSource())));
			else
				getPlayer(ctx.getSource()).sendMessage(new TextComponentString("Please set both points before moving on!"));
		}
		else
		{
			RenderEvent.setCreatingSchematic(true);
		}
		return 0;
	}

	public int executeSchematicCancel(CommandContext<CommandSource> ctx)
	{
		RenderEvent.setCreatingSchematic(false);
		SchematicUtil.selectionPoints[0] = null;
		SchematicUtil.selectionPoints[1] = null;
		return 0;
	}

	public int executeSpawnGiantCube(CommandContext<CommandSource> ctx, BlockPos pos)
	{
		EntityPlayerMP player = getPlayer(ctx.getSource());
		World world = player.getEntityWorld();

		if(RewardsUtil.isBlockUnbreakable(world, pos.add(0, 0, 0)) && CCubesSettings.nonReplaceableBlocks.contains(world.getBlockState(pos.add(0, 0, 0))))
			return 0;

		GiantCubeUtil.setupStructure(pos.add(-1, -1, -1), world, true);

		world.playSound(null, pos, CCubesSounds.GIANT_CUBE_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
		return 0;
	}

}