package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class SnowGlobeBiome extends BaseBiome
{
	public SnowGlobeBiome(String name)
	{
		super(name);
	}

	@Override
	public void spawnEntities(final BlockPos pos, final World world)
	{
		Random rand = RewardsUtil.rand;
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			int ri = rand.nextInt(2);

			if(ri == 0)
			{
				SnowGolemEntity snowman = EntityType.SNOW_GOLEM.create(world);
				snowman.setLocationAndAngles(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.addEntity(snowman);
			}
			else if(ri == 1)
			{
				PolarBearEntity polarBear = EntityType.POLAR_BEAR.create(world);
				polarBear.setLocationAndAngles(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.addEntity(polarBear);
			}
		}

		Scheduler.scheduleTask(new Task("SnowGlobe Snow", 20)
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < 100; i++)
				{
					SnowballEntity snowball = EntityType.SNOWBALL.create(world);
					snowball.setMotion(-1 + (Math.random() * 2), 0.8, -1 + (Math.random() * 2));
					snowball.setLocationAndAngles(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
					world.addEntity(snowball);
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