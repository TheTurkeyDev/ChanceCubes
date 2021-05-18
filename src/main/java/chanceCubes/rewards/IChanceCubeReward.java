package chanceCubes.rewards;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public interface IChanceCubeReward
{

	/**
	 * What occurs when the block is "opened"
	 *
	 * @param player Player who triggered the block
	 */
	void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings);

	/**
	 * @return How "lucky" this block is (can be negative). 0 would indicate an "average" reward. Range -100 to 100.
	 */
	int getChanceValue();

	/**
	 * @return Unique name for reward (suggested to pre-pend MODID).
	 */
	String getName();
}