package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class MatchingReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		int[] metas = { 0, 1, 1, 2, 2, 3, 3, 4, 4 };
		IBlockState[] savedBlocks = new IBlockState[9];
		for(int i = 0; i < 500; i++)
		{
			int index1 = RewardsUtil.rand.nextInt(9);
			int index2 = RewardsUtil.rand.nextInt(9);
			int metaTemp = metas[index1];
			metas[index1] = metas[index2];
			metas[index2] = metaTemp;
		}

		for(int i = 0; i < metas.length; i++)
		{
			int x = (i % 3) - 1;
			int z = (i / 3) - 1;
			savedBlocks[i] = world.getBlockState(pos.add(x, -1, z));
			world.setBlockState(pos.add(x, -1, z), RewardsUtil.getBlockStateFromBlockMeta(Blocks.WOOL, metas[i]));
		}
		player.sendMessage(new TextComponentString("Memerize these blocks!"));

		Scheduler.scheduleTask(new Task("Matching_Reward_Memerize_Delay", 140, 20)
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < metas.length; i++)
				{
					int x = (i % 3) - 1;
					int z = (i / 3) - 1;
					world.setBlockState(pos.add(x, -1, z), Blocks.GLASS.getDefaultState());
				}
				match(world, pos, player, metas, savedBlocks);
			}

			@Override
			public void update()
			{
				int time = this.delayLeft / 20;
				TextComponentString message = new TextComponentString(String.valueOf(time));
				message.getStyle().setBold(true).setColor(TextFormatting.RED);
				RewardsUtil.setPlayerTitle(player, new SPacketTitle(Type.ACTIONBAR, message, 0, 20, 0));
			}
		});
	}

	public void match(World world, BlockPos pos, EntityPlayer player, int[] metas, IBlockState[] savedBlocks)
	{
		player.sendMessage(new TextComponentString("Now break the matching blocks (in pairs)! You have 45 seconds!"));
		Scheduler.scheduleTask(new Task("Matching_Reward_Memerize_Delay", 900, 10)
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
				{
					int time = this.delayLeft / 20;
					TextComponentString message = new TextComponentString(String.valueOf(time));
					message.getStyle().setBold(true).setColor(TextFormatting.RED);
					RewardsUtil.setPlayerTitle(player, new SPacketTitle(Type.ACTIONBAR, message, 0, 20, 0));
				}

				for(int i = 0; i < metas.length; i++)
				{
					int x = (i % 3) - 1;
					int z = (i / 3) - 1;
					if(world.isAirBlock(pos.add(x, -1, z)) && !checked[i])
					{
						checked[i] = true;
						if(lastBroken != -1)
						{
							if(metas[i] == metas[lastBroken])
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
				player.world.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, false);
				player.attackEntityFrom(CCubesDamageSource.MATCHING_FAIL, Float.MAX_VALUE);
				reset();
			}

			private void win()
			{
				player.sendMessage(new TextComponentString("Good job! Have a cool little item!"));
				player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
				reset();
			}

			public void reset()
			{
				for(int i = 0; i < savedBlocks.length; i++)
				{
					int x = (i % 3) - 1;
					int z = (i / 3) - 1;
					world.setBlockState(pos.add(x, -1, z), savedBlocks[i]);
				}
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Matching";
	}

}
