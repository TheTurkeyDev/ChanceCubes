package chanceCubes.util;

import net.minecraft.block.Blocks;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator
{
	private int width;
	private int height;
	private int[][] map;
	private ArrayList<Location2I> walls = new ArrayList<>();
	private RewardBlockCache cache;
	private Random r = new Random();

	private final int nonWall = 0;

	private BlockPos startPos;
	private Location2I endBlock;
	public BlockPos endBlockWorldCords;

	public MazeGenerator(World world, BlockPos pos, BlockPos playerPos)
	{
		cache = new RewardBlockCache(world, pos, playerPos);
		startPos = pos;
	}

	public void generate(World world, int width, int height)
	{
		this.width = width;
		this.height = height;
		map = new int[width][height];
		int wall = 1;
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
				map[x][y] = wall;

		map[1][1] = nonWall;

		walls.add(new Location2I(1, 1));
		Location2I current = new Location2I(0, 0);
		Location2I north;
		Location2I east;
		Location2I south;
		Location2I west;

		do
		{
			int randomLoc = r.nextInt(walls.size());
			int currentX = walls.get(randomLoc).getX();
			int currentY = walls.get(randomLoc).getY();
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
		} while(walls.size() > 0);

		int endBlockX = width - 1;
		int endBlockZ = height - 1;
		boolean run = true;
		int i = 0;
		int xx;
		int zz;
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

		placeBlocks(world);
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

	private void placeBlocks(World world)
	{
		int xoff = -(this.width / 2);
		int zoff = -(this.height / 2);

		for(int xx = 0; xx < this.width; xx++)
		{
			for(int zz = 0; zz < this.height; zz++)
			{
				if(this.map[xx][zz] == 0)
				{
					cache.cacheBlock(new BlockPos(xoff + xx, -1, zoff + zz), Blocks.BEDROCK.getDefaultState());
					cache.cacheBlock(new BlockPos(xoff + xx, 0, zoff + zz), Blocks.TORCH.getDefaultState());
					cache.cacheBlock(new BlockPos(xoff + xx, 1, zoff + zz), Blocks.AIR.getDefaultState());
					cache.cacheBlock(new BlockPos(xoff + xx, 2, zoff + zz), Blocks.BEDROCK.getDefaultState());
				}
				else
				{
					cache.cacheBlock(new BlockPos(xoff + xx, -1, zoff + zz), Blocks.AIR.getDefaultState());
					cache.cacheBlock(new BlockPos(xoff + xx, 0, zoff + zz), Blocks.BEDROCK.getDefaultState());
					cache.cacheBlock(new BlockPos(xoff + xx, 1, zoff + zz), Blocks.BEDROCK.getDefaultState());
					cache.cacheBlock(new BlockPos(xoff + xx, 2, zoff + zz), Blocks.AIR.getDefaultState());
				}
			}
		}

		endBlockWorldCords = new BlockPos(startPos.getX() + xoff + this.endBlock.getX(), startPos.getY(), startPos.getZ() + zoff + this.endBlock.getY());
		cache.cacheBlock(new BlockPos(xoff + this.endBlock.getX(), 0, zoff + this.endBlock.getY()), Blocks.OAK_SIGN.getDefaultState().with(StandingSignBlock.ROTATION, 7));
		TileEntity te = world.getTileEntity(new BlockPos(startPos.getX() + xoff + this.endBlock.getX(), startPos.getY(), startPos.getZ() + zoff + this.endBlock.getY()));
		if(te instanceof SignTileEntity)
		{
			SignTileEntity sign = (SignTileEntity) te;
			sign.signText[0] = new StringTextComponent("Break me");
			sign.signText[1] = new StringTextComponent("To beat the");
			sign.signText[2] = new StringTextComponent("Maze");
		}
	}

	public void endMaze(PlayerEntity player)
	{
		cache.restoreBlocks(player);
	}
}