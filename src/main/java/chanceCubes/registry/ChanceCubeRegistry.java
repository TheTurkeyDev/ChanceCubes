package chanceCubes.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.BasicReward;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ItemRewardType;

public class ChanceCubeRegistry
{

	private static List<IChanceCubeReward> rewards = new ArrayList<IChanceCubeReward>();

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public void loadDefaultRewards()
	{
		this.registerReward(new BasicReward(CCubesCore.MODID+":RedstoneDiamond", -50, new ItemRewardType(new ItemStack(Items.redstone), new ItemStack(Items.diamond))));
		this.registerReward(new BasicReward(CCubesCore.MODID+":Creeper", 0, new EntityRewardType("Creeper")));
		this.registerReward(new BasicReward(CCubesCore.MODID+":RedstoneZombie", 50, new ItemRewardType(new ItemStack(Items.redstone)), new EntityRewardType("Zombie")));
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
	public void triggerRandomReward(World world, int x, int y, int z, EntityPlayer player, double luck)
	{
		double[] chances = new double[rewards.size()];
		for (int i = 0; i < chances.length; i++)
		{
			chances[i] = rewards.get(i).getLuckValue() + (world.rand.nextDouble()-0.5)*0.001; //Little bit of noise.
		}
		
		int minIndex = 0;
		double min = Double.MAX_VALUE;
		double r = world.rand.nextGaussian() + luck;
		
		for (int index = 0; index < chances.length; index++)
		{
			double curr = Math.abs(chances[index] - r);
			if (curr < min)
			{
				min = curr;
				minIndex = index;
			}
		}
		
		rewards.get(minIndex).trigger(world, x, y, z, player);
	}
}
