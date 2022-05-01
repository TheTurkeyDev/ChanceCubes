package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.GuiTextLocation;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MatchingReward extends BaseCustomReward
{
	private static final Block[] blocks = {Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.LIME_WOOL, Blocks.LIME_WOOL};

	public MatchingReward()
	{
		super(CCubesCore.MODID + ":matching", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		RewardBlockCache cache = new RewardBlockCache(level, pos, player.getOnPos());
		for(int i = 0; i < 500; i++)
		{
			int index1 = RewardsUtil.rand.nextInt(9);
			int index2 = RewardsUtil.rand.nextInt(9);
			Block temp = blocks[index1];
			blocks[index1] = blocks[index2];
			blocks[index2] = temp;
		}

		for(int i = 0; i < blocks.length; i++)
		{
			int x = (i % 3) - 1;
			int z = (i / 3) - 1;
			cache.cacheBlock(new BlockPos(x, -1, z), blocks[i].defaultBlockState());
		}
		RewardsUtil.sendMessageToPlayer(player, "Memorize these blocks!");

		int delay = super.getSettingAsInt(settings, "memDuration", 200, 60, 600);

		Scheduler.scheduleTask(new Task("Matching_Reward_Memorize_Delay", delay, 20)
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < blocks.length; i++)
				{
					int x = (i % 3) - 1;
					int z = (i / 3) - 1;
					level.setBlockAndUpdate(pos.offset(x, -1, z), Blocks.GLASS.defaultBlockState());
				}
				match(level, pos, player, blocks, cache);
			}

			@Override
			public void update()
			{
				this.showTimeLeft(player, GuiTextLocation.ACTION_BAR);
			}
		});
	}

	public void match(ServerLevel level, BlockPos pos, Player player, Block[] blocks, RewardBlockCache cache)
	{
		RewardsUtil.sendMessageToPlayer(player, "Now break the matching blocks (in pairs with white last)! You have 45 seconds!");
		Scheduler.scheduleTask(new Task("Matching_Reward_Memerize_Delay", 900, 2)
		{
			final boolean[] checked = new boolean[9];
			int lastBroken = -1;
			int matches = 0;

			@Override
			public void callback()
			{
				lose();
			}

			@Override
			public void update()
			{
				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, GuiTextLocation.ACTION_BAR);

				for(int i = 0; i < blocks.length; i++)
				{
					int x = (i % 3) - 1;
					int z = (i / 3) - 1;
					if(level.getBlockState(pos.offset(x, -1, z)).isAir() && !checked[i])
					{
						checked[i] = true;
						level.setBlockAndUpdate(pos.offset(x, -1, z), blocks[i].defaultBlockState());
						if(lastBroken != -1)
						{
							if(blocks[i] == blocks[lastBroken])
							{
								matches++;
								lastBroken = -1;
							}
							else
							{
								lose();
								Scheduler.removeTask(this);
							}
							break;
						}
						else
						{
							lastBroken = i;
						}

						if(matches == 4)
						{
							win();
							Scheduler.removeTask(this);
							break;
						}
					}
				}
			}

			private void lose()
			{
				player.level.explode(player, player.getX(), player.getY(), player.getZ(), 1.0F, Explosion.BlockInteraction.NONE);
				player.hurt(CCubesDamageSource.MATCHING_FAIL, Float.MAX_VALUE);
				reset();
			}

			private void win()
			{
				RewardsUtil.sendMessageToPlayer(player, "Good job! Have a cool little item!");
				player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				reset();
			}

			public void reset()
			{
				cache.restoreBlocks(player);
			}
		});
	}
}
