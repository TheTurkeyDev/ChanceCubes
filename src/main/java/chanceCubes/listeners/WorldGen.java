package chanceCubes.listeners;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;

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

			(new WorldGenMinable(CCubesBlocks.chanceCube.getDefaultState(), 3)).generate(world, rand, new BlockPos(firstBlockXCoord, firstBlockYCoord, firstBlockZCoord));
		}
	}

	public void generateSurface(World world, Random rand, int x, int z)
	{
		if(rand.nextInt(100) < CCubesSettings.surfaceGenAmount)
		{
			int xCord = x + rand.nextInt(16);
			int zCord = z + rand.nextInt(16);
			int yCord = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();

			world.setBlockState(new BlockPos(xCord, yCord, zCord), CCubesBlocks.chanceCube.getDefaultState());
		}
	}
}