package chanceCubes.rewards.biodomeGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;

import chanceCubes.rewards.rewardparts.OffsetBlock;

public class EndBiome implements IBioDomeBiome
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
					Vector3f vector = new Vector3f(x, y, z);
					float dist = Math.abs(vector.length()) - 25;
					if(dist < 1)
					{
						if(dist >= 0)
						{
							blocks.add(new OffsetBlock(x, y, z, Blocks.glass, false, (delay / delayShorten) + 20));
							delay++;
						}
						else if(y == 0)
						{
							blocks.add(new OffsetBlock(x, y, z, Blocks.end_stone, false, (delay / delayShorten) + 20));
							delay++;

							if(dist < -5 && rand.nextInt(200) == 0)
							{
								List<OffsetBlock> treeblocks = this.addTower(x, y, z, (delay / delayShorten));
								blocks.addAll(treeblocks);
							}
						}
					}
				}
			}
		}

		return blocks;
	}

	public List<OffsetBlock> addTower(int x, int y, int z, int delay)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();

		for(int yy = 0; yy < 10; yy++)
		{
			for(int xx = -1; xx < 2; xx++)
			{
				for(int zz = -1; zz < 2; zz++)
				{
					blocks.add(new OffsetBlock(x + xx, y + yy, z + zz, Blocks.obsidian, false, delay));
					delay++;
				}
			}
		}
		blocks.add(new OffsetBlock(x, y + 10, z, Blocks.bedrock, false, delay));
		return blocks;
	}
}
