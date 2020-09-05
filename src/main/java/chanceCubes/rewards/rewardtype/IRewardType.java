package chanceCubes.rewards.rewardtype;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public interface IRewardType
{

	/**
	 * What occurs when the this Reward type is triggered
	 */
	void trigger(ServerWorld world, int x, int y, int z, PlayerEntity player);
}
