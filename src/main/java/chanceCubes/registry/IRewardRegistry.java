package chanceCubes.registry;

import javax.annotation.Nullable;

import chanceCubes.rewards.IChanceCubeReward;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRewardRegistry
{
	/**
	 * Registers the given reward as a possible outcome
	 * 
	 * @param reward
	 *            to register
	 */
	void registerReward(IChanceCubeReward reward);

	/**
	 * Unregisters a reward with the given name
	 * 
	 * @param String
	 *            name of the reward to remove
	 * @return true is a reward was successfully removed, false if a reward was not removed
	 */
	boolean unregisterReward(String name);

	@Nullable
	IChanceCubeReward getRewardByName(String name);

	/**
	 * Triggers a random reward in the given world at the given location
	 * 
	 * @param world
	 *            The world object
	 * @param pos
	 *            The position of the block
	 * @param player
	 *            The player receiving the reward
	 * @param luck
	 *            The chance of the block
	 * @param luckBounds
	 *            Min and max chance values
	 */
	void triggerRandomReward(World world, BlockPos pos, PlayerEntity player, int chance);

	void triggerReward(IChanceCubeReward reward, World world, BlockPos pos, PlayerEntity player);
}
