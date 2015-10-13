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
		for(int i = 30; i > 0; i--)
			world.setBlockToAir(x, y + i, z);

		EntityEnderCrystal ent = new EntityEnderCrystal(world);
		ent.setLocationAndAngles(x + 0.5, y, z + 0.5, 0, 0);
		world.spawnEntityInWorld(ent);

		EntityArrow arrow = new EntityArrow(world, x + 0.5, y + 29, z + 0.5);
		arrow.motionX = 0;
		arrow.motionY = -0.25f;
		arrow.motionZ = 0;
		world.spawnEntityInWorld(arrow);
	}

	@Override
	public int getChanceValue()
	{
		return -90;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Ender_Crystal_Timer";
	}
}