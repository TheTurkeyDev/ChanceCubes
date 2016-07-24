package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.Location3I;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class OneIsLuckyReward implements IChanceCubeReward
{
	private Random random = new Random();

	@Override
	public void trigger(final World world, final int x, final int y, final int z, EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, x, y, z, 32, "A Lucky Block Salute");
		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = "One is lucky";
		sign.signText[1] = "One is not";
		sign.signText[3] = "#OGLuckyBlocks";
		boolean leftLucky = random.nextBoolean();
		TileChanceCube leftCube = new TileChanceCube(leftLucky ? 100 : -100);
		TileChanceCube rightCube = new TileChanceCube(!leftLucky ? 100 : -100);

		if(RewardsUtil.placeBlock(CCubesBlocks.chanceCube, world, x - 1, y, z))
			world.setTileEntity(x - 1, y, z, leftCube);
		if(RewardsUtil.placeBlock(Blocks.standing_sign, world, x, y, z))
			world.setTileEntity(x, y, z, sign);
		if(RewardsUtil.placeBlock(CCubesBlocks.chanceCube, world, x + 1, y, z))
			world.setTileEntity(x + 1, y, z, rightCube);

		Task task = new Task("One_Is_Lucky_Reward", 10)
		{
			@Override
			public void callback()
			{
				update(0, world, new Location3I(x, y, z));
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

	public void update(final int iteration, final World world, final Location3I loc)
	{
		boolean flag = false;

		if(world.isAirBlock(loc.getX() - 1, loc.getY(), loc.getZ()) || world.isAirBlock(loc.getX() + 1, loc.getY(), loc.getZ()))
			flag = true;

		if(iteration == 600 || flag)
		{
			world.setBlockToAir(loc.getX() - 1, loc.getY(), loc.getZ());
			world.setBlockToAir(loc.getX(), loc.getY(), loc.getZ());
			world.setBlockToAir(loc.getX() + 1, loc.getY(), loc.getZ());
			return;
		}

		Task task = new Task("One_Is_Lucky_Reward", 10)
		{
			@Override
			public void callback()
			{
				update(iteration + 1, world, loc);
			}
		};
		Scheduler.scheduleTask(task);
	}
}
