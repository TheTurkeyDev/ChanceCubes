package chanceCubes.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.ItemAndEntityReward;

public class ChanceCubeRegistry
{

	private static List<IChanceCubeReward> rewards = new ArrayList<IChanceCubeReward>();

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public void loadDefaultRewards()
	{
		this.registerReward(new ItemAndEntityReward(CCubesCore.MODID+":RedstoneDiamond", 0,
				new ItemStack[] {new ItemStack(Items.redstone), new ItemStack(Items.diamond)}, null));
		this.registerReward(new ItemAndEntityReward(CCubesCore.MODID+":Creeper", 0, null, new String[] {"Creeper"}));
		this.registerReward(new ItemAndEntityReward(CCubesCore.MODID+":RedstoneZombie", 2,
				new ItemStack[] {new ItemStack(Items.redstone)}, new String[] {"Zombie"}));
		
		//this.registerReward("Test reward 1", new ItemStack(Items.redstone, 1), new ItemStack(Items.diamond, 1));
		//this.registerReward("Test reward 2", "Creeper");
		//this.registerReward("Test reward 3", new ItemStack(Items.redstone, 1),"Zombie");
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
	public void triggerRandomReward(World world, int x, int y, int z)
	{
		Random r = new Random();
		rewards.get(r.nextInt(rewards.size())).trigger(world, x, y, z);
	}
}
