package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.Location3I;
import chanceCubes.util.MazeGenerator;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class MazeReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, int x, int y, int z, final EntityPlayer player)
	{
		player.addChatMessage(new ChatComponentText("Generation maze..... May be some lag..."));
		final MazeGenerator gen = new MazeGenerator();
		gen.generate(world, x, y, z, 20, 20);
		final int px = (int) player.posX;
		final int py = (int) player.posY;
		final int pz = (int) player.posZ;
		player.setPositionAndUpdate(x - 8.5, y, z - 8.5);
		
		Task task = new Task("Maze_Reward_Update", 20)
		{
			@Override
			public void callback()
			{
				update(0, gen, world, player, new Location3I(px, py, pz));
			}
		};
		Scheduler.scheduleTask(task);
		
		player.addChatMessage(new ChatComponentText("Beat the maze! You have 1 minute!"));
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
	
	public void update(final int iteration, final MazeGenerator gen, final World world, final EntityPlayer player, final Location3I playerLoc)
	{
		if(iteration == 60)
		{
			gen.endMaze(world);
			player.setPositionAndUpdate(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ());
			player.attackEntityFrom(CCubesDamageSource.mazefail, Float.MAX_VALUE);
			return;
		}
		else if(!world.getBlock(gen.endBlockWorldCords.getX(), gen.endBlockWorldCords.getY(), gen.endBlockWorldCords.getZ()).equals(Blocks.standing_sign))
		{
			player.addChatMessage(new ChatComponentText("Hey! You won!"));
			gen.endMaze(world);
			player.setPositionAndUpdate(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ());
			return;
		}
		else if(iteration == 30)
		{
			player.addChatMessage(new ChatComponentText("30 seconds left!!"));
		}
		else if(iteration == 55)
		{
			player.addChatMessage(new ChatComponentText("5..."));
		}
		else if(iteration == 56)
		{
			player.addChatMessage(new ChatComponentText("4..."));
		}
		else if(iteration == 57)
		{
			player.addChatMessage(new ChatComponentText("3..."));
		}
		else if(iteration == 58)
		{
			player.addChatMessage(new ChatComponentText("2..."));
		}
		else if(iteration == 59)
		{
			player.addChatMessage(new ChatComponentText("1!"));
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