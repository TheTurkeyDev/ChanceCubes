package chanceCubes;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator 
{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		if(CCubesSettings.isBlockedWorld(world.getWorldInfo().getWorldName()))
			return;
		else if(!CCubesSettings.oreGeneration && !CCubesSettings.surfaceGeneration)
			return;
		else if(CCubesSettings.oreGeneration && !CCubesSettings.surfaceGeneration)
			generateOre(world, random, chunkX * 16, chunkZ * 16);
		else if(!CCubesSettings.oreGeneration && CCubesSettings.surfaceGeneration)
		{
			int j = chunkX + random.nextInt(16) + 8;
			int k = chunkZ + random.nextInt(16) + 8;
			int  l = nextInt(world.getHeightValue(j, k) * 2, random);
			generateSurface(world, random, j, k, l);
		}
		else
		{
			int j = chunkX + random.nextInt(16) + 8;
			int k = chunkZ + random.nextInt(16) + 70;
			int  l = nextInt(world.getHeightValue(j, k) * 2, random);
			generateOre(world, random, chunkX * 16, chunkZ * 16);
			generateSurface(world, random, j, k, l);
		}

	}

	private void generateOre(World world, Random rand, int chunkX, int chunkZ) 
	{
		for(int k = 0; k < 8; k++)
		{
			int firstBlockXCoord = chunkX + rand.nextInt(16);
			int firstBlockYCoord = rand.nextInt(100);
			int firstBlockZCoord = chunkZ + rand.nextInt(16);

			(new WorldGenMinable(CCubesBlocks.chanceCube, 3)).generate(world, rand, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
		}
	}

	public boolean generateSurface(World world, Random rand, int x, int y, int z)
	{
		for (int l = 0; l < 8; ++l)
		{
			int i1 = x + rand.nextInt(8) - rand.nextInt(8);
			int j1 = y + rand.nextInt(4) - rand.nextInt(4);
			int k1 = z + rand.nextInt(8) - rand.nextInt(8);

			if (world.isAirBlock(i1, j1, k1) && world.getBlock(i1, j1 - 1, k1).isOpaqueCube())
			{
				System.out.println("Gen'd at (" + i1 + ", " + j1 + ", " + k1 + ")");
				world.setBlock(i1, j1, k1, CCubesBlocks.chanceCube);
			}
		}

		return true;
	}

	private int nextInt(int i, Random r) {
		if (i <= 1)
			return 0;
		return r.nextInt(i);
	}
}