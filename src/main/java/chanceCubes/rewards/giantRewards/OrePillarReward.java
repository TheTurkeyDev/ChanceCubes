package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OrePillarReward extends BaseCustomReward
{
	public OrePillarReward()
	{
		super(CCubesCore.MODID + ":ore_pillars", 0);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		List<String> whiteList = Arrays.asList(super.getSettingAsStringList(settings, "white_list", new String[0]));
		List<String> blackList = Arrays.asList(super.getSettingAsStringList(settings, "black_list", new String[0]));


		List<OffsetBlock> blocks = new ArrayList<>();
		int delay = 0;
		for(int i = 0; i < RewardsUtil.rand.nextInt(4) + 2; i++)
		{
			int xx = RewardsUtil.rand.nextInt(30) - 15;
			int zz = RewardsUtil.rand.nextInt(30) - 15;
			for(int yy = 1; yy < 255; yy++)
			{
				Block ore;
				if(whiteList.size() > 0)
					ore = RewardsUtil.getRandomOreFromOreDict(whiteList.get(RewardsUtil.rand.nextInt(whiteList.size())));
				else
					ore = RewardsUtil.getRandomOre(blackList);
				OffsetBlock osb = new OffsetBlock(xx, yy - pos.getY(), zz, ore, false, delay / 3);
				blocks.add(osb);
				delay++;
			}
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}
}