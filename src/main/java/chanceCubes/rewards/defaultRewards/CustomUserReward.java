package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.parsers.RewardParser;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.registry.player.PlayerCCRewardRegistry;
import chanceCubes.util.HTTPUtil;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class CustomUserReward extends BaseCustomReward
{
	private String userName;
	private UUID uuid;
	private String type;
	private List<BasicReward> customRewards;

	public static void getCustomUserReward(UUID uuid)
	{
		if(!CCubesSettings.userSpecificRewards.get())
			return;

		JsonElement users;
		try
		{
			users = HTTPUtil.getWebFile("GET", "https://api.theprogrammingturkey.com/chance_cubes/custom_rewards/UserList.json");
		} catch(Exception e)
		{
			e.printStackTrace();
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to get the list of users with custom rewards!");
			return;
		}

		String userName = "";
		String type = "";
		String twitch = "";
		for(JsonElement userElem : users.getAsJsonArray())
		{
			JsonObject user = userElem.getAsJsonObject();
			if(user.get("UUID").getAsString().equalsIgnoreCase(uuid.toString()))
			{
				userName = user.get("Name").getAsString();
				type = user.get("Type").getAsString();
				if(user.has("Twitch"))
					twitch = user.get("Twitch").getAsString();
			}
		}

		if(userName.equals(""))
		{
			CCubesCore.logger.log(Level.INFO, "No custom rewards detected for the current user!");
			return;
		}

		JsonElement userRewards;
		JsonObject contentCreatorStuff;

		try
		{
			userRewards = HTTPUtil.getWebFile("GET", "https://api.theprogrammingturkey.com/chance_cubes/custom_rewards/users/" + userName + ".json");
			contentCreatorStuff = HTTPUtil.getWebFile("GET", "https://api.theprogrammingturkey.com/chance_cubes/custom_rewards/ContentCreators.json").getAsJsonObject();
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to get the custom list for " + userName + "!");
			CCubesCore.logger.log(Level.ERROR, e.getMessage());
			return;
		}

		List<BasicReward> customRewards = new ArrayList<>();

		for(Entry<String, JsonElement> reward : userRewards.getAsJsonObject().entrySet())
			customRewards.add(RewardParser.parseReward(reward).getKey());

		//GROSS, but idk what else to do
		String userNameFinal = userName;
		String typeFinal = type;
		String twitchFinal = twitch;
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		server.execute(() ->
		{
			GlobalCCRewardRegistry.DEFAULT.registerReward(new CustomUserReward(userNameFinal, uuid, typeFinal, customRewards));
			GlobalCCRewardRegistry.DEFAULT.getPlayerRewardRegistry(uuid.toString()).enableReward(CCubesCore.MODID + ":cr_" + userNameFinal);
			PlayerEntity player = server.getPlayerList().getPlayerByUUID(uuid);
			if(player == null)
				return;

			Style ccStyle = Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.DARK_AQUA));

			if(contentCreatorStuff.get("Active").getAsBoolean() && !twitchFinal.trim().equals(""))
				PlayerCCRewardRegistry.streamerReward.put(uuid, new StreamerReward(twitchFinal, contentCreatorStuff.getAsJsonArray("Options")));

			if(contentCreatorStuff.get("Messages").getAsJsonArray().size() > 0)
			{
				for(JsonElement messageElem : contentCreatorStuff.get("Messages").getAsJsonArray())
				{
					String message = messageElem.getAsJsonObject().get("message").getAsString();
					message = message.replace("%username%", userNameFinal);
					RewardsUtil.sendMessageToPlayer(player, new StringTextComponent(message).setStyle(ccStyle));
				}
			}
			else
			{
				RewardsUtil.sendMessageToPlayer(player, new StringTextComponent("Seems you have some custom Chance Cubes rewards " + userNameFinal + "....").setStyle(ccStyle));
				RewardsUtil.sendMessageToPlayer(player, new StringTextComponent("Let the fun begin! >:)").setStyle(ccStyle));
			}
		});
	}

	public CustomUserReward(String un, UUID uuid, String type, List<BasicReward> rewards)
	{
		super(CCubesCore.MODID + ":cr_" + un, 0);
		this.userName = un;
		this.uuid = uuid;
		this.type = type;
		this.customRewards = rewards;
	}

	@Override
	public void trigger(final ServerWorld world, final BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{

		if(!UsernameCache.getLastKnownUsername(uuid).equalsIgnoreCase(player.getName().getUnformattedComponentText()))
		{
			RewardsUtil.sendMessageToPlayer(player, "Hey you aren't " + this.userName + "! You can't have their reward! Try again!");
			Entity itemEnt = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
			world.addEntity(itemEnt);
			return;
		}
		RewardsUtil.sendMessageToPlayer(player, "Selecting best (possibly deadly) reward for " + this.type + " " + this.userName);

		Scheduler.scheduleTask(new Task("Custom Reward", 100)
		{
			@Override
			public void callback()
			{
				customRewards.get(world.rand.nextInt(customRewards.size())).trigger(world, pos, player, settings);
			}
		});
	}

	public UUID getUuid()
	{
		return uuid;
	}
}
