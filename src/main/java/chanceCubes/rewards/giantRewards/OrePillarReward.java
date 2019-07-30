package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OrePillarReward extends BaseCustomReward
{
	public OrePillarReward()
	{
		super(CCubesCore.MODID + ":Ore_Pillars", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		List<OffsetBlock> blocks = new ArrayList<>();
		int delay = 0;
		for(int i = 0; i < RewardsUtil.rand.nextInt(4) + 2; i++)
		{
			int xx = RewardsUtil.rand.nextInt(30) - 15;
			int zz = RewardsUtil.rand.nextInt(30) - 15;
			for(int yy = 1; yy < 255; yy++)
			{
				Block ore = RewardsUtil.getRandomOre();
				OffsetBlock osb = new OffsetBlock(xx, yy - pos.getY(), zz, ore, false, delay / 3);
				osb.setBlockState(ore.getDefaultState());
				blocks.add(osb);
				delay++;
			}
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}
}