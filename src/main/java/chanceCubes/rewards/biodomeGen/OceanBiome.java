package chanceCubes.rewards.biodomeGen;

import java.util.List;
import java.util.Random;

import chanceCubes.rewards.giantRewards.BioDomeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OceanBiome implements IBioDomeBiome
{
	private Random rand = new Random();

	@Override
	public void spawnEntities(BlockPos pos, World world)
	{
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			EntitySquid chicken = new EntitySquid(world);
			chicken.setLocationAndAngles(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
			world.spawnEntityInWorld(chicken);
		}
	}

	@Override
	public Block getFloorBlock()
	{
		return Blocks.clay;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y == 0 || dist >= 0)
			return;
		blocks.add(new OffsetBlock(x, y, z, Blocks.water, false, delay / BioDomeReward.delayShorten));
	}
}