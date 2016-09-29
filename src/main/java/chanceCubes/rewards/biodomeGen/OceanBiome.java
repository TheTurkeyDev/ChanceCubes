package chanceCubes.rewards.biodomeGen;

import java.util.List;
import java.util.Random;

import chanceCubes.rewards.giantRewards.BioDomeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class OceanBiome implements IBioDomeBiome
{
	private Random rand = new Random();

	@Override
	public void spawnEntities(int centerX, int centerY, int centerZ, World world)
	{
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			EntitySquid squid = new EntitySquid(world);
			squid.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
			squid.setCustomNameTag("Mango");
			world.spawnEntityInWorld(squid);
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
