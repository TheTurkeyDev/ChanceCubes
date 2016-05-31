package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.MazeGenerator;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class MazeReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player)
	{
		player.addChatMessage(new TextComponentString("Generating maze..... May be some lag..."));
		final MazeGenerator gen = new MazeGenerator();
		gen.generate(world, pos.getX(), pos.getY(), pos.getZ(), 20, 20);
		final int px = (int) player.posX;
		final int py = (int) player.posY;
		final int pz = (int) player.posZ;
		player.setPositionAndUpdate(pos.getX() - 8.5, pos.getY(), pos.getZ() - 8.5);

		Task task = new Task("Maze_Reward_Update", 20)
		{
			@Override
			public void callback()
			{
				update(0, gen, world, player, new BlockPos(px, py, pz));
			}
		};
		Scheduler.scheduleTask(task);

		player.addChatMessage(new TextComponentString("Beat the maze and find the sign!"));
		player.addChatMessage(new TextComponentString("You have 45 seconds!"));
	}

	@Override
	public int getChanceValue()
	{
		return -25;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Maze";
	}

	public void update(final int iteration, final MazeGenerator gen, final World world, final EntityPlayer player, final BlockPos playerLoc)
	{
		if(iteration == 45)
		{
			gen.endMaze(world);
			player.setPositionAndUpdate(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ());
			player.attackEntityFrom(CCubesDamageSource.mazefail, Float.MAX_VALUE);
			return;
		}
		else if(!world.getBlockState(new BlockPos(gen.endBlockWorldCords.getX(), gen.endBlockWorldCords.getY(), gen.endBlockWorldCords.getZ())).getBlock().equals(Blocks.standing_sign))
		{
			player.addChatMessage(new TextComponentString("Hey! You won!"));
			gen.endMaze(world);
			player.setPositionAndUpdate(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ());
			return;
		}
		else if(iteration == 15)
		{
			player.addChatMessage(new TextComponentString("30 seconds left!!"));
		}
		else if(iteration == 40)
		{
			player.addChatMessage(new TextComponentString("5..."));
		}
		else if(iteration == 41)
		{
			player.addChatMessage(new TextComponentString("4..."));
		}
		else if(iteration == 42)
		{
			player.addChatMessage(new TextComponentString("3..."));
		}
		else if(iteration == 43)
		{
			player.addChatMessage(new TextComponentString("2..."));
		}
		else if(iteration == 44)
		{
			player.addChatMessage(new TextComponentString("1!"));
		}

		Task task = new Task("Maze_Reward_Update", 20)
		{
			@Override
			public void callback()
			{
				update(iteration + 1, gen, world, player, playerLoc);
			}
		};
		Scheduler.scheduleTask(task);
	}
}