package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Random;

public class SnowGlobeBiome extends BaseBiome
{
	public SnowGlobeBiome(String name)
	{
		super(name);
	}

	@Override
	public void spawnEntities(final BlockPos pos, final ServerLevel level)
	{
		Random rand = RewardsUtil.rand;
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			int ri = rand.nextInt(2);

			if(ri == 0)
			{
				SnowGolem snowman = EntityType.SNOW_GOLEM.create(level);
				snowman.moveTo(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
				level.addFreshEntity(snowman);
			}
			else if(ri == 1)
			{
				PolarBear polarBear = EntityType.POLAR_BEAR.create(level);
				polarBear.moveTo(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
				level.addFreshEntity(polarBear);
			}
		}

		Scheduler.scheduleTask(new Task("SnowGlobe Snow", 20)
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < 100; i++)
				{
					Snowball snowball = EntityType.SNOWBALL.create(level);
					snowball.setDeltaMovement(-1 + (Math.random() * 2), 0.8, -1 + (Math.random() * 2));
					snowball.moveTo(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
					level.addFreshEntity(snowball);
				}
			}
		});
	}

	@Override
	public Block getFloorBlock()
	{
		return Blocks.SNOW_BLOCK;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y != 0)
			return;
		if(dist < 0 && rand.nextInt(5) == 0)
		{
			OffsetBlock osb = new OffsetBlock(x, y + 1, z, Blocks.SNOW, false, (delay / BioDomeGen.delayShorten));
			blocks.add(osb);
		}
	}
}