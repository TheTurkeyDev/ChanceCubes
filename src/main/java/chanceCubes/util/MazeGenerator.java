package chanceCubes.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.ArrayList;

public class MazeGenerator
{
	private int width;
	private int height;
	private int[][] map;
	private final ArrayList<Location2I> walls = new ArrayList<>();
	private final RewardBlockCache cache;

	private final int nonWall = 0;

	private final BlockPos startPos;
	private Location2I endBlock;
	public BlockPos endBlockWorldCords;

	public MazeGenerator(Level level, BlockPos pos, BlockPos playerPos)
	{
		cache = new RewardBlockCache(level, pos, playerPos);
		startPos = pos;
	}

	public void generate(Level level, int width, int height)
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
			int randomLoc = RewardsUtil.rand.nextInt(walls.size());
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

		placeBlocks(level);
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

	private void placeBlocks(Level level)
	{
		int xoff = -(this.width / 2);
		int zoff = -(this.height / 2);

		for(int xx = 0; xx < this.width; xx++)
		{
			for(int zz = 0; zz < this.height; zz++)
			{
				if(this.map[xx][zz] == 0)
				{
					cache.cacheBlock(new BlockPos(xoff + xx, -1, zoff + zz), Blocks.BEDROCK.defaultBlockState());
					cache.cacheBlock(new BlockPos(xoff + xx, 0, zoff + zz), Blocks.TORCH.defaultBlockState());
					cache.cacheBlock(new BlockPos(xoff + xx, 1, zoff + zz), Blocks.AIR.defaultBlockState());
					cache.cacheBlock(new BlockPos(xoff + xx, 2, zoff + zz), Blocks.BEDROCK.defaultBlockState());
				}
				else
				{
					cache.cacheBlock(new BlockPos(xoff + xx, -1, zoff + zz), Blocks.AIR.defaultBlockState());
					cache.cacheBlock(new BlockPos(xoff + xx, 0, zoff + zz), Blocks.BEDROCK.defaultBlockState());
					cache.cacheBlock(new BlockPos(xoff + xx, 1, zoff + zz), Blocks.BEDROCK.defaultBlockState());
					cache.cacheBlock(new BlockPos(xoff + xx, 2, zoff + zz), Blocks.AIR.defaultBlockState());
				}
			}
		}

		endBlockWorldCords = new BlockPos(startPos.getX() + xoff + this.endBlock.getX(), startPos.getY(), startPos.getZ() + zoff + this.endBlock.getY());
		cache.cacheBlock(new BlockPos(xoff + this.endBlock.getX(), 0, zoff + this.endBlock.getY()), Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION, 7));
		BlockEntity te = level.getBlockEntity(new BlockPos(startPos.getX() + xoff + this.endBlock.getX(), startPos.getY(), startPos.getZ() + zoff + this.endBlock.getY()));
		if(te instanceof SignBlockEntity sign)
		{
			sign.setMessage(0, new TextComponent("Break me"));
			sign.setMessage(1, new TextComponent("To beat the"));
			sign.setMessage(2, new TextComponent("Maze"));
		}
	}

	public void endMaze(Player player)
	{
		cache.restoreBlocks(player);
	}
}