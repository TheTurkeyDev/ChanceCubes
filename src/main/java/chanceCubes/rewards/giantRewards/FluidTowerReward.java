package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidTowerReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int i = 0; i < 25; i++)
		{
			RewardsUtil.placeBlock(RewardsUtil.getRandomFluid().getBlock().getDefaultState(), world, pos.add(0, i, 0));
			RewardsUtil.placeBlock(Blocks.GLASS.getDefaultState(), world, pos.add(1, i, 0));
			RewardsUtil.placeBlock(Blocks.GLASS.getDefaultState(), world, pos.add(-1, i, 0));
			RewardsUtil.placeBlock(Blocks.GLASS.getDefaultState(), world, pos.add(0, i, 1));
			RewardsUtil.placeBlock(Blocks.GLASS.getDefaultState(), world, pos.add(0, i, -1));
		}
	}

	@Override
	public int getChanceValue()
	{
		return 15;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Fluid_Tower";
	}
}