package chanceCubes;

import java.util.Random;

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
		if(CCubesSettings.oreGeneration)
			generateOre(world, random, chunkX * 16, chunkZ * 16);

		if(CCubesSettings.surfaceGeneration)
			generateSurface(world, random, chunkX * 16, chunkZ * 16);
	}

	private void generateOre(World world, Random rand, int x, int z) 
	{ 
		for(int k = 0; k < 8; k++)
		{
			int firstBlockXCoord = x + rand.nextInt(16);
			int firstBlockYCoord = rand.nextInt(100);
			int firstBlockZCoord = z + rand.nextInt(16);

			(new WorldGenMinable(CCubesBlocks.chanceCube, 3)).generate(world, rand, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
		}
	}

	public void generateSurface(World world, Random rand, int x, int z)
	{
		for (int l = 0; l < 8; ++l)
		{
			int xCord = x + rand.nextInt(16);
			int zCord = z + rand.nextInt(16);
			int yCord = world.getTopSolidOrLiquidBlock(xCord, zCord);

			world.setBlock(xCord, yCord, zCord, CCubesBlocks.chanceCube);
		}
	}
}