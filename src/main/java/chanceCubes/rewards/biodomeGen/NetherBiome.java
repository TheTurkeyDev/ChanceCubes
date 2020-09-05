package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class NetherBiome extends BaseBiome
{

	public NetherBiome(String name)
	{
		super(name);
	}

	@Override
	public void spawnEntities(BlockPos center, ServerWorld world)
	{
		Random rand = RewardsUtil.rand;
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			int ri = rand.nextInt(5);

			if(ri == 0)
			{
				GhastEntity ghast = EntityType.GHAST.create(world);
				ghast.setLocationAndAngles(center.getX() + (rand.nextInt(31) - 15), center.getY() + 5, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.addEntity(ghast);
			}
			else
			{
				ZombifiedPiglinEntity pigman = EntityType.ZOMBIFIED_PIGLIN.create(world);
				pigman.setLocationAndAngles(center.getX() + (rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.addEntity(pigman);
			}
		}
	}

	@Override
	public Block getFloorBlock()
	{
		return Blocks.NETHERRACK;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y != 0)
			return;

		if(dist < 0 && rand.nextInt(50) == 0)
		{
			OffsetBlock osb = new OffsetBlock(x, y - 1, z, Blocks.NETHERRACK, false, (delay / BioDomeGen.delayShorten));
			blocks.add(osb);
			delay++;
			osb = new OffsetBlock(x, y, z, Blocks.LAVA, false, (delay / BioDomeGen.delayShorten) + 1);
			blocks.add(osb);
		}
		else if(dist < 0 && rand.nextInt(20) == 0)
		{
			OffsetBlock osb = new OffsetBlock(x, y, z, Blocks.SOUL_SAND, false, (delay / BioDomeGen.delayShorten) + 1);
			blocks.add(osb);
		}
	}
}