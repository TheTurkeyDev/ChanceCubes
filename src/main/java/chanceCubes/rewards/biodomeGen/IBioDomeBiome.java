package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Random;

public interface IBioDomeBiome
{
	Block getFloorBlock();

	void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay);

	void spawnEntities(BlockPos center, ServerLevel level);

	String getBiomeName();
}