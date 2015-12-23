package chanceCubes.rewards.defaultRewards;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class TrollHoleReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, BlockPos pos, final EntityPlayer player)
	{

		final Map<BlockPos, IBlockState> storedBlocks = new HashMap<BlockPos, IBlockState>();
		final int px = (int) Math.floor(player.posX);
		final int py = (int) Math.floor(player.posY) - 1;
		final int pz = (int) Math.floor(player.posZ);

		for(int y = 0; y < 75; y++)
		{
			for(int x = -2; x < 3; x++)
			{
				for(int z = -2; z < 3; z++)
				{
					storedBlocks.put(new BlockPos(x, y, z), world.getBlockState(new BlockPos(px + x, py - y, pz + z)));
					world.setBlockToAir(new BlockPos(px + x, py - y, pz + z));
				}
			}
		}

		Task task = new Task("TrollHole", 35)
		{
			@Override
			public void callback()
			{
				fillHole(world, player, px, py, pz, storedBlocks);
			}

		};

		Scheduler.scheduleTask(task);
	}

	public void fillHole(World world, EntityPlayer player, int x, int y, int z, Map<BlockPos, IBlockState> storedBlocks)
	{
		for(BlockPos loc : storedBlocks.keySet())
			world.setBlockState(new BlockPos(x + loc.getX(), y - loc.getY(), z + loc.getZ()), storedBlocks.get(loc));

		player.setPositionAndUpdate(x, y + 1, z);
		player.motionY = 0;
		player.fallDistance = 0;
	}

	@Override
	public int getChanceValue()
	{
		return -30;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":TrollHole";
	}
}