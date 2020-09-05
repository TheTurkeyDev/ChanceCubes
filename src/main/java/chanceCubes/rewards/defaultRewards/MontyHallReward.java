package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StoneButtonBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class MontyHallReward extends BaseCustomReward
{
	public MontyHallReward()
	{
		super(CCubesCore.MODID + ":monty_hall", 0);
	}

	@Override
	public void trigger(final ServerWorld world, final BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		RewardsUtil.sendMessageToPlayer(player, "Which button do you press?");

		RewardBlockCache cache = new RewardBlockCache(world, pos, player.getPosition());
		cache.cacheBlock(new BlockPos(-1, 0, 0), Blocks.OBSIDIAN.getDefaultState());
		cache.cacheBlock(new BlockPos(0, 0, 0), Blocks.OBSIDIAN.getDefaultState());
		cache.cacheBlock(new BlockPos(1, 0, 0), Blocks.OBSIDIAN.getDefaultState());
		cache.cacheBlock(new BlockPos(-1, 0, 1), Blocks.STONE_BUTTON.getDefaultState().with(StoneButtonBlock.HORIZONTAL_FACING, Direction.SOUTH));
		cache.cacheBlock(new BlockPos(0, 0, 1), Blocks.STONE_BUTTON.getDefaultState().with(StoneButtonBlock.HORIZONTAL_FACING, Direction.SOUTH));
		cache.cacheBlock(new BlockPos(1, 0, 1), Blocks.STONE_BUTTON.getDefaultState().with(StoneButtonBlock.HORIZONTAL_FACING, Direction.SOUTH));

		Scheduler.scheduleTask(new Task("Monty_Hall_Reward", 6000, 10)
		{
			int[] chance = {RewardsUtil.rand.nextInt(3) - 1, RewardsUtil.rand.nextInt(3) - 1, RewardsUtil.rand.nextInt(3) - 1};

			@Override
			public void callback()
			{
				cache.restoreBlocks(player);
			}

			@Override
			public void update()
			{
				BlockState state = world.getBlockState(pos.add(-1, 0, 1));
				if(state.getProperties().contains(StoneButtonBlock.POWERED) && state.get(StoneButtonBlock.POWERED))
					giveReward(chance[0]);

				state = world.getBlockState(pos.add(0, 0, 1));
				if(state.getProperties().contains(StoneButtonBlock.POWERED) && state.get(StoneButtonBlock.POWERED))
					giveReward(chance[1]);

				state = world.getBlockState(pos.add(1, 0, 1));
				if(state.getProperties().contains(StoneButtonBlock.POWERED) && state.get(StoneButtonBlock.POWERED))
					giveReward(chance[2]);
			}

			private void giveReward(int value)
			{
				if(value == -1)
				{
					TNTEntity entitytntprimed = new TNTEntity(world, player.getPosX(), player.getPosY() + 1D, player.getPosZ(), player);
					world.addEntity(entitytntprimed);
					world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					entitytntprimed.setFuse(40);
				}
				else if(value == 0)
				{
					RewardsUtil.sendMessageToPlayer(player, "You walk away to live another day...");
				}
				else if(value == 1)
				{
					player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}

				this.callback();
				Scheduler.removeTask(this);
			}
		});
	}
}