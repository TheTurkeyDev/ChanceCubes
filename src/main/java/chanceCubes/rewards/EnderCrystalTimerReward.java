package chanceCubes.rewards;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class EnderCrystalTimerReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		for(int i = 30; i > 0; i++)
			world.setBlockToAir(x, y + i, z);
		
		EntityEnderCrystal ent = new EntityEnderCrystal(world);
		ent.setLocationAndAngles(x, y, z, 90, 90);
		world.spawnEntityInWorld(ent);
		
		EntityArrow arrow = new EntityArrow(world, x, y + 29, z);
		arrow.motionY = -1;
		world.spawnEntityInWorld(arrow);
	}

	@Override
	public int getChanceValue()
	{
		return -70;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Ender_Crystal_Timer";
	}
}