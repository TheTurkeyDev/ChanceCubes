package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TableFlipReward extends BaseCustomReward
{
	public TableFlipReward()
	{
		super(CCubesCore.MODID + ":Table_Flip", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
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
						RewardsUtil.placeBlock(Blocks.OAK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP), world, pos);
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(1, 0, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(-1, 0, 0));
						break;
					}
					case 1:
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos);
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(1, 0, 0));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(-1, 0, 0));

						RewardsUtil.placeBlock(Blocks.OAK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP), world, pos.add(0, 1, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(1, 1, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(-1, 1, 0));
						break;
					}
					case 2:
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 0));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(1, 1, 0));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(-1, 1, 0));

						RewardsUtil.placeBlock(Blocks.OAK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP), world, pos.add(0, 2, 1));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(1, 2, 1));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(-1, 2, 1));
						break;
					}
					case 3:
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 2, 1));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(1, 2, 1));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(-1, 2, 1));

						RewardsUtil.placeBlock(Blocks.OAK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), world, pos.add(0, 1, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(1, 1, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(-1, 1, 2));
						break;
					}
					case 4:
					{
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 2));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(1, 1, 2));
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(-1, 1, 2));

						RewardsUtil.placeBlock(Blocks.OAK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), world, pos.add(0, 0, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(1, 0, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE, StairsShape.STRAIGHT), world, pos.add(-1, 0, 2));
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
}