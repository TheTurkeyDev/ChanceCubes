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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fmllegacy.LogicalSidedProvider;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class CustomUserReward extends BaseCustomReward
{
	private final String userName;
	private final UUID uuid;
	private final String type;
	private final List<BasicReward> customRewards;

	public static void getCustomUserReward(UUID uuid)
	{
		if(!CCubesSettings.userSpecificRewards.get())
			return;

		JsonElement users;
		try
		{
			users = HTTPUtil.makeAPIReq("GET", "chancecubes/userlist");
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
			userRewards = HTTPUtil.makeAPIReq("GET", "chancecubes/user/" + userName);
			contentCreatorStuff = HTTPUtil.makeAPIReq("GET", "chancecubes/contentcreatorinfo").getAsJsonObject();
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to get the custom list for " + userName + "!");
			CCubesCore.logger.log(Level.ERROR, e.getMessage());
			return;
		}

		if(userRewards.isJsonNull() || contentCreatorStuff.isJsonNull())
			return;

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
			Player player = server.getPlayerList().getPlayer(uuid);
			if(player == null)
				return;

			Style ccStyle = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_AQUA));

			if(contentCreatorStuff.get("Active").getAsBoolean() && !twitchFinal.trim().equals(""))
				PlayerCCRewardRegistry.streamerReward.put(uuid, new StreamerReward(twitchFinal, contentCreatorStuff.getAsJsonArray("Options")));

			if(contentCreatorStuff.get("Messages").getAsJsonArray().size() > 0)
			{
				for(JsonElement messageElem : contentCreatorStuff.get("Messages").getAsJsonArray())
				{
					String message = messageElem.getAsJsonObject().get("message").getAsString();
					message = message.replace("%username%", userNameFinal);
					RewardsUtil.sendMessageToPlayer(player, new TextComponent(message).setStyle(ccStyle));
				}
			}
			else
			{
				RewardsUtil.sendMessageToPlayer(player, new TextComponent("Seems you have some custom Chance Cubes rewards " + userNameFinal + "....").setStyle(ccStyle));
				RewardsUtil.sendMessageToPlayer(player, new TextComponent("Let the fun begin! >:)").setStyle(ccStyle));
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
	public void trigger(final ServerLevel level, final BlockPos pos, final Player player, JsonObject settings)
	{

		if(!UsernameCache.getLastKnownUsername(uuid).equalsIgnoreCase(player.getName().getString()))
		{
			RewardsUtil.sendMessageToPlayer(player, "Hey you aren't " + this.userName + "! You can't have their reward! Try again!");
			Entity itemEnt = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
			level.addFreshEntity(itemEnt);
			return;
		}
		RewardsUtil.sendMessageToPlayer(player, "Selecting best (possibly deadly) reward for " + this.type + " " + this.userName);

		Scheduler.scheduleTask(new Task("Custom Reward", 100)
		{
			@Override
			public void callback()
			{
				customRewards.get(RewardsUtil.rand.nextInt(customRewards.size())).trigger(level, pos, player, settings);
			}
		});
	}

	public UUID getUuid()
	{
		return uuid;
	}
}
