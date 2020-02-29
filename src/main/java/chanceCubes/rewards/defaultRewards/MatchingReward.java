package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Map;

public class MatchingReward extends BaseCustomReward
{
	private static final Block[] blocks = {Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.LIME_WOOL, Blocks.LIME_WOOL};

	public MatchingReward()
	{
		super(CCubesCore.MODID + ":matching", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		RewardBlockCache cache = new RewardBlockCache(world, pos, player.getPosition());
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
			cache.cacheBlock(new BlockPos(x, -1, z), blocks[i].getDefaultState());
		}
		player.sendMessage(new StringTextComponent("Memerize these blocks!"));

		int delay = super.getSettingAsInt(settings, "mem_duration", 200, 60, 600);

		Scheduler.scheduleTask(new Task("Matching_Reward_Memerize_Delay", delay, 20)
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < blocks.length; i++)
				{
					int x = (i % 3) - 1;
					int z = (i / 3) - 1;
					world.setBlockState(pos.add(x, -1, z), Blocks.GLASS.getDefaultState());
				}
				match(world, pos, player, blocks, cache);
			}

			@Override
			public void update()
			{
				this.showTimeLeft(player, STitlePacket.Type.ACTIONBAR);
			}
		});
	}

	public void match(World world, BlockPos pos, PlayerEntity player, Block[] blocks, RewardBlockCache cache)
	{
		player.sendMessage(new StringTextComponent("Now break the matching blocks (in pairs with white last)! You have 45 seconds!"));
		Scheduler.scheduleTask(new Task("Matching_Reward_Memerize_Delay", 900, 2)
		{
			boolean[] checked = new boolean[9];
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
					this.showTimeLeft(player, STitlePacket.Type.ACTIONBAR);

				for(int i = 0; i < blocks.length; i++)
				{
					int x = (i % 3) - 1;
					int z = (i / 3) - 1;
					if(world.isAirBlock(pos.add(x, -1, z)) && !checked[i])
					{
						checked[i] = true;
						world.setBlockState(pos.add(x, -1, z), blocks[i].getDefaultState());
						if(lastBroken != -1)
						{
							if(blocks[i] == blocks[lastBroken])
							{
								matches++;
								lastBroken = -1;
								break;
							}
							else
							{
								lose();
								Scheduler.removeTask(this);
								break;
							}
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
				player.world.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, Explosion.Mode.NONE);
				player.attackEntityFrom(CCubesDamageSource.MATCHING_FAIL, Float.MAX_VALUE);
				reset();
			}

			private void win()
			{
				player.sendMessage(new StringTextComponent("Good job! Have a cool little item!"));
				player.world.addEntity(new ItemEntity(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
				reset();
			}

			public void reset()
			{
				cache.restoreBlocks(player);
			}
		});
	}
}
