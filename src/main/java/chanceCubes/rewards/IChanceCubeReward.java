package chanceCubes.rewards;

import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;
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
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings);
	
	/**
	 * @return How "lucky" this block is (can be negative). 0 would indicate an "average" reward. Range -100 to 100.
	 */
	public int getChanceValue();
	
	/**
	 * @return Set how "lucky" this block is (can be negative). 0 would indicate an "average" reward. Range -100 to 100.
	 */
	public void setChanceValue(int chance);
	
	/**
	 * @return Unique name for reward (suggested to pre-pend MODID).
	 */
	public String getName();
}