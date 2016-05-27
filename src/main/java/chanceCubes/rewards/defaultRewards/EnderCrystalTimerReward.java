package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnderCrystalTimerReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int i = 30; i > 0; i--)
			RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, i, 0));

		EntityEnderCrystal ent = new EntityEnderCrystal(world);
		ent.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
		world.spawnEntityInWorld(ent);

		EntityArrow arrow = new EntityTippedArrow(world, pos.getX() + 0.5, pos.getY() + 29, pos.getZ() + 0.5);
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