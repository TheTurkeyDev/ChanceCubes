package chanceCubes.rewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class RandomTeleportReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		int xChange = (world.rand.nextInt(30) + 20) + x;
		int zChange = (world.rand.nextInt(30) + 20) + z;

		int yChange = -1;

		for(int yy = 0; yy <= world.getActualHeight(); yy++)
		{
			if(world.getBlock(xChange, yy, zChange).isAir(world, xChange, yy, zChange) && world.getBlock(xChange, yy + 1, zChange).isAir(world, xChange, yy + 1, zChange))
			{
				yChange = yy;
				break;
			}
		}
		if(yChange == -1)
			return;
		
		player.setPositionAndUpdate(xChange, yChange, zChange);
	}

	@Override
	public int getChanceValue()
	{
		return -35;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Random_Teleport";
	}
}