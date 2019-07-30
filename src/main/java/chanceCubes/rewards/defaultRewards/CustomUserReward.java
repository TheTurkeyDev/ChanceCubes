package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonElement;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.util.HTTPUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

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
		for(JsonElement user : users.getAsJsonArray())
		{
			if(user.getAsJsonObject().get("UUID").getAsString().equalsIgnoreCase(uuid.toString()))
			{
				userName = user.getAsJsonObject().get("Name").getAsString();
				type = user.getAsJsonObject().get("Type").getAsString();
			}
		}

		if(userName.equals(""))
		{
			CCubesCore.logger.log(Level.INFO, "No custom rewards detected for the current user!");
			return;
		}

		JsonElement userRewards;

		try
		{
			userRewards = HTTPUtil.getWebFile("GET", "https://api.theprogrammingturkey.com/chance_cubes/custom_rewards/users/" + userName + ".json");
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to get the custom list for " + userName + "!");
			CCubesCore.logger.log(Level.ERROR, e.getMessage());
			return;
		}

		List<BasicReward> customRewards = new ArrayList<>();
		for(Entry<String, JsonElement> reward : userRewards.getAsJsonObject().entrySet())
			customRewards.add(CustomRewardsLoader.instance.parseReward(reward).getKey());

		//GROSS, but idk what else todo
		String userNameFinal = userName;
		String typeFinal = type;
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		server.execute(() ->
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new CustomUserReward(uuid, userNameFinal, typeFinal, customRewards));
			PlayerEntity player = server.getPlayerList().getPlayerByUUID(uuid);

			if(player != null)
			{
				player.sendMessage(new StringTextComponent("Seems you have some custom Chance Cubes rewards " + userNameFinal + "...."));
				player.sendMessage(new StringTextComponent("Let the fun begin! >:)"));
			}
		});
	}

	public CustomUserReward(UUID uuid, String userName, String type, List<BasicReward> customRewards)
	{
		super(CCubesCore.MODID + ":CR_" + userName, 0);
		this.uuid = uuid;
		this.userName = userName;
		this.type = type;
		this.customRewards = customRewards;
	}

	@Override
	public void trigger(final World world, final BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{

		if(!UsernameCache.getLastKnownUsername(uuid).equalsIgnoreCase(player.getName().getString()))
		{
			player.sendMessage(new StringTextComponent("Hey you aren't " + this.userName + "! You can't have their reward! Try again!"));
			Entity itemEnt = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
			world.addEntity(itemEnt);
			return;
		}

		player.sendMessage(new StringTextComponent("Selecting best (possibly deadly) reward for " + this.type + " " + this.userName));

		Scheduler.scheduleTask(new Task("Custom Reward", 100)
		{
			@Override
			public void callback()
			{
				customRewards.get(world.rand.nextInt(customRewards.size())).trigger(world, pos, player, settings);
			}
		});
	}
}
