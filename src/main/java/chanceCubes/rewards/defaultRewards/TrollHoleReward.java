package chanceCubes.rewards.defaultRewards;

import java.util.HashMap;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

		Scheduler.scheduleTask(new Task("TrollHole", 35)
		{
			@Override
			public void callback()
			{
				for(BlockPos loc : storedBlocks.keySet())
					world.setBlockState(new BlockPos(px + loc.getX(), py - loc.getY(), pz + loc.getZ()), storedBlocks.get(loc));

				player.setPositionAndUpdate(px, py + 1, pz);
				player.motionY = 0;
				player.fallDistance = 0;
			}

		});
	}

	@Override
	public int getChanceValue()
	{
		return -20;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":TrollHole";
	}
}