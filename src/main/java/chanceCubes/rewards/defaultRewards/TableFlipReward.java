package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class TableFlipReward extends BaseCustomReward
{
	public TableFlipReward()
	{
		super(CCubesCore.MODID + ":table_flip", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToAllPlayers(level, "(╯°□°）╯︵ ┻━┻)");

		Scheduler.scheduleTask(new Task("Table_Flip", 1000, 10)
		{
			int stage = 0;

			@Override
			public void update()
			{
				switch(stage)
				{
					case 0 -> {
						RewardsUtil.placeBlock(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), level, pos);
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.TOP).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(1, 0, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.TOP).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(-1, 0, 0));
					}
					case 1 -> {
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos);
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(1, 0, 0));
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(-1, 0, 0));

						RewardsUtil.placeBlock(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), level, pos.offset(0, 1, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.TOP).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(1, 1, 0));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.TOP).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(-1, 1, 0));
					}
					case 2 -> {
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(0, 1, 0));
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(1, 1, 0));
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(-1, 1, 0));

						RewardsUtil.placeBlock(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP), level, pos.offset(0, 2, 1));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.TOP).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(1, 2, 1));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.TOP).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(-1, 2, 1));
					}
					case 3 -> {
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(0, 2, 1));
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(1, 2, 1));
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(-1, 2, 1));

						RewardsUtil.placeBlock(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM), level, pos.offset(0, 1, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(1, 1, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(-1, 1, 2));
					}
					case 4 -> {
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(0, 1, 2));
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(1, 1, 2));
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(-1, 1, 2));

						RewardsUtil.placeBlock(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM), level, pos.offset(0, 0, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(1, 0, 2));
						RewardsUtil.placeBlock(Blocks.OAK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), level, pos.offset(-1, 0, 2));
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