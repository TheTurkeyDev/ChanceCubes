package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Random;

public class NetherBiome extends BaseBiome
{

	public NetherBiome(String name)
	{
		super(name);
	}

	@Override
	public void spawnEntities(BlockPos center, ServerLevel world)
	{
		Random rand = RewardsUtil.rand;
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			int ri = rand.nextInt(5);

			if(ri == 0)
			{
				Ghast ghast = EntityType.GHAST.create(world);
				ghast.moveTo(center.getX() + (rand.nextInt(31) - 15), center.getY() + 5, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.addFreshEntity(ghast);
			}
			else
			{
				ZombifiedPiglin pigman = EntityType.ZOMBIFIED_PIGLIN.create(world);
				pigman.moveTo(center.getX() + (rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.addFreshEntity(pigman);
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