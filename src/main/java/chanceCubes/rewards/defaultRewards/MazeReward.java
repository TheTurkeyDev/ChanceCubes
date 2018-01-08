package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.MazeGenerator;
import chanceCubes.util.RewardsUtil;
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

		Scheduler.scheduleTask(new Task("Maze_Reward_Update", 900, 20)
		{
			@Override
			public void callback()
			{
				player.setPositionAndUpdate(px, py, pz);
				gen.endMaze(world);
				if(RewardsUtil.isPlayerOnline(player))
					player.attackEntityFrom(CCubesDamageSource.MAZE_FAIL, Float.MAX_VALUE);
			}

			@Override
			public void update()
			{
				int time = this.delayLeft / 20;

				if(!world.getBlockState(new BlockPos(gen.endBlockWorldCords.getX(), gen.endBlockWorldCords.getY(), gen.endBlockWorldCords.getZ())).getBlock().equals(Blocks.STANDING_SIGN))
				{
					player.addChatMessage(new TextComponentString("Hey! You won!"));
					gen.endMaze(world);
					player.setPositionAndUpdate(px, py, pz);
					Scheduler.removeTask(this);
				}
				else if(time == 30)
				{
					player.addChatMessage(new TextComponentString("30 seconds left!!"));
				}
				else if(time == 5)
				{
					player.addChatMessage(new TextComponentString("5..."));
				}
				else if(time == 4)
				{
					player.addChatMessage(new TextComponentString("4..."));
				}
				else if(time == 3)
				{
					player.addChatMessage(new TextComponentString("3..."));
				}
				else if(time == 2)
				{
					player.addChatMessage(new TextComponentString("2..."));
				}
				else if(time == 1)
				{
					player.addChatMessage(new TextComponentString("1!"));
				}
			}
		});

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
}