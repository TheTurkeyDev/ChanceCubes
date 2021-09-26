package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class AnvilRain extends BaseCustomReward
{

	public AnvilRain()
	{
		super(CCubesCore.MODID + ":anvil_rain", -45);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos position, Player player, JsonObject settings)
	{
		final RewardBlockCache cache = new RewardBlockCache(level, position, player.getOnPos());
		int numAnvils = super.getSettingAsInt(settings, "numAnvils", 5, 0, 100);
		for(int i = 0; i < numAnvils; i++)
		{
			int xx = RewardsUtil.rand.nextInt(9) - 4;
			int zz = RewardsUtil.rand.nextInt(9) - 4;
			for(int yy = 0; yy < 25; yy++)
				cache.cacheBlock(new BlockPos(xx, yy, zz), Blocks.AIR.defaultBlockState());
			cache.cacheBlock(new BlockPos(xx, 25, zz), Blocks.ANVIL.defaultBlockState());
		}

		int yy;
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(0, yy, 0), Blocks.AIR.defaultBlockState());
		cache.cacheBlock(new BlockPos(0, 25, 0), Blocks.ANVIL.defaultBlockState());

		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(player.getX() - position.getX(), yy, player.getZ() - position.getZ()), Blocks.AIR.defaultBlockState());
		cache.cacheBlock(new BlockPos(player.getX() - position.getX(), 25, player.getZ() - position.getZ()), Blocks.ANVIL.defaultBlockState());

		for(int xx = 0; xx < 2; xx++)
			for(int zz = -4; zz < 5; zz++)
				for(int yyy = 0; yyy < 3; yyy++)
					cache.cacheBlock(new BlockPos(xx == 1 ? 5 : -5, yyy, zz), Blocks.COBBLESTONE.defaultBlockState());

		for(int xx = -4; xx < 5; xx++)
			for(int zz = 0; zz < 2; zz++)
				for(int yyy = 0; yyy < 3; yyy++)
					cache.cacheBlock(new BlockPos(xx, yyy, zz == 1 ? 5 : -5), Blocks.COBBLESTONE.defaultBlockState());

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