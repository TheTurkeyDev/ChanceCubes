package chanceCubes.rewards;

import net.minecraft.world.World;

public interface IChanceCubeReward {
	
	/**
	 * What occurs when the block is "opened"
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public void trigger(World world, int x, int y, int z);
	
	/**
	 * @return How "lucky" this block is (can be negative). 0 would indicate an "average" reward.
	 */
	public int getLuckValue();
	
	/**
	 * @return Unique name for reward (suggested to pre-pend MODID).
	 */
	public String getName();

}
