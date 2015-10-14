package chanceCubes.rewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class ThrownInAirReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		int px = (int) Math.floor(player.posX);
		int py = (int) Math.floor(player.posY) + 1;
		int pz = (int) Math.floor(player.posZ);

		for(int yy = 0; yy < 30; yy++)
			for(int xx = -1; xx < 2; xx++)
				for(int zz = -1; zz < 2; zz++)
					world.setBlockToAir(px + xx, py + yy, pz + zz);

		player.isAirBorne = true;
		player.motionY = 100;
	}

	@Override
	public int getChanceValue()
	{
		return -30;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Thrown_In_Air";
	}

}
