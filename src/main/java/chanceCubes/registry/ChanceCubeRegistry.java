package chanceCubes.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import chanceCubes.rewards.ChanceCubeReward;

public class ChanceCubeRegistry
{

	private List<ChanceCubeReward> rewards = new ArrayList<ChanceCubeReward>();

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public void loadDefaultRewards()
	{
		this.registerReward("Test reward 1", new ItemStack(Items.redstone, 1), new ItemStack(Items.diamond, 1));
		this.registerReward("Test reward 2", "Creeper");
		this.registerReward("Test reward 3", new ItemStack(Items.redstone, 1),"Zombie");
	}

	/**
	 * Registers a reward with the given name and given objects
	 * Valid objects are
	 * - ItemStack
	 * - Entity
	 * 
	 * @param name of the reward to add
	 * @param Objects to add to the reward when the block is broken
	 */
	public void registerReward(String name, Object... args)
	{
		List<ItemStack> stack = new ArrayList<ItemStack>();
		List<String> ents = new ArrayList<String>();

		for(Object s: args)
		{
			if(s instanceof ItemStack)
				stack.add((ItemStack) s);
			if(s instanceof String)
				ents.add((String) s);
		}

		ChanceCubeReward reward = new ChanceCubeReward(name, stack, ents);
		rewards.add(reward);
	}

	/**
	 * Registers the given reward as a possible outcome
	 * @param reward to register
	 */
	public void registerReward(ChanceCubeReward reward)
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
			ChanceCubeReward reward = rewards.get(i);
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
