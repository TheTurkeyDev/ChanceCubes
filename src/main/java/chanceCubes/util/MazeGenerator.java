package chanceCubes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class MazeGenerator
{
	private int width;
	private int height;
	private int[][] map;
	private ArrayList<Location2I> walls = new ArrayList<Location2I>();
	private Map<BlockPos, IBlockState> blockStorgae = new HashMap<BlockPos, IBlockState>();
	private Map<BlockPos, NBTTagCompound> tileStorgae = new HashMap<BlockPos, NBTTagCompound>();
	private Random r = new Random();

	private int currentX = 1;
	private int currentY = 1;

	private final int nonWall = 0;
	private final int wall = 1;

	private Location2I endBlock;
	public BlockPos endBlockWorldCords;

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
		
		walls.add(new Location2I(1, 1));
		Location2I current = new Location2I(0, 0);
		Location2I north = new Location2I(0, 0);
		Location2I east = new Location2I(0, 0);
		Location2I south = new Location2I(0, 0);
		Location2I west = new Location2I(0, 0);
		
		do {
			int randomLoc = r.nextInt(walls.size());
			currentX = walls.get(randomLoc).getX();
			currentY = walls.get(randomLoc).getY();
			current.setXY(currentX, currentY);
			north = current.add(0, -1);
			east = current.add(1, 0);
			south = current.add(0, 1);
			west = current.add(-1, 0);

			if(!checkwalls(current))
			{
				map[currentX][currentY] = nonWall;
				walls.remove(randomLoc);

				if((north.getY() > 0) && (map[north.getX()][north.getY()] == wall))
					if(map[north.getX()][north.getY() - 1] == wall && !walls.contains(north))
						walls.add(north);
					
				if((east.getX() + 1 < width) && (map[east.getX()][east.getY()] == wall))
					if(map[east.getX() + 1][east.getY()] == wall && !walls.contains(east))
						walls.add(east);
				
				if((south.getY() + 1 < height) && (map[south.getX()][south.getY()] == wall))
					if(map[south.getX()][south.getY() + 1] == wall && !walls.contains(south))
						walls.add(south);
				
				if((west.getX() > 0) && (map[west.getX()][west.getY()] == wall))
					if(map[west.getX() - 1][west.getY()] == wall && !walls.contains(west))
						walls.add(west);
			}
			else
			{
				walls.remove(randomLoc);
			}
		}while(walls.size() > 0);
		

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

		placeBlocks(world, new BlockPos(x1, y1, z1));
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

	private void placeBlocks(World world, BlockPos pos)
	{
		int xoff = (pos.getX() - (this.width / 2));
		int zoff = (pos.getZ() - (this.height / 2));

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
						blockStorgae.put(new BlockPos(xoff + xx, pos.getY() + yy, zoff + zz), world.getBlockState(new BlockPos(xoff + xx, pos.getY() + yy, zoff + zz)));
						temp = world.getTileEntity(new BlockPos(xoff + xx, pos.getY() + yy, zoff + zz));
						if(temp != null)
						{
							temp.write(nbt);
							this.tileStorgae.put(new BlockPos(xoff + xx, pos.getY() + yy, zoff + zz), nbt);
						}
					}

					world.setBlockState(new BlockPos(xoff + xx, pos.getY() - 1, zoff + zz), Blocks.BEDROCK.getDefaultState());
					world.setBlockState(new BlockPos(xoff + xx, pos.getY(), zoff + zz), Blocks.TORCH.getDefaultState());
					world.setBlockState(new BlockPos(xoff + xx, pos.getY() + 1, zoff + zz), Blocks.AIR.getDefaultState());
					world.setBlockState(new BlockPos(xoff + xx, pos.getY() + 2, zoff + zz), Blocks.BEDROCK.getDefaultState());
				}
				else
				{
					for(int yy = -1; yy < 3; yy++)
					{
						BlockPos tempPos = new BlockPos(xoff + xx, pos.getY() + yy, zoff + zz);
						blockStorgae.put(tempPos, world.getBlockState(tempPos));
						temp = world.getTileEntity(tempPos);
						if(temp != null)
						{
							temp.write(nbt);
							this.tileStorgae.put(new BlockPos(xoff + xx, pos.getY() + yy, zoff + zz), nbt);
						}
					}
					world.setBlockState(new BlockPos(xoff + xx, pos.getY() - 1, zoff + zz), Blocks.AIR.getDefaultState());
					world.setBlockState(new BlockPos(xoff + xx, pos.getY(), zoff + zz), Blocks.BEDROCK.getDefaultState());
					world.setBlockState(new BlockPos(xoff + xx, pos.getY() + 1, zoff + zz), Blocks.BEDROCK.getDefaultState());
					world.setBlockState(new BlockPos(xoff + xx, pos.getY() + 2, zoff + zz), Blocks.AIR.getDefaultState());
				}
			}
		}

		endBlockWorldCords = new BlockPos(xoff + this.endBlock.getX(), pos.getY(), zoff + this.endBlock.getY());
		world.setBlockState(new BlockPos(xoff + this.endBlock.getX(), pos.getY(), zoff + this.endBlock.getY()), Blocks.SIGN.getDefaultState());
		temp = world.getTileEntity(new BlockPos(xoff + this.endBlock.getX(), pos.getY(), zoff + this.endBlock.getY()));
		if(temp instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign) temp;
			sign.signText[0] = new TextComponentString("Break me");
			sign.signText[1] = new TextComponentString("To beat the");
			sign.signText[2] = new TextComponentString("Maze");
		}
	}

	public void endMaze(World world)
	{
		for(BlockPos loc : this.blockStorgae.keySet())
			world.setBlockState(loc, this.blockStorgae.get(loc), 2);

		for(BlockPos loc : this.tileStorgae.keySet())
			world.setTileEntity(loc, TileEntity.create(this.tileStorgae.get(loc)));
	}
}