package chanceCubes.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chanceCubes.blocks.BlockGiantCube;
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
					world.setBlockMetadataWithNotify(x, y, z, x + 3 * (y + 3 * z), 3);
					TileEntity tile = world.getTileEntity(x, y, z);
					// Check if block is bottom center block
					boolean master = (x == cx + 1 && y == cy + 1 && z == cz + 1);
					if(tile != null && (tile instanceof TileGiantCube))
					{
						((TileGiantCube) tile).setMasterCoords(cx + 1, cy + 1, cz + 1);
						((TileGiantCube) tile).setHasMaster(true);
						((TileGiantCube) tile).setIsMaster(master);
					}
					
					/*BlockGiantCube giantCube = (BlockGiantCube) world.getBlock(x, y, z);
					
					if(z == cz)
						giantCube.setSideIcon(3, GiantCubeUtil.getTextureForSide(2, x - cx, y - cy));
					else if(z == cz + 2)
						giantCube.setSideIcon(2, GiantCubeUtil.getTextureForSide(5, x - cx, y - cy));
					
					if(y == cy)
						giantCube.setSideIcon(0, GiantCubeUtil.getTextureForSide(1, x - cx, z - cz));
					else if(y == cy + 2)
						giantCube.setSideIcon(1, GiantCubeUtil.getTextureForSide(6, x - cx, z - cz));
					
					if(x == cx)
						giantCube.setSideIcon(4, GiantCubeUtil.getTextureForSide(4, z - cz, y - cy));
					else if(x == cx + 2)
						giantCube.setSideIcon(5, GiantCubeUtil.getTextureForSide(3, z - cz, y - cy));*/
				}
	}
	
	private static int getTextureForSide(int side, int x, int y)
	{
		System.out.println(side + " " + x + " " + y);
		if(x == 0)
		{
			if(y == 0)
			{
				if(side == 1)
					return 2;
				return 1;
			}
			else if(y == 1)
			{
				return 3;
			}
			else if(y == 2)
			{
				if(side < 4)
					return 4;
				return 5;
			}
		}
		else if(x == 1)
		{
			if(y == 0)
			{
				if(side == 6)
					return 6;
				return 7;
			}
			else if(y == 1)
			{
				if(side % 2 == 1)
					return 0;
				return 8;
			}
			else if(y == 2)
			{
				if(side == 6)
					return 9;
				return 10;
			}
		}
		else if(x == 2)
		{
			if(y == 0)
			{
				if(side > 3)
					return 11;
				return 12;
			}
			else if(y == 1)
			{
				return 13;
			}
			else if(y == 2)
			{
				if(side != 1)
					return 14;
				return 15;
			}
		}
		return 0;
	}

	/** Reset all the parts of the structure */
	public static void resetStructure(int xCoord, int yCoord, int zCoord, World world)
	{
		for(int x = xCoord - 1; x < xCoord + 2; x++)
			for(int y = yCoord - 1; y < yCoord + 2; y++)
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
						((TileGiantCube) tile).reset();
						world.removeTileEntity(x, y, z);
						world.setBlockToAir(x, y, z);
					}
				}
	}
}
