package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OreSphereReward extends BaseCustomReward
{
	public OreSphereReward()
	{
		super(CCubesCore.MODID + ":ore_sphere", 0);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		List<OffsetBlock> blocks = new ArrayList<>();

		List<String> whiteList = Arrays.asList(super.getSettingAsStringList(settings, "whiteList", new String[0]));

		Block ore;
		if(whiteList.size() > 0)
		{
			ore = RewardsUtil.getRandomOreFromOreDict(whiteList.get(RewardsUtil.rand.nextInt(whiteList.size())));
		}
		else
		{
			List<String> blackList = Arrays.asList(super.getSettingAsStringList(settings, "blackList", new String[0]));
			ore = RewardsUtil.getRandomOre(blackList);
		}

		int delay = 0;
		for(int i = 0; i < 5; i++)
		{
			for(int yy = -5; yy < 6; yy++)
			{
				for(int zz = -5; zz < 6; zz++)
				{
					for(int xx = -5; xx < 6; xx++)
					{
						BlockPos loc = new BlockPos(xx, yy, zz);
						double dist = Math.sqrt(Math.abs(loc.distanceSq(0, 0, 0, false)));
						if(dist <= i && dist > i - 1)
						{
							OffsetBlock osb = new OffsetBlock(xx, yy, zz, ore, false, delay);
							blocks.add(osb);
							delay++;
						}
					}
				}
			}
			delay += 10;
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}
}