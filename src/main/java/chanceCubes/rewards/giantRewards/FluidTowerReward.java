package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidTowerReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int i = 0; i < 25; i++)
			RewardsUtil.placeBlock(ChanceCubeRegistry.getRandomFluid().getBlock().getDefaultState(), world, pos.add(0, i, 0));
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