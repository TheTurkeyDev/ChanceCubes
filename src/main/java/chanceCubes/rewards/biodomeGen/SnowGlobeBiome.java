package chanceCubes.rewards.biodomeGen;

import java.util.List;
import java.util.Random;

import chanceCubes.rewards.giantRewards.BioDomeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SnowGlobeBiome implements IBioDomeBiome
{
	private Random rand = new Random();

	@Override
	public void spawnEntities(final BlockPos pos, final World world)
	{
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			int ri = rand.nextInt(2);

			if(ri == 0)
			{
				EntitySnowman snowman = new EntitySnowman(world);
				snowman.setLocationAndAngles(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntity(snowman);
			}
			else if(ri == 0)
			{
				EntityPolarBear polarBear = new EntityPolarBear(world);
				polarBear.setLocationAndAngles(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntity(polarBear);
			}
		}

		Scheduler.scheduleTask(new Task("SnowGlobe Snow", 20)
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < 100; i++)
				{
					EntitySnowball snowball = new EntitySnowball(world);
					snowball.motionX = -1 + (Math.random() * 2);
					snowball.motionY = 0.8;
					snowball.motionZ = -1 + (Math.random() * 2);
					snowball.setLocationAndAngles(pos.getX() + (rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (rand.nextInt(31) - 15), 0, 0);
					world.spawnEntity(snowball);
				}
			}
		});
	}

	@Override
	public Block getFloorBlock()
	{
		return Blocks.SNOW;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y != 0)
			return;
		if(dist < 0 && rand.nextInt(5) == 0)
		{
			OffsetBlock osb = new OffsetBlock(x, y + 1, z, Blocks.SNOW_LAYER, false, (delay / BioDomeReward.delayShorten));
			blocks.add(osb);
		}
	}
}