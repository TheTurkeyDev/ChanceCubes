package chanceCubes.listeners;

import java.util.Random;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldGen
{
	@SubscribeEvent
	public void onGenerate(PopulateChunkEvent.Pre event)
	{
		if(CCubesSettings.isBlockedWorld(event.getWorld().getWorldInfo().getWorldName()) || CCubesSettings.isBlockedWorld("" + event.getWorld().provider.getDimension()))
			return;
		if(CCubesSettings.oreGeneration)
			generateOre(event.getWorld(), new Random(), event.getChunkX() * 16, event.getChunkZ() * 16);

		if(CCubesSettings.surfaceGeneration)
			generateSurface(event.getWorld(), new Random(), event.getChunkX() * 16, event.getChunkZ() * 16);
	}

	private void generateOre(World world, Random rand, int x, int z)
	{
		for(int k = 0; k < CCubesSettings.oreGenAmount; k++)
			(new WorldGenMinable(CCubesBlocks.CHANCE_CUBE.getDefaultState(), 3)).generate(world, rand, new BlockPos(x + rand.nextInt(16), rand.nextInt(100), z + rand.nextInt(16)));
	}

	public void generateSurface(World world, Random rand, int x, int z)
	{
		if(rand.nextInt(CCubesSettings.surfaceGenAmount + 1) == 1)
		{
			int yCord = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();

			BlockPos pos = new BlockPos(x + rand.nextInt(16), yCord - 1, z + rand.nextInt(16));

			if(world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK))
			{
				for(int y = 0; y < yCord; y++)
				{
					BlockPos pos2 = new BlockPos(pos.getX(), y, pos.getZ());
					if(world.getBlockState(pos).isSideSolid(world, pos2, EnumFacing.UP) && world.isAirBlock(pos2))
					{
						yCord = y;
						return;
					}
				}
			}
			world.setBlockState(new BlockPos(pos.getX(), yCord, pos.getZ()), CCubesBlocks.CHANCE_CUBE.getDefaultState());
		}
	}
}