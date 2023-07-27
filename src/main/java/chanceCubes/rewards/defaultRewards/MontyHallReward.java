package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MontyHallReward extends BaseCustomReward
{
	public MontyHallReward()
	{
		super(CCubesCore.MODID + ":monty_hall", 0);
	}

	@Override
	public void trigger(final ServerLevel level, final BlockPos pos, Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToPlayer(player, "Which button do you press?");

		RewardBlockCache cache = new RewardBlockCache(level, pos, player.getOnPos());
		cache.cacheBlock(new BlockPos(-1, 0, 0), Blocks.OBSIDIAN.defaultBlockState());
		cache.cacheBlock(new BlockPos(0, 0, 0), Blocks.OBSIDIAN.defaultBlockState());
		cache.cacheBlock(new BlockPos(1, 0, 0), Blocks.OBSIDIAN.defaultBlockState());
		cache.cacheBlock(new BlockPos(-1, 0, 1), Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.SOUTH));
		cache.cacheBlock(new BlockPos(0, 0, 1), Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.SOUTH));
		cache.cacheBlock(new BlockPos(1, 0, 1), Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.SOUTH));

		Scheduler.scheduleTask(new Task("Monty_Hall_Reward", 6000, 10)
		{
			final int[] chance = {RewardsUtil.rand.nextInt(3) - 1, RewardsUtil.rand.nextInt(3) - 1, RewardsUtil.rand.nextInt(3) - 1};

			@Override
			public void callback()
			{
				cache.restoreBlocks(player);
			}

			@Override
			public void update()
			{
				BlockState state = level.getBlockState(pos.offset(-1, 0, 1));
				if(state.getProperties().contains(ButtonBlock.POWERED) && state.getValue(ButtonBlock.POWERED))
					giveReward(chance[0]);

				state = level.getBlockState(pos.offset(0, 0, 1));
				if(state.getProperties().contains(ButtonBlock.POWERED) && state.getValue(ButtonBlock.POWERED))
					giveReward(chance[1]);

				state = level.getBlockState(pos.offset(1, 0, 1));
				if(state.getProperties().contains(ButtonBlock.POWERED) && state.getValue(ButtonBlock.POWERED))
					giveReward(chance[2]);
			}

			private void giveReward(int value)
			{
				if(value == -1)
				{
					PrimedTnt entitytntprimed = new PrimedTnt(level, player.getX(), player.getY() + 1D, player.getZ(), player);
					level.addFreshEntity(entitytntprimed);
					level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
					entitytntprimed.setFuse(40);
				}
				else if(value == 0)
				{
					RewardsUtil.sendMessageToPlayer(player, "You walk away to live another day...");
				}
				else if(value == 1)
				{
					player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}

				this.callback();
				Scheduler.removeTask(this);
			}
		});
	}
}