package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IChanceCubeReward {
	
	/**
	 * What occurs when the block is "opened"
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param player Player who triggered the block
	 */
	public void trigger(World world, BlockPos pos, EntityPlayer player);
	
	/**
	 * @return How "lucky" this block is (can be negative). 0 would indicate an "average" reward.
	 */
	public int getChanceValue();
	
	/**
	 * @return Unique name for reward (suggested to pre-pend MODID).
	 */
	public String getName();
}