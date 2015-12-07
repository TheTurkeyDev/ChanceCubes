package chanceCubes.rewards.biodomeGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;

import chanceCubes.rewards.rewardparts.OffsetBlock;

public class DesertBiome implements IBioDomeBiome
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
							blocks.add(new OffsetBlock(x, y, z, Blocks.sand, false, (delay / delayShorten) + 21));
							blocks.add(new OffsetBlock(x, y - 1, z, Blocks.sandstone, false, (delay / delayShorten) + 20));
							delay++;
							if(dist < 0 && rand.nextInt(50) == 0)
							{
								OffsetBlock osb = new OffsetBlock(x, y + 1, z, Blocks.deadbush, false, (delay / delayShorten) + 20);
								blocks.add(osb);
								delay++;
							}
							
							if(dist < 0 && rand.nextInt(60) == 0)
							{
								OffsetBlock osb = new OffsetBlock(x, y + 1, z, Blocks.cactus, false, (delay / delayShorten) + 20);
								blocks.add(osb);
								delay++;
								osb = new OffsetBlock(x, y + 2, z, Blocks.cactus, false, (delay / delayShorten) + 20);
								blocks.add(osb);
								delay++;
								osb = new OffsetBlock(x, y + 3, z, Blocks.cactus, false, (delay / delayShorten) + 20);
								blocks.add(osb);
								delay++;
							}

							/*if(dist < -5 && rand.nextInt(100) == 0)
							{
								List<OffsetBlock> treeblocks = this.addTree(x, y, z, (delay / delayShorten));
								blocks.addAll(treeblocks);
							}*/
						}
					}
				}
			}
		}

		return blocks;
	}
}