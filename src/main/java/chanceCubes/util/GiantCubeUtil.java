package chanceCubes.util;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GiantCubeUtil
{
	/**
	 * Check that structure is properly formed
	 *
	 * @param pos   to start the check at
	 * @param level world
	 * @param build if the giant cube should be built if the structure is valid
	 * @return if there is a valid 3x3x3 configuration
	 */
	public static boolean checkMultiBlockForm(BlockPos pos, Level level, boolean build)
	{
		if(CCubesSettings.disableGiantCC.get())
			return false;
		BlockPos bottomLeft = findBottomCorner(pos, level);
		int cx = bottomLeft.getX();
		int cy = bottomLeft.getY();
		int cz = bottomLeft.getZ();
		int i = 0;
		// Scan a 3x3x3 area, starting with the bottom left corner
		for(int x = cx; x < cx + 3; x++)
			for(int y = cy; y < cy + 3; y++)
				for(int z = cz; z < cz + 3; z++)
					if(level.getBlockState(new BlockPos(x, y, z)).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
						i++;
		// check if there are 27 blocks present (3*3*3) and if a giant cube should be built
		if(build)
		{
			if(i > 26)
			{
				setupStructure(new BlockPos(cx, cy, cz), level, true);
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
	public static void setupStructure(BlockPos pos, Level level, boolean areCoordsCorrect)
	{
		if(CCubesSettings.disableGiantCC.get())
			return;
		int cx = pos.getX();
		int cy = pos.getY();
		int cz = pos.getZ();

		if(!areCoordsCorrect)
		{
			BlockPos bottomLeft = findBottomCorner(pos, level);
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

					RewardsUtil.placeBlock(CCubesBlocks.GIANT_CUBE.defaultBlockState(), level, new BlockPos(x, y, z), i == 27 ? 3 : 2, level.getBlockState(new BlockPos(x, y, z)).getBlock().equals(CCubesBlocks.CHANCE_CUBE));


					BlockEntity tile = level.getBlockEntity(new BlockPos(x, y, z));
					// Check if block is bottom center block
					boolean master = (x == cx + 1 && y == cy + 1 && z == cz + 1);
					if(tile instanceof TileGiantCube)
					{
						((TileGiantCube) tile).setMasterCoords(cx + 1, cy + 1, cz + 1);
						((TileGiantCube) tile).setHasMaster(true);
						((TileGiantCube) tile).setIsMaster(master);
					}
				}
			}
		}
		level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), CCubesSounds.GIANT_CUBE_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	public static BlockPos findBottomCorner(BlockPos pos, Level level)
	{
		int cx = pos.getX();
		int cy = pos.getY();
		int cz = pos.getZ();
		while(level.getBlockState(pos.offset(0, -1, 0)).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
		{
			pos = pos.offset(0, -1, 0);
			cy--;
		}
		while(level.getBlockState(pos.offset(-1, 0, 0)).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
		{
			pos = pos.offset(-1, 0, 0);
			cx--;
		}
		while(level.getBlockState(pos.offset(0, 0, -1)).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
		{
			pos = pos.offset(0, 0, -1);
			cz--;
		}

		return new BlockPos(cx, cy, cz);
	}

	/**
	 * Reset all the parts of the structure
	 */
	public static void resetStructure(BlockPos pos, Level level)
	{
		for(int x = pos.getX() - 1; x < pos.getX() + 2; x++)
			for(int y = pos.getY() - 1; y < pos.getY() + 2; y++)
				for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++)
				{
					BlockPos blockPos = new BlockPos(x, y, z);
					BlockEntity tile = level.getBlockEntity(blockPos);
					if(tile instanceof TileGiantCube)
					{
						((TileGiantCube) tile).reset();
						level.removeBlockEntity(blockPos);
						level.setBlockAndUpdate(blockPos, CCubesBlocks.CHANCE_CUBE.defaultBlockState());
					}
				}
	}

	/**
	 * Reset all the parts of the structure
	 */
	public static void removeStructure(BlockPos pos, Level level)
	{
		for(int x = pos.getX() - 1; x < pos.getX() + 2; x++)
			for(int y = pos.getY() - 1; y < pos.getY() + 2; y++)
				for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++)
				{
					BlockPos blockPos = new BlockPos(x, y, z);
					BlockEntity tile = level.getBlockEntity(blockPos);
					if(tile instanceof TileGiantCube)
					{
						((TileGiantCube) tile).reset();
						level.removeBlockEntity(blockPos);
						level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
					}
				}
	}
}