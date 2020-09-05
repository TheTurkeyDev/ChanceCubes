package chanceCubes.rewards.biodomeGen;

import java.util.List;
import java.util.Random;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public interface IBioDomeBiome
{
	Block getFloorBlock();

	void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay);

	void spawnEntities(BlockPos center, ServerWorld world);

	String getBiomeName();
}