package chanceCubes.rewards.biodomeGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.Location3I;

public class BasicTreesBiome implements IBioDomeBiome
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
							blocks.add(new OffsetBlock(x, y, z, Blocks.grass, false, (delay / delayShorten)));
							delay++;
							if(dist < 0 && rand.nextInt(5) == 0)
							{
								OffsetBlock osb = new OffsetBlock(x, y + 1, z, Blocks.tallgrass, false, (delay / delayShorten));
								osb.setData((byte) 1);
								blocks.add(osb);
								delay++;
							}
							else if(dist < -5 && rand.nextInt(100) == 0)
							{
								List<OffsetBlock> treeblocks = this.addTree(x, y, z, (delay / delayShorten));
								blocks.addAll(treeblocks);
							}
						}
					}
				}
			}
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
			int ri = rand.nextInt(5);
			
			if(ri == 0)
			{
				EntityChicken chicken = new EntityChicken(world);
				chicken.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(chicken);
			}
			else if(ri == 1)
			{
				EntityCow cow = new EntityCow(world);
				cow.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(cow);
			}
			else if(ri == 2)
			{
				EntityHorse horse = new EntityHorse(world);
				horse.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(horse);
			}
			else if(ri == 3)
			{
				EntityPig pig = new EntityPig(world);
				pig.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(pig);
			}
			else if(ri == 4)
			{
				EntitySheep sheep = new EntitySheep(world);
				sheep.setLocationAndAngles(centerX + (rand.nextInt(31) - 15), centerY + 1, centerZ + (rand.nextInt(31) - 15), 0, 0);
				world.spawnEntityInWorld(sheep);
			}
		}
	}
}
