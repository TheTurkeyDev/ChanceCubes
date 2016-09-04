package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesAchievements;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class WitherReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		final EntityWither wither = new EntityWither(world);
		wither.setLocationAndAngles((double) x + 0.5D, (double) y + 1D, (double) z + 1.5D, 90.0F, 0.0F);
		wither.renderYawOffset = 90.0F;
		wither.func_82206_m();
		wither.setCustomNameTag("Kiwi");
		world.spawnEntityInWorld(wither);

		RewardsUtil.sendMessageToNearPlayers(world, x, y, z, 32, "\"You've got to ask yourself one question: 'Do I feel lucky?' Well, do ya, punk?\"");

		Task task = new Task("Wither Reward", 180)
		{
			@Override
			public void callback()
			{
				if(!removeEnts(wither))
					player.triggerAchievement(CCubesAchievements.wither);
			}
		};

		Scheduler.scheduleTask(task);
	}

	private boolean removeEnts(Entity ent)
	{
		if(ent.worldObj.rand.nextInt(10) != 1)
		{
			ent.setDead();
			return true;
		}
		return false;
	}

	@Override
	public int getChanceValue()
	{
		return -100;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Wither";
	}

}
