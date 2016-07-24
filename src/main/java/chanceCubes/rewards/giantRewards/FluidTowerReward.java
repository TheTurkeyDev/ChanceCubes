package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class FluidTowerReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		for(int i = 0; i < 25; i++)
			RewardsUtil.placeBlock(ChanceCubeRegistry.getRandomFluid().getBlock(), world, x, y + i, z);
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
