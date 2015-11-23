package chanceCubes.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileGiantCube;

public class GiantCubeUtil
{
	/** Check that structure is properly formed */
	public static boolean checkMultiBlockForm(int xCoord, int yCoord, int zCoord, World world)
	{
		int cx = xCoord;
		int cy = yCoord;
		int cz = zCoord;
		while(world.getBlock(cx, cy - 1, cz).equals(CCubesBlocks.chanceCube))
			cy--;
		while(world.getBlock(cx - 1, cy, cz).equals(CCubesBlocks.chanceCube))
			cx--;
		while(world.getBlock(cx, cy, cz - 1).equals(CCubesBlocks.chanceCube))
			cz--;
		int i = 0;
		// Scan a 3x3x3 area, starting with the bottom left corner
		for(int x = cx; x < cx + 3; x++)
			for(int y = cy; y < cy + 3; y++)
				for(int z = cz; z < cz + 3; z++)
					if(world.getBlock(x, y, z).equals(CCubesBlocks.chanceCube))
						i++;
		// check if there are 27 blocks present (3*3*3)
		return i > 26;
	}

	/** Setup all the blocks in the structure */
	public static void setupStructure(int xCoord, int yCoord, int zCoord, World world)
	{
		int cx = xCoord;
		int cy = yCoord;
		int cz = zCoord;
		while(world.getBlock(cx, cy - 1, cz).equals(CCubesBlocks.chanceCube))
			cy--;
		while(world.getBlock(cx - 1, cy, cz).equals(CCubesBlocks.chanceCube))
			cx--;
		while(world.getBlock(cx, cy, cz - 1).equals(CCubesBlocks.chanceCube))
			cz--;
		for(int x = cx; x < cx + 3; x++)
			for(int y = cy; y < cy + 3; y++)
				for(int z = cz; z < cz + 3; z++)
				{
					world.setBlock(x, y, z, CCubesBlocks.chanceGiantCube);
					TileEntity tile = world.getTileEntity(x, y, z);
					// Check if block is bottom center block
					boolean master = (x == cx + 1 && y == cy + 1 && z == cz + 1);
					if(tile != null && (tile instanceof TileGiantCube))
					{
						((TileGiantCube) tile).setMasterCoords(cx + 1, cy + 1, cz + 1);
						((TileGiantCube) tile).setHasMaster(true);
						((TileGiantCube) tile).setIsMaster(master);
					}
				}
	}

	/** Reset all the parts of the structure */
	public static void resetStructure(int xCoord, int yCoord, int zCoord, World world)
	{
		for(int x = xCoord - 1; x < xCoord + 2; x++)
			for(int y = yCoord - 1 ; y < yCoord + 2; y++)
				for(int z = zCoord - 1; z < zCoord + 2; z++)
				{
					TileEntity tile = world.getTileEntity(x, y, z);
					if(tile != null && (tile instanceof TileGiantCube))
					{
						((TileGiantCube) tile).reset();
						world.removeTileEntity(x, y, z);
						world.setBlock(x, y, z, CCubesBlocks.chanceCube);
					}
				}
	}
	
	/** Reset all the parts of the structure */
	public static void removeStructure(int xCoord, int yCoord, int zCoord, World world)
	{
		for(int x = xCoord - 1; x < xCoord + 2; x++)
			for(int y = yCoord - 1; y < yCoord + 2; y++)
				for(int z = zCoord - 1; z < zCoord + 2; z++)
				{
					TileEntity tile = world.getTileEntity(x, y, z);
					if(tile != null && (tile instanceof TileGiantCube))
					{
						((TileGiantCube)tile).reset();
						world.removeTileEntity(x, y, z);
						world.setBlockToAir(x, y, z);
					}
				}
	}
}
