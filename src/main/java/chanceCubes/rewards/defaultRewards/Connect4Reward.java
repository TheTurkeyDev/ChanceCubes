package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.GameTurn;
import chanceCubes.util.Point;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Connect4Reward extends BaseCustomReward
{
	private static final int WIDTH = 7;
	private static final int HEIGHT = 6;

	public Connect4Reward()
	{
		super(CCubesCore.MODID + ":connect_4", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		int mistakeChance = super.getSettingAsInt(settings, "mistakeChance", 3, 0, 100);
		RewardsUtil.sendMessageToPlayer(player, "Lets play Connect 4!");
		RewardsUtil.sendMessageToPlayer(player, "Beat the Computer to get 5 Diamonds!");

		RewardBlockCache cache = new RewardBlockCache(level, pos, player.getOnPos());

		BlockPos playerPos = pos.offset(3, 0, 0);
		player.moveTo(playerPos.getX(), playerPos.getY(), playerPos.getZ(), 0, 0);
		player.level().addFreshEntity(new ItemEntity(player.level(), playerPos.getX(), playerPos.getY(), playerPos.getZ(), new ItemStack(Blocks.RED_CONCRETE_POWDER, 21)));

		for(int x = -5; x < 6; x++)
			for(int z = -6; z < 7; z++)
				for(int y = 0; y < 9; y++)
					cache.cacheBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());

		for(int x = -3; x < 4; x++)
			for(int z = -4; z < 5; z++)
				if(z == -4 || z == 4 || x == 0)
					level.setBlockAndUpdate(pos.offset(x, 0, z), Blocks.BEDROCK.defaultBlockState());

		for(int y = 0; y < 8; y++)
		{
			for(int x = -1; x < 2; x++)
			{
				level.setBlockAndUpdate(pos.offset(x, y, -4), Blocks.BEDROCK.defaultBlockState());
				level.setBlockAndUpdate(pos.offset(x, y, 4), Blocks.BEDROCK.defaultBlockState());
			}
		}

		for(int x = -1; x < 2; x++)
			for(int z = -4; z < 5; z++)
				if(z == -4 || z == 4 || x == -1 || x == 1)
					level.setBlockAndUpdate(pos.offset(x, 7, z), Blocks.BEDROCK.defaultBlockState());

		for(int y = 1; y < 7; y++)
		{
			for(int z = -3; z < 4; z++)
			{
				level.setBlockAndUpdate(pos.offset(-1, y, z), Blocks.GLASS.defaultBlockState());
				level.setBlockAndUpdate(pos.offset(1, y, z), Blocks.GLASS.defaultBlockState());
			}
		}

		for(int y = 0; y < 8; y++)
		{
			level.setBlockAndUpdate(pos.offset(0, y, -5), Blocks.LADDER.defaultBlockState());
			level.setBlockAndUpdate(pos.offset(0, y, 5), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH));
		}

		level.setBlockAndUpdate(pos.offset(-2, 1, -4), Blocks.BEDROCK.defaultBlockState());
		level.setBlockAndUpdate(pos.offset(2, 1, -4), Blocks.BEDROCK.defaultBlockState());
		level.setBlockAndUpdate(pos.offset(-2, 1, 4), Blocks.BEDROCK.defaultBlockState());
		level.setBlockAndUpdate(pos.offset(2, 1, 4), Blocks.BEDROCK.defaultBlockState());

		Scheduler.scheduleTask(new Task("Connect_4_Game", 10000, 5)
		{
			final Board board = new Board();

			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				for(int z = -3; z < 4; z++)
				{
					for(int y = 1; y < 7; y++)
					{
						if(board.board[((y - 1) * WIDTH) + (z + 3)] != 0 || (y - 2 >= 0 && board.board[((y - 2) * WIDTH) + (z + 3)] == 0))
							continue;
						BlockPos pos2 = pos.offset(0, y, z);
						if(!level.getBlockState(pos2).isAir() && level.getBlockState(pos2).getBlock().equals(Blocks.RED_CONCRETE_POWDER))
							makeMove(z + 3, y - 1);
					}
				}
			}

			private void makeMove(int x, int y)
			{
				board.placeMove(x, y, GameTurn.PLAYER);
				board.updateWorldView(level, pos);

				boolean playerWon = board.hasWon(x, y, GameTurn.PLAYER);

				if(!board.isTie() && !playerWon)
				{
					//Make CPU Move
					board.minimax(0, GameTurn.CPU, null, mistakeChance);
					board.placeMove(board.computersMove.x, board.computersMove.y, GameTurn.CPU);
					level.setBlockAndUpdate(pos.offset(0, 8, board.computersMove.x - 3), Blocks.YELLOW_CONCRETE_POWDER.defaultBlockState());
				}

				boolean gameOver = false;
				if(playerWon)
				{
					RewardsUtil.sendMessageToPlayer(player, "You Won!");
					player.level().addFreshEntity(new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), new ItemStack(Items.DIAMOND, 5)));
					gameOver = true;
				}
				else if(board.hasWon(board.computersMove.x, board.computersMove.y, GameTurn.CPU))
				{
					RewardsUtil.sendMessageToPlayer(player, "The Computer won! Better luck next time!");
					gameOver = true;
				}
				else if(board.isTie())
				{
					RewardsUtil.sendMessageToPlayer(player, "You tied! Better luck next time!");
					gameOver = true;
				}

				if(gameOver)
				{
					this.delayLeft = 0;
					Scheduler.scheduleTask(new Task("Connect_4_Game_End_Delay", 40)
					{
						@Override
						public void callback()
						{
							cache.restoreBlocks(player);
						}
					});
				}

			}
		});
	}

	private static class Board
	{
		public final int[] board = new int[HEIGHT * WIDTH];
		public Point computersMove;

		public boolean isTie()
		{
			return getAvailableStates().isEmpty();
		}

		public void placeMove(int x, int y, GameTurn turn)
		{
			board[(WIDTH * y) + x] = turn == GameTurn.PLAYER ? 2 : 1;
		}

		private boolean hasWon(int placedX, int placedY, GameTurn playerTurn)
		{
			//Horizontal Check
			int turn = playerTurn == GameTurn.PLAYER ? 2 : 1;
			int y = placedY * WIDTH;
			for(int i = Math.max(0, placedX - 3); i <= placedX; i++)
			{
				int adj = i + y;
				if(adj >= 0 && adj < WIDTH * HEIGHT && i + 3 < WIDTH)
				{
					if(board[adj] == turn && board[adj + 1] == turn && board[adj + 2] == turn && board[adj + 3] == turn)
						return true;
				}
			}

			//Verticle Check
			for(int i = Math.max(0, placedY - 3); i <= placedY; i++)
			{
				int adj = placedX + (i * WIDTH);
				if(adj >= 0 && adj < WIDTH * HEIGHT && i + 3 < HEIGHT)
				{
					if(board[adj] == turn && board[adj + WIDTH] == turn && board[adj + (2 * WIDTH)] == turn && board[adj + (3 * WIDTH)] == turn)
						return true;
				}
			}

			//Ascending Diag
			for(int i = -3; i <= 0; i++)
			{
				int adjX = placedX + i;
				int adjY = placedY + i;
				int adj = adjX + (adjY * WIDTH);
				if(adj >= 0 && adj < WIDTH * HEIGHT && adjX + 3 < WIDTH && adjY + 3 < HEIGHT)
				{
					if(board[adj] == turn && board[adj + WIDTH + 1] == turn && board[adj + (2 * WIDTH) + 2] == turn && board[adj + (3 * WIDTH) + 3] == turn)
						return true;
				}
			}

			//Descending Diag
			for(int i = -3; i <= 0; i++)
			{
				int adjX = placedX + i;
				int adjY = placedY - i;
				int adj = adjX + (adjY * WIDTH);
				if(adj >= 0 && adj < WIDTH * HEIGHT && adjX + 3 < WIDTH && adjY - 3 >= 0)
				{
					if(board[adj] == turn && board[adj - WIDTH + 1] == turn && board[adj - (2 * WIDTH) + 2] == turn && board[adj - (3 * WIDTH) + 3] == turn)
						return true;
				}
			}

			return false;
		}

		public List<Point> getAvailableStates()
		{
			List<Point> availablePoints = new ArrayList<>();
			for(int j = 0; j < 6; ++j)
				for(int i = 0; i < 7; ++i)
					if(board[(j * WIDTH) + i] == 0 && (j == 0 || board[((j - 1) * WIDTH) + i] != 0))
						availablePoints.add(new Point(i, j));
			return availablePoints;
		}

		public int minimax(int depth, GameTurn turn, Point lastPlaced, int mistakeChance)
		{
			if(lastPlaced != null)
			{
				if(turn == GameTurn.PLAYER && hasWon(lastPlaced.x, lastPlaced.y, GameTurn.CPU))
					return 1 - depth;
				if(turn == GameTurn.CPU && hasWon(lastPlaced.x, lastPlaced.y, GameTurn.PLAYER))
					return -1;
			}
			if(depth > 7)
				return 0;

			List<Point> pointsAvailable = getAvailableStates();
			if(pointsAvailable.isEmpty())
				return 0;

			if(depth == 0 && RewardsUtil.rand.nextInt(100) < mistakeChance)
			{
				this.computersMove = pointsAvailable.get(RewardsUtil.rand.nextInt(pointsAvailable.size()));
				return 0;
			}

			int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
			for(int i = 0; i < pointsAvailable.size(); ++i)
			{
				Point point = pointsAvailable.get(i);
				placeMove(point.x, point.y, turn);

				int currentScore = minimax(depth + 1, turn == GameTurn.CPU ? GameTurn.PLAYER : GameTurn.CPU, point, mistakeChance);
				if(turn == GameTurn.CPU)
				{
					max = Math.max(currentScore, max);

					if(currentScore >= 0 && depth == 0)
						computersMove = point;

					if(currentScore == 1)
					{
						board[(point.y * WIDTH) + point.x] = 0;
						break;
					}

					if(i == pointsAvailable.size() - 1 && max < 0 && depth == 0)
						computersMove = point;
				}
				else if(turn == GameTurn.PLAYER)
				{
					min = Math.min(currentScore, min);
					if(min == -1)
					{
						board[(point.y * WIDTH) + point.x] = 0;
						break;
					}
				}
				board[(point.y * WIDTH) + point.x] = 0;
			}
			return turn == GameTurn.CPU ? max : min;
		}

		public void updateWorldView(Level level, BlockPos pos)
		{
			for(int z = -3; z < 4; z++)
				for(int y = 1; y < 7; y++)
					level.setBlockAndUpdate(pos.offset(0, y, z), getStateForBoardVal(board[((y - 1) * WIDTH) + (z + 3)]));
		}

		public BlockState getStateForBoardVal(int val)
		{
			return switch(val)
					{
						case 1 -> Blocks.YELLOW_CONCRETE_POWDER.defaultBlockState();
						case 2 -> Blocks.RED_CONCRETE_POWDER.defaultBlockState();
						default -> Blocks.AIR.defaultBlockState();
					};
		}
	}
}
