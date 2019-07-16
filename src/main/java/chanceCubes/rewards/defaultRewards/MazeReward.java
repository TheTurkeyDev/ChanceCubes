package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.MazeGenerator;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.STitlePacket.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class MazeReward extends BaseCustomReward
{
	public MazeReward()
	{
		super(CCubesCore.MODID + ":Maze", -25);
	}

	@Override
	public void trigger(final World world, final BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		player.sendMessage(new StringTextComponent("Generating maze..... May be some lag..."));
		final MazeGenerator gen = new MazeGenerator(world, pos, player.getPosition());
		gen.generate(world, 20, 20);
		BlockPos initialPos = new BlockPos(pos.getX() - 8, pos.getY(), pos.getZ() - 8);
		player.setPositionAndUpdate(initialPos.getX() - 0.5, initialPos.getY(), initialPos.getZ() - 0.5);

		Scheduler.scheduleTask(new Task("Maze_Reward_Update", 900, 20)
		{
			@Override
			public void callback()
			{
				gen.endMaze(world, player);
				if(RewardsUtil.isPlayerOnline(player))
					player.attackEntityFrom(CCubesDamageSource.MAZE_FAIL, Float.MAX_VALUE);
			}

			@Override
			public void update()
			{
				if(initialPos.withinDistance(player.getPosition(), 2))
				{
					this.delayLeft++;
					return;
				}

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, Type.ACTIONBAR);

				if(!world.getBlockState(new BlockPos(gen.endBlockWorldCords.getX(), gen.endBlockWorldCords.getY(), gen.endBlockWorldCords.getZ())).getBlock().equals(Blocks.OAK_SIGN))
				{
					gen.endMaze(world, player);
					player.sendMessage(new StringTextComponent("Hey! You won!"));
					player.sendMessage(new StringTextComponent("Here, have a item!"));
					player.world.addEntity(new ItemEntity(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}
			}
		});

		player.sendMessage(new StringTextComponent("Beat the maze and find the sign!"));
		player.sendMessage(new StringTextComponent("You have 45 seconds!"));
	}
}