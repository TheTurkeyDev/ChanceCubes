package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnvilRain extends BaseCustomReward
{

	public AnvilRain()
	{
		super(CCubesCore.MODID + ":Anvil_Rain", -45);
	}

	@Override
	public void trigger(World world, BlockPos position, EntityPlayer player, Map<String, Object> settings)
	{
		final RewardBlockCache cache = new RewardBlockCache(world, position, player.getPosition());
		for(int i = 0; i < super.getSettingAsInt(settings, "num_anvils", 5, 0, 100); i++)
		{
			int xx = RewardsUtil.rand.nextInt(9) - 4;
			int zz = RewardsUtil.rand.nextInt(9) - 4;
			for(int yy = 0; yy < 25; yy++)
				cache.cacheBlock(new BlockPos(xx, yy, zz), Blocks.AIR.getDefaultState());
			cache.cacheBlock(new BlockPos(xx, 25, zz), Blocks.ANVIL.getDefaultState());
		}

		int yy = 0;
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(0, yy, 0), Blocks.AIR.getDefaultState());
		cache.cacheBlock(new BlockPos(0, 25, 0), Blocks.ANVIL.getDefaultState());

		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(player.posX - position.getX(), yy, player.posZ - position.getZ()), Blocks.AIR.getDefaultState());
		cache.cacheBlock(new BlockPos(player.posX - position.getX(), 25, player.posZ - position.getZ()), Blocks.ANVIL.getDefaultState());

		for(int xx = 0; xx < 2; xx++)
			for(int zz = -4; zz < 5; zz++)
				for(int yyy = 0; yyy < 3; yyy++)
					cache.cacheBlock(new BlockPos(xx == 1 ? 5 : -5, yyy, zz), Blocks.COBBLESTONE.getDefaultState());

		for(int xx = -4; xx < 5; xx++)
			for(int zz = 0; zz < 2; zz++)
				for(int yyy = 0; yyy < 3; yyy++)
					cache.cacheBlock(new BlockPos(xx, yyy, zz == 1 ? 5 : -5), Blocks.COBBLESTONE.getDefaultState());

		Scheduler.scheduleTask(new Task("Anvil_Rain_cache_Delay", 100)
		{
			@Override
			public void callback()
			{
				cache.restoreBlocks(player);
			}
		});
	}
}