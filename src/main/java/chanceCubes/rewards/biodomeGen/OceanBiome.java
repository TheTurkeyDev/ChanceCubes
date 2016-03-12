package chanceCubes.rewards.biodomeGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.Location3I;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class OceanBiome implements IBioDomeBiome
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
							blocks.add(new OffsetBlock(x, y, z, Blocks.glass, false, delay / delayShorten));
							if(y == 0)
								delay++;
						}
						else if(y == 0)
						{
							blocks.add(new OffsetBlock(x, y, z, Blocks.clay, false, (delay / delayShorten)));
							delay++;
						}
						else
						{
							blocks.add(new OffsetBlock(x, y, z, Blocks.water, false, delay / delayShorten));
						}
					}
				}
			}
			delay += 100;
		}

		return blocks;
	}

	public List<OffsetBlock> addTree(int x, int y, int z, int delay)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();

		for(int yy = 1; yy < 6; yy++)
		{
			blocks.add(new OffsetBlock(x, y + yy, z, Blocks.log, false, delay));
			delay++;
		}

		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				for(int yy = 0; yy < 2; yy++)
				{
					if((xx != 0 || zz != 0))
					{
						blocks.add(new OffsetBlock(x + xx, y + 4 + yy, z + zz, Blocks.leaves, false, delay));
						delay++;
					}
				}
			}
		}

		blocks.add(new OffsetBlock(x + 1, y + 6, z, Blocks.leaves, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x - 1, y + 6, z, Blocks.leaves, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z + 1, Blocks.leaves, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z - 1, Blocks.leaves, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z, Blocks.leaves, false, delay));
		delay++;

		return blocks;
	}

	@Override
	public void spawnEntities(int centerX, int centerY, int centerZ, World world)
	{
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			EntitySquid chicken = new EntitySquid(world);
			chicken.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
			world.spawnEntityInWorld(chicken);
		}
	}
}
