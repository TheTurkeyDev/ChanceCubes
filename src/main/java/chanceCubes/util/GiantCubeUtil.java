package chanceCubes.util;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GiantCubeUtil
{
	/**
	 * Check that structure is properly formed
	 *
	 * @param pos   to start the check at
	 * @param world world
	 * @param build if the giant cube should be built if the structure is valid
	 * @return if there is a valid 3x3x3 configuration
	 */
	public static boolean checkMultiBlockForm(BlockPos pos, World world, boolean build)
	{
		BlockPos bottomLeft = findBottomCorner(pos, world);
		int cx = bottomLeft.getX();
		int cy = bottomLeft.getY();
		int cz = bottomLeft.getZ();
		int i = 0;
		// Scan a 3x3x3 area, starting with the bottom left corner
		for(int x = cx; x < cx + 3; x++)
			for(int y = cy; y < cy + 3; y++)
				for(int z = cz; z < cz + 3; z++)
					if(world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
						i++;
		// check if there are 27 blocks present (3*3*3) and if a giant cube should be built
		if(build)
		{
			if(i > 26)
			{
				setupStructure(new BlockPos(cx, cy, cz), world, true);
				return true;
			}
			return false;
		}
		else
		{
			return i > 26;
		}
	}

	/**
	 * Setup all the blocks in the structure
	 */
	public static void setupStructure(BlockPos pos, World world, boolean areCoordsCorrect)
	{
		int cx = pos.getX();
		int cy = pos.getY();
		int cz = pos.getZ();

		if(!areCoordsCorrect)
		{
			BlockPos bottomLeft = findBottomCorner(pos, world);
			cx = bottomLeft.getX();
			cy = bottomLeft.getY();
			cz = bottomLeft.getZ();
		}

		int i = 0;
		for(int x = cx; x < cx + 3; x++)
		{
			for(int z = cz; z < cz + 3; z++)
			{
				for(int y = cy; y < cy + 3; y++)
				{
					i++;

					RewardsUtil.placeBlock(CCubesBlocks.GIANT_CUBE.getDefaultState(), world, new BlockPos(x, y, z), i == 27 ? 3 : 2, world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(CCubesBlocks.CHANCE_CUBE));


					TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
					// Check if block is bottom center block
					boolean master = (x == cx && y == cy + 1 && z == cz);
					if(tile instanceof TileGiantCube)
					{
						((TileGiantCube) tile).setMasterCoords(cx + 1, cy + 1, cz + 1);
						((TileGiantCube) tile).setHasMaster(true);
						((TileGiantCube) tile).setIsMaster(master);
					}
				}
			}
		}
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), CCubesSounds.GIANT_CUBE_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	public static BlockPos findBottomCorner(BlockPos pos, World world)
	{
		int cx = pos.getX();
		int cy = pos.getY();
		int cz = pos.getZ();
		while(world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
		{
			pos = pos.add(0, -1, 0);
			cy--;
		}
		while(world.getBlockState(pos.add(-1, 0, 0)).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
		{
			pos = pos.add(-1, 0, 0);
			cx--;
		}
		while(world.getBlockState(pos.add(0, 0, -1)).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
		{
			pos = pos.add(0, 0, -1);
			cz--;
		}

		return new BlockPos(cx, cy, cz);
	}

	/**
	 * Reset all the parts of the structure
	 */
	public static void resetStructure(BlockPos pos, World world)
	{
		for(int x = pos.getX() - 1; x < pos.getX() + 2; x++)
			for(int y = pos.getY() - 1; y < pos.getY() + 2; y++)
				for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++)
				{
					BlockPos blockPos = new BlockPos(x, y, z);
					TileEntity tile = world.getTileEntity(blockPos);
					if(tile instanceof TileGiantCube)
					{
						((TileGiantCube) tile).reset();
						world.removeTileEntity(blockPos);
						world.setBlockState(blockPos, CCubesBlocks.CHANCE_CUBE.getDefaultState());
					}
				}
	}

	/**
	 * Reset all the parts of the structure
	 */
	public static void removeStructure(BlockPos pos, World world)
	{
		for(int x = pos.getX() - 1; x < pos.getX() + 2; x++)
			for(int y = pos.getY() - 1; y < pos.getY() + 2; y++)
				for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++)
				{
					BlockPos blockPos = new BlockPos(x, y, z);
					TileEntity tile = world.getTileEntity(blockPos);
					if(tile instanceof TileGiantCube)
					{
						((TileGiantCube) tile).reset();
						world.removeTileEntity(blockPos);
						world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
					}
				}
	}
}