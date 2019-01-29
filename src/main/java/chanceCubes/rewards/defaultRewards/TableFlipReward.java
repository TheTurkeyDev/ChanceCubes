package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TableFlipReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		RewardsUtil.sendMessageToAllPlayers(world, "(╯°□°）╯︵ ┻━┻)");

		Scheduler.scheduleTask(new Task("Table_Flip", 1000, 10)
		{
			int stage = 0;

			@Override
			public void update()
			{
				switch(stage)
				{
					case 0:
					{
						RewardsUtil.placeBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), world, pos);
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(1, 0, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(-1, 0, 0));
						break;
					}
					case 1:
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos);
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(1, 0, 0));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(-1, 0, 0));

						RewardsUtil.placeBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), world, pos.add(0, 1, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(1, 1, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(-1, 1, 0));
						break;
					}
					case 2:
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 0));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(1, 1, 0));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(-1, 1, 0));

						RewardsUtil.placeBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), world, pos.add(0, 2, 1));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(1, 2, 1));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(-1, 2, 1));
						break;
					}
					case 3:
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 2, 1));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(1, 2, 1));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(-1, 2, 1));

						RewardsUtil.placeBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM), world, pos.add(0, 1, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(1, 1, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(-1, 1, 2));
						break;
					}
					case 4:
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 2));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(1, 1, 2));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(-1, 1, 2));

						RewardsUtil.placeBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM), world, pos.add(0, 0, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(1, 0, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), world, pos.add(-1, 0, 2));
						break;
					}
				}

				if(stage < 4)
					stage++;
				else
					Scheduler.removeTask(this);
			}

			@Override
			public void callback()
			{
				
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
		return CCubesCore.MODID + ":Table_Flip";
	}
}