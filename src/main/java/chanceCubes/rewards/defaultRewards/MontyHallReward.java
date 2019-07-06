package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class MontyHallReward extends BaseCustomReward
{
	public MontyHallReward()
	{
		super(CCubesCore.MODID + ":monty_hall", 0);
	}

	@Override
	public void trigger(final World world, final BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		player.sendMessage(new TextComponentString("Which button do you press?"));

		RewardBlockCache cache = new RewardBlockCache(world, pos, player.getPosition());
		cache.cacheBlock(new BlockPos(-1, 0, 0), Blocks.OBSIDIAN.getDefaultState());
		cache.cacheBlock(new BlockPos(0, 0, 0), Blocks.OBSIDIAN.getDefaultState());
		cache.cacheBlock(new BlockPos(1, 0, 0), Blocks.OBSIDIAN.getDefaultState());
		cache.cacheBlock(new BlockPos(-1, 0, 1), Blocks.STONE_BUTTON.getDefaultState().withProperty(BlockButtonStone.FACING, EnumFacing.SOUTH));
		cache.cacheBlock(new BlockPos(0, 0, 1), Blocks.STONE_BUTTON.getDefaultState().withProperty(BlockButtonStone.FACING, EnumFacing.SOUTH));
		cache.cacheBlock(new BlockPos(1, 0, 1), Blocks.STONE_BUTTON.getDefaultState().withProperty(BlockButtonStone.FACING, EnumFacing.SOUTH));

		Scheduler.scheduleTask(new Task("Monty_Hall_Reward", 6000, 10)
		{
			int[] chance = { RewardsUtil.rand.nextInt(3) - 1, RewardsUtil.rand.nextInt(3) - 1, RewardsUtil.rand.nextInt(3) - 1 };

			@Override
			public void callback()
			{
				cache.restoreBlocks(player);
			}

			@Override
			public void update()
			{
				IBlockState state = world.getBlockState(pos.add(-1, 0, 1));
				if(state.getPropertyKeys().contains(BlockButton.POWERED) && state.getValue(BlockButton.POWERED).booleanValue())
					giveReward(chance[0]);

				state = world.getBlockState(pos.add(0, 0, 1));
				if(state.getPropertyKeys().contains(BlockButton.POWERED) && state.getValue(BlockButton.POWERED).booleanValue())
					giveReward(chance[1]);

				state = world.getBlockState(pos.add(1, 0, 1));
				if(state.getPropertyKeys().contains(BlockButton.POWERED) && state.getValue(BlockButton.POWERED).booleanValue())
					giveReward(chance[2]);
			}

			private void giveReward(int value)
			{
				if(value == -1)
				{
					EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, player.posX, player.posY + 1D, player.posZ, player);
					world.spawnEntity(entitytntprimed);
					world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					entitytntprimed.setFuse(40);
				}
				else if(value == 0)
				{
					player.sendMessage(new TextComponentString("You walk away to live another day..."));
				}
				else if(value == 1)
				{
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}

				this.callback();
				Scheduler.removeTask(this);
			}
		});
	}
}