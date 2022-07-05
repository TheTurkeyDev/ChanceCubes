package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.mcwrapper.BlockWrapper;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class OneIsLuckyReward extends BaseCustomReward
{
	public OneIsLuckyReward()
	{
		super(CCubesCore.MODID + ":one_is_lucky", 0);
	}

	@Override
	public void trigger(final ServerLevel level, final BlockPos pos, Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToNearPlayers(level, pos, 32, "A Lucky Block Salute");

		boolean leftLucky = RewardsUtil.rand.nextBoolean();

		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.get().defaultBlockState(), level, pos.offset(-1, 0, 0)))
		{
			TileChanceCube leftCube = new TileChanceCube(leftLucky ? 100 : -100, pos.offset(-1, 0, 0), CCubesBlocks.CHANCE_CUBE.get().defaultBlockState());
			level.setBlockEntity(leftCube);
		}
		if(RewardsUtil.placeBlock(Blocks.OAK_SIGN.defaultBlockState(), level, pos))
		{
			SignBlockEntity sign = BlockWrapper.createSign(pos, new String[]{"One is lucky", "One is not", "#OGLuckyBlocks"});
			level.setBlockEntity(sign);
		}
		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.get().defaultBlockState(), level, pos.offset(1, 0, 0)))
		{
			TileChanceCube rightCube = new TileChanceCube(!leftLucky ? 100 : -100, pos.offset(1, 0, 0), CCubesBlocks.CHANCE_CUBE.get().defaultBlockState());
			level.setBlockEntity(rightCube);
		}

		Scheduler.scheduleTask(new Task("One_Is_Lucky_Reward", 6000, 10)
		{
			@Override
			public void callback()
			{
				level.setBlockAndUpdate(pos.offset(-1, 0, 0), Blocks.AIR.defaultBlockState());
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				level.setBlockAndUpdate(pos.offset(1, 0, 0), Blocks.AIR.defaultBlockState());
			}

			@Override
			public void update()
			{
				if(level.getBlockState(pos.offset(-1, 0, 0)).isAir() || level.getBlockState(pos.offset(1, 0, 0)).isAir())
				{
					this.callback();
					Scheduler.removeTask(this);
				}
				else if(level.getBlockState(pos).isAir())
				{
					SignBlockEntity sign3 = BlockWrapper.createSign(pos, new String[]{"Middle? Really?", "Fine you don't", "get a cube then!", "(¬_¬)"});
					level.setBlockAndUpdate(pos.offset(0, 0, 0), Blocks.OAK_SIGN.defaultBlockState());
					level.setBlockEntity(sign3);
					level.setBlockAndUpdate(pos.offset(-1, 0, 0), Blocks.AIR.defaultBlockState());
					level.setBlockAndUpdate(pos.offset(1, 0, 0), Blocks.AIR.defaultBlockState());
					Scheduler.removeTask(this);
				}
			}
		});
	}
}