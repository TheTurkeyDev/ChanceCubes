package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.Location3I;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class OneIsLuckyReward implements IChanceCubeReward
{
	private Random random = new Random();

	@Override
	public void trigger(final World world, final int x, final int y, final int z, EntityPlayer player)
	{
		player.addChatMessage(new ChatComponentText("A Lucky Block Salute"));
		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = "One is lucky";
		sign.signText[1] = "One is not";
		sign.signText[3] = "#OGLuckyBlocks";
		boolean leftLucky = random.nextBoolean();
		TileChanceCube leftCube = new TileChanceCube(leftLucky ? 100 : -100);
		TileChanceCube rightCube = new TileChanceCube(!leftLucky ? 100 : -100);
		world.setBlock(x - 1, y, z, CCubesBlocks.chanceCube);
		world.setTileEntity(x - 1, y, z, leftCube);
		world.setBlock(x, y, z, Blocks.standing_sign);
		world.setTileEntity(x, y, z, sign);
		world.setBlock(x + 1, y, z, CCubesBlocks.chanceCube);
		world.setTileEntity(x + 1, y, z, rightCube);

		Task task = new Task("One_Is_Lucky_Reward", 20)
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
		
		if(iteration == 300 || flag)
		{
			world.setBlockToAir(loc.getX() - 1, loc.getY(), loc.getZ());
			world.setBlockToAir(loc.getX(), loc.getY(), loc.getZ());
			world.setBlockToAir(loc.getX() + 1, loc.getY(), loc.getZ());
			return;
		}

		Task task = new Task("Maze_Reward_Update", 20)
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
