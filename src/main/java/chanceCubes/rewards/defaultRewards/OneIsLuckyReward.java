package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Map;

public class OneIsLuckyReward extends BaseCustomReward
{
	public OneIsLuckyReward()
	{
		super(CCubesCore.MODID + ":one_is_lucky", 0);
	}

	@Override
	public void trigger(final World world, final BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "A Lucky Block Salute");
		SignTileEntity sign = new SignTileEntity();
		sign.signText[0] = new StringTextComponent("One is lucky");
		sign.signText[1] = new StringTextComponent("One is not");
		sign.signText[3] = new StringTextComponent("#OGLuckyBlocks");
		boolean leftLucky = RewardsUtil.rand.nextBoolean();
		TileChanceCube leftCube = new TileChanceCube(leftLucky ? 100 : -100);
		TileChanceCube rightCube = new TileChanceCube(!leftLucky ? 100 : -100);

		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(-1, 0, 0)))
			world.setTileEntity(pos.add(-1, 0, 0), leftCube);
		if(RewardsUtil.placeBlock(Blocks.OAK_SIGN.getDefaultState(), world, pos))
			world.setTileEntity(pos, sign);
		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(1, 0, 0)))
			world.setTileEntity(pos.add(1, 0, 0), rightCube);

		Scheduler.scheduleTask(new Task("One_Is_Lucky_Reward", 6000, 10)
		{
			@Override
			public void callback()
			{
				world.setBlockState(pos.add(-1, 0, 0), Blocks.AIR.getDefaultState());
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.setBlockState(pos.add(1, 0, 0), Blocks.AIR.getDefaultState());
			}

			@Override
			public void update()
			{
				if(world.isAirBlock(pos.add(-1, 0, 0)) || world.isAirBlock(pos.add(1, 0, 0)))
				{
					this.callback();
					Scheduler.removeTask(this);
				}
			}
		});
	}
}