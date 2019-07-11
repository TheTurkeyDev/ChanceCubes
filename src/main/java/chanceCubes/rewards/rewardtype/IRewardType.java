package chanceCubes.rewards.rewardtype;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IRewardType
{

	/**
	 * What occurs when the this Reward type is triggered
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public void trigger(World world, int x, int y, int z, PlayerEntity player);
}
