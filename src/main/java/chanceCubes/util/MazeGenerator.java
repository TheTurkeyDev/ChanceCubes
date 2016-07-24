package chanceCubes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class MazeGenerator
{
	private int width;
	private int height;
	private int[][] map;
	private ArrayList<Location2I> walls = new ArrayList<Location2I>();
	private Map<Location3I, CustomEntry<Block, Integer>> blockStorgae = new HashMap<Location3I, CustomEntry<Block, Integer>>();
	private Map<Location3I, NBTTagCompound> tileStorgae = new HashMap<Location3I, NBTTagCompound>();
	private Random r = new Random();

	private int currentX = 1;
	private int currentY = 1;

	private final int nonWall = 0;
	private final int wall = 1;

	private Location2I endBlock;
	public Location3I endBlockWorldCords;

	/**
	 * 
	 * @param multiple
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void generate(World world, int x1, int y1, int z1, int width, int height)
	{
		this.width = width;
		this.height = height;
		map = new int[width][height];
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
				map[x][y] = wall;

		map[1][1] = nonWall;
		currentX = 1;
		currentY = 1;
		Location2I current = new Location2I(currentX, currentY);
		Location2I north = current.add(0, -1);
		Location2I east = current.add(1, 0);
		Location2I south = current.add(0, 1);
		Location2I west = current.add(-1, 0);

		if((north.getY() > 0) && (map[(int) north.getX()][(int) north.getY()] == wall))
		{
			if(map[(int) north.getX()][(int) (north.getY() - 1)] == wall)
				walls.add(north);
		}
		if((east.getX() < width) && (map[(int) east.getX()][(int) east.getY()] == wall))
		{
			if(map[(int) (east.getX() + 1)][(int) east.getY()] == wall)
				walls.add(east);
		}
		if((south.getY() < height) && (map[(int) south.getX()][(int) south.getY()] == wall))
		{
			if(map[(int) south.getX()][(int) (south.getY() + 1)] == wall)
				walls.add(south);
		}
		if((west.getX() > 0) && (map[(int) west.getX()][(int) west.getY()] == wall))
		{
			if(map[(int) (west.getX() - 1)][(int) west.getY()] == wall)
				walls.add(west);
		}

		int randomLoc = 0;
		while(walls.size() > 0)
		{
			randomLoc = r.nextInt(walls.size());
			currentX = (int) ((Location2I) walls.get(randomLoc)).getX();
			currentY = (int) ((Location2I) walls.get(randomLoc)).getY();
			current.setXY(currentX, currentY);
			north = current.add(0, -1);
			east = current.add(1, 0);
			south = current.add(0, 1);
			west = current.add(-1, 0);

			if(!checkwalls(current))
			{
				map[currentX][currentY] = nonWall;
				walls.remove(randomLoc);

				if((north.getY() - 1 > 0) && (map[(int) north.getX()][(int) north.getY()] == wall))
				{
					if(map[(int) north.getX()][(int) (north.getY() - 1)] == wall)
						walls.add(north);
				}
				if((east.getX() + 1 < width) && (map[(int) east.getX()][(int) east.getY()] == wall))
				{
					if(map[(int) (east.getX() + 1)][(int) east.getY()] == wall)
						walls.add(east);
				}
				if((south.getY() + 1 < height) && (map[(int) south.getX()][(int) south.getY()] == wall))
				{
					if(map[(int) south.getX()][(int) (south.getY() + 1)] == wall)
						walls.add(south);
				}
				if((west.getX() - 1 > 0) && (map[(int) west.getX()][(int) west.getY()] == wall))
				{
					if(map[(int) (west.getX() - 1)][(int) west.getY()] == wall)
						walls.add(west);
				}
			}
			else
			{
				walls.remove(randomLoc);
			}
		}
		int endBlockX = width - 1;
		int endBlockZ = height - 1;
		boolean run = true;
		int i = 0;
		int xx = 0;
		int zz = 0;
		while(run)
		{
			for(xx = 0; xx <= i; xx++)
			{
				for(zz = i; zz >= 0; zz--)
				{
					if(this.map[endBlockX - xx][endBlockZ - zz] == this.nonWall && run)
					{
						endBlock = new Location2I(endBlockX - xx, endBlockZ - zz);
						run = false;
					}
				}
			}
			i++;
		}

		placeBlocks(world, x1, y1, z1);
	}

	private boolean checkwalls(Location2I loc)
	{
		Location2I north = loc.add(0, -1);
		Location2I east = loc.add(1, 0);
		Location2I south = loc.add(0, 1);
		Location2I west = loc.add(-1, 0);

		int yes = 0;
		if(north.getY() >= 0 && map[north.getX()][north.getY()] == nonWall)
			yes++;
		if(east.getX() < width && map[east.getX()][east.getY()] == nonWall)
			yes++;
		if(south.getY() < height && map[south.getX()][south.getY()] == nonWall)
			yes++;
		if(west.getX() >= 0 && map[west.getX()][west.getY()] == nonWall)
			yes++;
		return yes > 1;
	}

	private void placeBlocks(World world, int x, int y, int z)
	{
		int xoff = (x - (this.width / 2));
		int zoff = (z - (this.height / 2));

		TileEntity temp;
		NBTTagCompound nbt = new NBTTagCompound();
		for(int xx = 0; xx < this.width; xx++)
		{
			for(int zz = 0; zz < this.height; zz++)
			{
				if(this.map[xx][zz] == 0)
				{
					for(int yy = -1; yy < 3; yy++)
					{
						blockStorgae.put(new Location3I(xoff + xx, y + yy, zoff + zz), new CustomEntry<Block, Integer>(world.getBlock(xoff + xx, y + yy, zoff + zz), world.getBlockMetadata(xoff + xx, y + yy, zoff + zz)));
						temp = world.getTileEntity(xoff + xx, y + yy, zoff + zz);
						if(temp != null)
						{
							temp.writeToNBT(nbt);
							this.tileStorgae.put(new Location3I(xoff + xx, y + yy, zoff + zz), nbt);
						}
					}

					world.setBlock(xoff + xx, y - 1, zoff + zz, Blocks.bedrock);
					world.setBlock(xoff + xx, y, zoff + zz, Blocks.torch);
					world.setBlockMetadataWithNotify(xoff + xx, y, zoff + zz, 0, 3);
					world.setBlockToAir(xoff + xx, y + 1, zoff + zz);
					world.setBlock(xoff + xx, y + 2, zoff + zz, Blocks.bedrock);
				}
				else
				{
					for(int yy = -1; yy < 3; yy++)
					{
						blockStorgae.put(new Location3I(xoff + xx, y + yy, zoff + zz), new CustomEntry<Block, Integer>(world.getBlock(xoff + xx, y + yy, zoff + zz), world.getBlockMetadata(xoff + xx, y + yy, zoff + zz)));
						temp = world.getTileEntity(xoff + xx, y + yy, zoff + zz);
						if(temp != null)
						{
							temp.writeToNBT(nbt);
							this.tileStorgae.put(new Location3I(xoff + xx, y + yy, zoff + zz), nbt);
						}
					}
					world.setBlockToAir(xoff + xx, y - 1, zoff + zz);
					world.setBlock(xoff + xx, y, zoff + zz, Blocks.bedrock);
					world.setBlock(xoff + xx, y + 1, zoff + zz, Blocks.bedrock);
					world.setBlockToAir(xoff + xx, y + 2, zoff + zz);
				}
			}
		}

		endBlockWorldCords = new Location3I(xoff + this.endBlock.getX(), y, zoff + this.endBlock.getY());
		world.setBlock(xoff + this.endBlock.getX(), y, zoff + this.endBlock.getY(), Blocks.standing_sign, 6, 2);
		temp = world.getTileEntity(xoff + this.endBlock.getX(), y, zoff + this.endBlock.getY());
		if(temp instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign) temp;
			sign.signText[0] = "Break me";
			sign.signText[1] = "To beat the";
			sign.signText[2] = "Maze";
		}
	}

	public void endMaze(World world)
	{
		for(Location3I loc : this.blockStorgae.keySet())
			world.setBlock(loc.getX(), loc.getY(), loc.getZ(), this.blockStorgae.get(loc).getKey(), this.blockStorgae.get(loc).getValue(), 2);

		for(Location3I loc : this.tileStorgae.keySet())
			world.setTileEntity(loc.getX(), loc.getY(), loc.getZ(), TileEntity.createAndLoadEntity(this.tileStorgae.get(loc)));
	}
}