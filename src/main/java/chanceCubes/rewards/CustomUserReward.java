package chanceCubes.rewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.type.IRewardType;
import chanceCubes.util.HTTPUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CustomUserReward implements IChanceCubeReward
{
	private String userName = "";
	private String type;

	private List<BasicReward> customRewards = new ArrayList<BasicReward>();

	public CustomUserReward(EntityPlayer player)
	{
		if(!CCubesSettings.userSpecificRewards)
			return;

		JsonElement users;
		try
		{
			users = HTTPUtil.getWebFile("https://raw.githubusercontent.com/wyldmods/ChanceCubes/master/customRewards/UserList.json");
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to get the list of users with custom rewards!");
			return;
		}

		for(JsonElement user : users.getAsJsonArray())
		{
			if(user.getAsJsonObject().get("Name").getAsString().equalsIgnoreCase(player.getCommandSenderName()))
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
			userRewards = HTTPUtil.getWebFile("https://raw.githubusercontent.com/wyldmods/ChanceCubes/master/customRewards/Users/" + userName + ".json");
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to get the custom list for " + userName + "!");
			CCubesCore.logger.log(Level.ERROR, e.getMessage());
			return;
		}

		CustomRewardsLoader rewardLoader = CustomRewardsLoader.instance;

		for(Entry<String, JsonElement> reward : userRewards.getAsJsonObject().entrySet())
		{
			List<IRewardType> rewards = new ArrayList<IRewardType>();
			JsonObject rewardElements = reward.getValue().getAsJsonObject();
			int chance = 0;
			for(Entry<String, JsonElement> rewardElement : rewardElements.entrySet())
			{
				if(rewardElement.getKey().equalsIgnoreCase("chance"))
				{
					chance = rewardElement.getValue().getAsInt();
					continue;
				}

				JsonArray rewardTypes = rewardElement.getValue().getAsJsonArray();
				if(rewardElement.getKey().equalsIgnoreCase("Item"))
					rewardLoader.loadItemReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Block"))
					rewardLoader.loadBlockReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Message"))
					rewardLoader.loadMessageReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Command"))
					rewardLoader.loadCommandReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Entity"))
					rewardLoader.loadEntityReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Experience"))
					rewardLoader.loadExperienceReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Potion"))
					rewardLoader.loadPotionReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Schematic"))
					rewardLoader.loadSchematicReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Sound"))
					rewardLoader.loadSoundReward(rewardTypes, rewards);
				else if(rewardElement.getKey().equalsIgnoreCase("Chest"))
					rewardLoader.loadChestReward(rewardTypes, rewards);
			}

			customRewards.add(new BasicReward(reward.getKey(), chance, rewards.toArray(new IRewardType[rewards.size()])));
		}

		ChanceCubeRegistry.INSTANCE.registerReward(this);
		player.addChatMessage(new ChatComponentText("Seems you have some custom rewards...."));
		player.addChatMessage(new ChatComponentText("Let the fun begin! >:)"));
	}

	@Override
	public void trigger(final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		if(!player.getCommandSenderName().equalsIgnoreCase(this.userName))
		{
			player.addChatMessage(new ChatComponentText("Hey you aren't " + this.userName + "! You cant have his reward! Try again!"));
			Entity itemEnt = new EntityItem(world, x, y, z, new ItemStack(CCubesBlocks.chanceCube, 1));
			world.spawnEntityInWorld(itemEnt);
			return;
		}
		player.addChatMessage(new ChatComponentText("Selecting best (possibly deadly) reward for " + this.type + " " + this.userName));

		Task task = new Task("Custom Reward", 100)
		{
			@Override
			public void callback()
			{
				triggerAcutalReward(world, x, y, z, player);
			}
		};

		Scheduler.scheduleTask(task);

	}

	public void triggerAcutalReward(World world, int x, int y, int z, EntityPlayer player)
	{
		this.customRewards.get(world.rand.nextInt(this.customRewards.size())).trigger(world, x, y, z, player);
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Custom_Reward_For_" + this.userName;
	}

}
