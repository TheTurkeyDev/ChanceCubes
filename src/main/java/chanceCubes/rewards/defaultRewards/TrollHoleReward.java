package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrollHoleReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, BlockPos pos, final EntityPlayer player)
	{

		final BlockPos worldPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY) - 1, Math.floor(player.posZ));
		final RewardBlockCache cache = new RewardBlockCache(world, worldPos, new BlockPos(worldPos.getX(), worldPos.getY() + 1, worldPos.getZ()));

		for(int y = 0; y > -75; y--)
			for(int x = -2; x < 3; x++)
				for(int z = -2; z < 3; z++)
					cache.cacheBlock(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());

		Scheduler.scheduleTask(new Task("TrollHole", 35)
		{
			@Override
			public void callback()
			{
				cache.restoreBlocks(player);
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
		return CCubesCore.MODID + ":Troll_Hole";
	}
}