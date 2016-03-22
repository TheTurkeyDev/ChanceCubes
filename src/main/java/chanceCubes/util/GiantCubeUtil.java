package chanceCubes.util;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GiantCubeUtil
{
	/** Check that structure is properly formed */
	public static boolean checkMultiBlockForm(BlockPos pos, World world)
	{
		int cx = pos.getX();
		int cy = pos.getY();
		int cz = pos.getZ();
		while(world.getBlockState(new BlockPos(cx, cy - 1, cz)).getBlock().equals(CCubesBlocks.chanceCube))
			cy--;
		while(world.getBlockState(new BlockPos(cx - 1, cy, cz)).getBlock().equals(CCubesBlocks.chanceCube))
			cx--;
		while(world.getBlockState(new BlockPos(cx, cy, cz - 1)).getBlock().equals(CCubesBlocks.chanceCube))
			cz--;
		int i = 0;
		// Scan a 3x3x3 area, starting with the bottom left corner
		for(int x = cx; x < cx + 3; x++)
			for(int y = cy; y < cy + 3; y++)
				for(int z = cz; z < cz + 3; z++)
					if(world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(CCubesBlocks.chanceCube))
						i++;
		// check if there are 27 blocks present (3*3*3)
		return i > 26;
	}

	/** Setup all the blocks in the structure */
	public static void setupStructure(BlockPos pos, World world)
	{
		int cx = pos.getX();
		int cy = pos.getY();
		int cz = pos.getZ();
		while(world.getBlockState(new BlockPos(cx, cy - 1, cz)).getBlock().equals(CCubesBlocks.chanceCube))
			cy--;
		while(world.getBlockState(new BlockPos(cx - 1, cy, cz)).getBlock().equals(CCubesBlocks.chanceCube))
			cx--;
		while(world.getBlockState(new BlockPos(cx, cy, cz - 1)).getBlock().equals(CCubesBlocks.chanceCube))
			cz--;
		int i = 0;
		for(int x = cx; x < cx + 3; x++)
			for(int z = cz; z < cz + 3; z++)
				for(int y = cy; y < cy + 3; y++)
				{
					i++;
					world.setBlockState(new BlockPos(x, y, z), CCubesBlocks.chanceGiantCube.getDefaultState(), i == 27 ? 3 : 2);
					TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
					// Check if block is bottom center block
					boolean master = (x == cx && y == cy + 1 && z == cz);
					if(tile != null && (tile instanceof TileGiantCube))
					{
						((TileGiantCube) tile).setMasterCoords(cx + 1, cy + 1, cz + 1);
						((TileGiantCube) tile).setHasMaster(true);
						((TileGiantCube) tile).setIsMaster(master);
					}
				}
		world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), CCubesCore.MODID + ":giant_Cube_Spawn", 1, 1);
	}

	/** Reset all the parts of the structure */
	public static void resetStructure(BlockPos pos, World world)
	{
		for(int x = pos.getX() - 1; x < pos.getX() + 2; x++)
			for(int y = pos.getY() - 1; y < pos.getY() + 2; y++)
				for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++)
				{
					BlockPos blockPos = new BlockPos(x, y, z);
					TileEntity tile = world.getTileEntity(blockPos);
					if(tile != null && (tile instanceof TileGiantCube))
					{
						((TileGiantCube) tile).reset();
						world.removeTileEntity(blockPos);
						world.setBlockState(blockPos, CCubesBlocks.chanceCube.getDefaultState());
					}
				}
	}

	/** Reset all the parts of the structure */
	public static void removeStructure(BlockPos pos, World world)
	{
		for(int x = pos.getX() - 1; x < pos.getX() + 2; x++)
			for(int y = pos.getY() - 1; y < pos.getY() + 2; y++)
				for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++)
				{
					BlockPos blockPos = new BlockPos(x, y, z);
					TileEntity tile = world.getTileEntity(blockPos);
					if(tile != null && (tile instanceof TileGiantCube))
					{
						((TileGiantCube) tile).reset();
						world.removeTileEntity(blockPos);
						world.setBlockToAir(blockPos);
					}
				}
	}
}