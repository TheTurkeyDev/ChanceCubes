package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonElement;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.HTTPUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CustomUserReward implements IChanceCubeReward
{
	private String userName = "";
	private UUID uuid = null;
	private String type;

	private List<BasicReward> customRewards = new ArrayList<BasicReward>();

	public static void getCustomUserReward(UUID uuid)
	{
		String userName;
		String type;
		if(!CCubesSettings.userSpecificRewards)
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

		List<BasicReward> customRewards = new ArrayList<BasicReward>();
		for(Entry<String, JsonElement> reward : userRewards.getAsJsonObject().entrySet())
		{
			customRewards.add(CustomRewardsLoader.instance.parseReward(reward).getKey());
		}

		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				ChanceCubeRegistry.INSTANCE.registerReward(new CustomUserReward(uuid, userName, type, customRewards));
				EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);

				if(player != null)
				{
					player.sendMessage(new TextComponentString("Seems you have some custom Chance Cubes rewards " + userName + "...."));
					player.sendMessage(new TextComponentString("Let the fun begin! >:)"));
				}
			}
		});
	}

	public CustomUserReward(UUID uuid, String userName, String type, List<BasicReward> customRewards)
	{
		this.uuid = uuid;
		this.userName = userName;
		this.type = type;
		this.customRewards = customRewards;
	}

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player)
	{

		if(!UsernameCache.getLastKnownUsername(uuid).equalsIgnoreCase(player.getName().getString()))
		{
			player.sendMessage(new TextComponentString("Hey you aren't " + this.userName + "! You can't have their reward! Try again!"));
			Entity itemEnt = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
			world.spawnEntity(itemEnt);
			return;
		}

		player.sendMessage(new TextComponentString("Selecting best (possibly deadly) reward for " + this.type + " " + this.userName));

		Scheduler.scheduleTask(new Task("Custom Reward", 100)
		{
			@Override
			public void callback()
			{
				customRewards.get(world.rand.nextInt(customRewards.size())).trigger(world, pos, player);
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":CR_" + this.userName;
	}
}
