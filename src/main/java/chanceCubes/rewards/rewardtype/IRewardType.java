package chanceCubes.rewards.rewardtype;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public interface IRewardType
{

	/**
	 * What occurs when this Reward type is triggered
	 */
	void trigger(ServerLevel level, int x, int y, int z, Player player);
}
