package chanceCubes.rewards.biodomeGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.Location3I;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class SnowGlobeBiome implements IBioDomeBiome
{

	private Random rand = new Random();

	@Override
	public List<OffsetBlock> genDome(int centerX, int centerY, int centerZ, World world)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();
		int delay = 0;
		int delayShorten = 10;
		for(int y = 0; y <= 25; y++)
		{
			for(int x = -25; x <= 25; x++)
			{
				for(int z = -25; z <= 25; z++)
				{
					Location3I loc = new Location3I(x, y, z);
					float dist = Math.abs(loc.length()) - 25;
					if(dist < 1)
					{
						if(dist >= 0)
						{
							blocks.add(new OffsetBlock(x, y, z, Blocks.glass, false, (delay / delayShorten)));
							delay++;
						}
						else if(y == 0)
						{
							blocks.add(new OffsetBlock(x, y, z, Blocks.snow, false, (delay / delayShorten)));
							delay++;
							if(dist < 0 && rand.nextInt(5) == 0)
							{
								OffsetBlock osb = new OffsetBlock(x, y + 1, z, Blocks.snow_layer, false, (delay / delayShorten));
								blocks.add(osb);
							}
						}
						else
						{
							blocks.add(new OffsetBlock(x, y, z, Blocks.air, false, (delay / delayShorten)));
						}
					}
				}
			}
		}

		return blocks;
	}

	@Override
	public void spawnEntities(final int centerX, final int centerY, final int centerZ, final World world)
	{
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			EntitySnowman snowman = new EntitySnowman(world);
			snowman.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
			world.spawnEntityInWorld(snowman);
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
					snowball.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
					world.spawnEntityInWorld(snowball);
				}
			}
		});
	}
}
