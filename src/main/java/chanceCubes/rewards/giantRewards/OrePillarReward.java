package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrePillarReward extends BaseCustomReward
{
	public OrePillarReward()
	{
		super(CCubesCore.MODID + ":ore_pillars", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		List<String> whiteList = Arrays.asList(super.getSettingAsStringList(settings, "whiteList", new String[0]));
		List<String> blackList = Arrays.asList(super.getSettingAsStringList(settings, "blackList", new String[0]));


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
			b.spawnInWorld(level, pos.getX(), pos.getY(), pos.getZ());
	}
}