package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
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
		SignBlockEntity sign = new SignBlockEntity();
		sign.setMessage(0, new TextComponent("One is lucky"));
		sign.setMessage(1, new TextComponent("One is not"));
		sign.setMessage(3, new TextComponent("#OGLuckyBlocks"));
		boolean leftLucky = RewardsUtil.rand.nextBoolean();
		TileChanceCube leftCube = new TileChanceCube(leftLucky ? 100 : -100);
		TileChanceCube rightCube = new TileChanceCube(!leftLucky ? 100 : -100);

		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.defaultBlockState(), level, pos.offset(-1, 0, 0)))
			level.setBlockEntity(pos.offset(-1, 0, 0), leftCube);
		if(RewardsUtil.placeBlock(Blocks.OAK_SIGN.defaultBlockState(), level, pos))
			level.setBlockEntity(pos, sign);
		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.defaultBlockState(), level, pos.offset(1, 0, 0)))
			level.setBlockEntity(pos.offset(1, 0, 0), rightCube);

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
				if(level.isAirBlock(pos.offset(-1, 0, 0)) || level.isAirBlock(pos.offset(1, 0, 0)))
				{
					this.callback();
					Scheduler.removeTask(this);
				}
				else if(level.isAirBlock(pos.offset(0, 0, 0)))
				{
					SignBlockEntity sign3 = new SignBlockEntity();
					sign3.setMessage(0, new TextComponent("Middle? Really?"));
					sign3.setMessage(1, new TextComponent("Fine you don't"));
					sign3.setMessage(2, new TextComponent("get a cube then!"));
					sign3.setMessage(3, new TextComponent("(¬_¬)"));
					level.setBlockAndUpdate(pos.offset(0, 0, 0), Blocks.OAK_SIGN.defaultBlockState());
					level.setBlockEntity(pos.offset(0, 0, 0), sign3);
					level.setBlockAndUpdate(pos.offset(-1, 0, 0), Blocks.AIR.defaultBlockState());
					level.setBlockAndUpdate(pos.offset(1, 0, 0), Blocks.AIR.defaultBlockState());
					Scheduler.removeTask(this);
				}
			}
		});
	}
}