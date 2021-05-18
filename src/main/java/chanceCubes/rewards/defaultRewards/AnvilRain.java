package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class AnvilRain extends BaseCustomReward
{

	public AnvilRain()
	{
		super(CCubesCore.MODID + ":anvil_rain", -45);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos position, PlayerEntity player, JsonObject settings)
	{
		final RewardBlockCache cache = new RewardBlockCache(world, position, player.getPosition());
		int numAnvils = super.getSettingAsInt(settings, "numAnvils", 5, 0, 100);
		for(int i = 0; i < numAnvils; i++)
		{
			int xx = RewardsUtil.rand.nextInt(9) - 4;
			int zz = RewardsUtil.rand.nextInt(9) - 4;
			for(int yy = 0; yy < 25; yy++)
				cache.cacheBlock(new BlockPos(xx, yy, zz), Blocks.AIR.getDefaultState());
			cache.cacheBlock(new BlockPos(xx, 25, zz), Blocks.ANVIL.getDefaultState());
		}

		int yy;
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(0, yy, 0), Blocks.AIR.getDefaultState());
		cache.cacheBlock(new BlockPos(0, 25, 0), Blocks.ANVIL.getDefaultState());

		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(player.getPosX() - position.getX(), yy, player.getPosZ() - position.getZ()), Blocks.AIR.getDefaultState());
		cache.cacheBlock(new BlockPos(player.getPosX() - position.getX(), 25, player.getPosZ() - position.getZ()), Blocks.ANVIL.getDefaultState());

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