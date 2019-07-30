package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnvilRain extends BaseCustomReward
{

	public AnvilRain()
	{
		super(CCubesCore.MODID + ":Anvil_Rain", -45);
	}

	@Override
	public void trigger(World world, BlockPos position, PlayerEntity player, Map<String, Object> settings)
	{
		final RewardBlockCache cache = new RewardBlockCache(world, position, player.getPosition());
		int x1 = RewardsUtil.rand.nextInt(9) - 4;
		int z1 = RewardsUtil.rand.nextInt(9) - 4;

		int x2 = RewardsUtil.rand.nextInt(9) - 4;
		int z2 = RewardsUtil.rand.nextInt(9) - 4;

		int x3 = RewardsUtil.rand.nextInt(9) - 4;
		int z3 = RewardsUtil.rand.nextInt(9) - 4;

		int x4 = RewardsUtil.rand.nextInt(9) - 4;
		int z4 = RewardsUtil.rand.nextInt(9) - 4;

		int x5 = RewardsUtil.rand.nextInt(9) - 4;
		int z5 = RewardsUtil.rand.nextInt(9) - 4;

		int yy;
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(0, yy, 0), Blocks.AIR.getDefaultState());
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(x1, yy, z1), Blocks.AIR.getDefaultState());
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(x2, yy, z2), Blocks.AIR.getDefaultState());
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(x3, yy, z3), Blocks.AIR.getDefaultState());
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(x4, yy, z4), Blocks.AIR.getDefaultState());
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(x5, yy, z5), Blocks.AIR.getDefaultState());
		for(yy = 0; yy < 25; yy++)
			cache.cacheBlock(new BlockPos(player.posX - position.getX(), yy, player.posZ - position.getZ()), Blocks.AIR.getDefaultState());

		cache.cacheBlock(new BlockPos(0, 25, 0), Blocks.ANVIL.getDefaultState());
		cache.cacheBlock(new BlockPos(x1, 25, z1), Blocks.ANVIL.getDefaultState());
		cache.cacheBlock(new BlockPos(x2, 25, z2), Blocks.ANVIL.getDefaultState());
		cache.cacheBlock(new BlockPos(x3, 25, z3), Blocks.ANVIL.getDefaultState());
		cache.cacheBlock(new BlockPos(x4, 25, z4), Blocks.ANVIL.getDefaultState());
		cache.cacheBlock(new BlockPos(x5, 25, z5), Blocks.ANVIL.getDefaultState());
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