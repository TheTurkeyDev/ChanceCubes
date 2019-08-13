package chanceCubes.rewards.rewardtype;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IRewardType
{

	/**
	 * What occurs when the this Reward type is triggered
	 */
	void trigger(World world, int x, int y, int z, EntityPlayer player);
}
