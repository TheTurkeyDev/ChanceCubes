package chanceCubes.listeners;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WorldGen 
{
	@SubscribeEvent
	public void onGenerate(PopulateChunkEvent.Pre event)
	{
		if(CCubesSettings.isBlockedWorld(event.world.getWorldInfo().getWorldName()))
			return;
		if(CCubesSettings.oreGeneration)
			generateOre(event.world, new Random(), event.chunkX * 16, event.chunkZ * 16);

		if(CCubesSettings.surfaceGeneration)
			generateSurface(event.world, new Random(), event.chunkX * 16, event.chunkZ * 16);
	}

	private void generateOre(World world, Random rand, int x, int z) 
	{ 
		for(int k = 0; k < CCubesSettings.oreGenAmount; k++)
		{
			int firstBlockXCoord = x + rand.nextInt(16);
			int firstBlockYCoord = rand.nextInt(100);
			int firstBlockZCoord = z + rand.nextInt(16);

			(new WorldGenMinable(CCubesBlocks.chanceCube, 3)).generate(world, rand, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
		}
	}

	public void generateSurface(World world, Random rand, int x, int z)
	{
		if(rand.nextInt(100) < CCubesSettings.surfaceGenAmount)
		{
			int xCord = x + rand.nextInt(16);
			int zCord = z + rand.nextInt(16);
			int yCord = world.getTopSolidOrLiquidBlock(xCord, zCord);

			world.setBlock(xCord, yCord, zCord, CCubesBlocks.chanceCube);
		}
	}
}