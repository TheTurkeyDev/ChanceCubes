package chanceCubes.rewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class VillageReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		 
	}

	@Override
	public int getChanceValue()
	{
		return 100;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID+":Village_From_The_Sky";
	}
}