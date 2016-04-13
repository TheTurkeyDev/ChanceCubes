package chanceCubes.rewards.biodomeGen;

import java.util.List;
import java.util.Random;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IBioDomeBiome
{
	public void spawnEntities(int centerX, int centerY, int centerZ, World world);

	public Block getFloorBlock();

	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay);
}
