package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OrePillarReward implements IChanceCubeReward
{
	private Random rand = new Random();

	public OrePillarReward()
	{

	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();
		int delay = 0;
		for(int i = 0; i < rand.nextInt(4) + 2; i++)
		{
			int xx = rand.nextInt(30) - 15;
			int zz = rand.nextInt(30) - 15;
			for(int yy = 1; yy < 255; yy++)
			{
				CustomEntry<Block, Integer> ore = RewardsUtil.getRandomOre();
				OffsetBlock osb = new OffsetBlock(xx, yy - pos.getY(), zz, ore.getKey(), false, delay / 3);
				osb.setBlockState(ore.getKey().getStateFromMeta(ore.getValue()));
				blocks.add(osb);
				delay++;
			}
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Ore_Pillars";
	}

}