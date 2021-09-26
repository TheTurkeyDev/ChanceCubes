package chanceCubes.rewards;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public interface IChanceCubeReward
{

	/**
	 * What occurs when the block is "opened"
	 *
	 * @param player Player who triggered the block
	 */
	void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings);

	/**
	 * @return How "lucky" this block is (can be negative). 0 would indicate an "average" reward. Range -100 to 100.
	 */
	int getChanceValue();

	/**
	 * @return Unique name for reward (suggested to pre-pend MODID).
	 */
	String getName();
}