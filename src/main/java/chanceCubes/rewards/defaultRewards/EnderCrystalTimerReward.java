package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class EnderCrystalTimerReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int i = 30; i > 0; i--)
			world.setBlockToAir(pos.add(0, i, 0));

		EntityEnderCrystal ent = new EntityEnderCrystal(world);
		ent.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
		world.spawnEntityInWorld(ent);

		EntityArrow arrow = new EntityArrow(world, pos.getX() + 0.5, pos.getY() + 29, pos.getZ() + 0.5);
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