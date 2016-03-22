package chanceCubes.rewards.biodomeGen;

import java.util.List;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBioDomeBiome
{
	public List<OffsetBlock> genDome(BlockPos center, World world);

	public void spawnEntities(BlockPos center, World world);
}