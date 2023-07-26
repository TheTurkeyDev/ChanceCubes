package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageTypes;
import chanceCubes.util.GuiTextLocation;
import chanceCubes.util.MazeGenerator;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class MazeReward extends BaseCustomReward
{
	public MazeReward()
	{
		super(CCubesCore.MODID + ":maze", -25);
	}

	@Override
	public void trigger(final ServerLevel world, final BlockPos pos, final Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToPlayer(player, "Generating maze..... May be some lag...");
		final MazeGenerator gen = new MazeGenerator(world, pos, player.getOnPos());
		gen.generate(world, 20, 20);
		BlockPos initialPos = new BlockPos(pos.getX() - 8, pos.getY(), pos.getZ() - 8);
		player.moveTo(initialPos.getX() - 0.5, initialPos.getY(), initialPos.getZ() - 0.5);

		int duration = super.getSettingAsInt(settings, "time", 900, 600, 4800);

		Scheduler.scheduleTask(new Task("Maze_Reward_Update", duration, 20)
		{
			@Override
			public void callback()
			{
				gen.endMaze(player);
				if(RewardsUtil.isPlayerOnline(player))
					player.hurt(player.damageSources().source(CCubesDamageTypes.MAZE_FAIL), Float.MAX_VALUE);
			}

			@Override
			public void update()
			{
				if(initialPos.distToLowCornerSqr(player.getX(), player.getY(), player.getZ()) < 4)
				{
					this.delayLeft++;
					return;
				}

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, GuiTextLocation.ACTION_BAR);

				if(!world.getBlockState(new BlockPos(gen.endBlockWorldCords.getX(), gen.endBlockWorldCords.getY(), gen.endBlockWorldCords.getZ())).getBlock().equals(Blocks.OAK_SIGN))
				{
					gen.endMaze(player);
					RewardsUtil.sendMessageToPlayer(player, "Hey! You won!");
					RewardsUtil.sendMessageToPlayer(player, "Here, have a item!");
					player.level().addFreshEntity(new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}
			}
		});

		RewardsUtil.sendMessageToPlayer(player, "Beat the maze and find the sign!");
		RewardsUtil.sendMessageToPlayer(player, "You have 45 seconds!");
	}
}