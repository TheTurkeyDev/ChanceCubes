package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.CCubesAchievements;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WitherReward implements IChanceCubeReward
{
	private Random random = new Random();
	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		final EntityWither wither = new EntityWither(world);
		wither.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 1.5D, 90.0F, 0.0F);
		wither.renderYawOffset = 90.0F;
		wither.ignite();
		if(world.rand.nextBoolean())
			wither.setCustomNameTag("Kiwi");
		else
			wither.setCustomNameTag("Kehaan");
		world.spawnEntityInWorld(wither);

		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "\"You've got to ask yourself one question: 'Do I feel lucky?' Well, do ya, punk?\"");

		Task task = new Task("Wither Reward", 180)
		{
			@Override
			public void callback()
			{
				if(!removeEnts(wither))
					player.addStat(CCubesAchievements.wither);
			}
		};

		Scheduler.scheduleTask(task);
	}

	private boolean removeEnts(Entity ent)
	{
		if(random.nextInt(10) != 1)
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
