package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class OneIsLuckyReward implements IChanceCubeReward
{
	private Random random = new Random();

	@Override
	public void trigger(final World world, final BlockPos pos, EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "A Lucky Block Salute");
		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = new TextComponentString("One is lucky");
		sign.signText[1] = new TextComponentString("One is not");
		sign.signText[3] = new TextComponentString("#OGLuckyBlocks");
		boolean leftLucky = random.nextBoolean();
		TileChanceCube leftCube = new TileChanceCube(leftLucky ? 100 : -100);
		TileChanceCube rightCube = new TileChanceCube(!leftLucky ? 100 : -100);

		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(-1, 0, 0)))
			world.setTileEntity(pos.add(-1, 0, 0), leftCube);
		if(RewardsUtil.placeBlock(Blocks.STANDING_SIGN.getDefaultState(), world, pos))
			world.setTileEntity(pos, sign);
		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(1, 0, 0)))
			world.setTileEntity(pos.add(1, 0, 0), rightCube);

		Task task = new Task("One_Is_Lucky_Reward", 10)
		{
			@Override
			public void callback()
			{
				update(0, world, pos);
			}
		};
		Scheduler.scheduleTask(task);
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":One_Is_Lucky";
	}

	public void update(final int iteration, final World world, final BlockPos pos)
	{
		boolean flag = false;

		if(world.isAirBlock(pos.add(-1, 0, 0)) || world.isAirBlock(pos.add(1, 0, 0)))
			flag = true;

		if(iteration == 600 || flag)
		{
			world.setBlockToAir(pos.add(-1, 0, 0));
			world.setBlockToAir(pos);
			world.setBlockToAir(pos.add(1, 0, 0));
			return;
		}

		Task task = new Task("One_Is_Lucky_Reward", 10)
		{
			@Override
			public void callback()
			{
				update(iteration + 1, world, pos);
			}
		};
		Scheduler.scheduleTask(task);
	}
}