package chanceCubes.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.BasicReward;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.PotionRewardType;
import chanceCubes.rewards.type.PotionRewardType.PotionType;
import chanceCubes.util.ListSort;

public class ChanceCubeRegistry
{

	private static List<IChanceCubeReward> rewards = new ArrayList<IChanceCubeReward>();
	
	private int range = 75;

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public void loadDefaultRewards()
	{
		this.registerReward(new BasicReward(CCubesCore.MODID+":RedstoneDiamond", -75, new ItemRewardType(new ItemStack(Items.redstone), new ItemStack(Items.diamond))));
		this.registerReward(new BasicReward(CCubesCore.MODID+":Creeper", 0, new EntityRewardType("Creeper")));
		this.registerReward(new BasicReward(CCubesCore.MODID+":RedstoneZombie", 100, new ItemRewardType(new ItemStack(Items.redstone)), new EntityRewardType("Zombie")));
		this.registerReward(new BasicReward(CCubesCore.MODID+":EXP", 25, new ExperienceRewardType(100)));
		this.registerReward(new BasicReward(CCubesCore.MODID+":Potions", 0, new PotionRewardType(PotionType.POISON_II)));
		this.registerReward(new BasicReward(CCubesCore.MODID+":ChatMessage", 0, new MessageRewardType("You have escaped the wrath of the Chance Cubes........."), new MessageRewardType("For now......")));
	}

	/**
	 * Registers the given reward as a possible outcome
	 * @param reward to register
	 */
	public void registerReward(IChanceCubeReward reward)
	{
		rewards.add(reward);
	}

	/**
	 * Unregisters a reward with the given name
	 * @param name of the reward to remove
	 * @return true is a reward was successfully removed, false if a reward was not removed
	 */
	public boolean unregisterReward(String name)
	{
		for(int i = 0; i < rewards.size(); i++)
		{
			IChanceCubeReward reward = rewards.get(i);
			if(reward.getName().equalsIgnoreCase(name))
			{
				rewards.remove(reward);
				return true;
			}
		}
		return false;
	}

	/**
	 * Triggers a random reward in the given world at the given location
	 * @param World
	 * @param x
	 * @param y
	 * @param z
	 */
	public void triggerRandomReward(World world, int x, int y, int z, EntityPlayer player, int luck)
	{
		int lowerIndex = 0;
		int upperIndex = rewards.size();
		int lowerRange = luck - this.range < -100 ? -100: luck - this.range;
		int upperRange = luck + this.range > 100 ? 100: luck + this.range;
		
		for(int i = 0; i < rewards.size(); i++)
		{
			if(rewards.get(i).getLuckValue() >= lowerRange)
			{
				lowerIndex = i;
				break;
			}
		}
		for(int i = rewards.size()-1; i >= 0; i--)
		{
			if(rewards.get(i).getLuckValue() <= upperRange)
			{
				upperIndex = i;
				break;
			}
		}
		
		int pick = world.rand.nextInt(upperIndex-lowerIndex + 1) + lowerIndex;
		rewards.get(pick).trigger(world, x, y, z, player);
	}

	public void processRewards()
	{
		rewards = ListSort.sortList(rewards);
	}
	
	public void setRange(int r)
	{
		this.range = r;
	}
}
