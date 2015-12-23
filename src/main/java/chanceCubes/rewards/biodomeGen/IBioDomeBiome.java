package chanceCubes.rewards.biodomeGen;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.OffsetBlock;

public interface IBioDomeBiome
{
	public List<OffsetBlock> genDome(BlockPos center, World world);

	public void spawnEntities(BlockPos center, World world);
}