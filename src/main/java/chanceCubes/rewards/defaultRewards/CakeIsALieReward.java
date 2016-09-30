package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesAchievements;
import chanceCubes.util.Location3I;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CakeIsALieReward implements IChanceCubeReward
{
	private Random random = new Random();

	@Override
	public void trigger(final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, x, y, z, 32, "But is it a lie?");

		RewardsUtil.placeBlock(Blocks.cake, world, x, y, z);

		if(random.nextInt(3) == 1)
		{
			Task task = new Task("Cake_Is_A_Lie", 20)
			{
				@Override
				public void callback()
				{
					update(0, world, new Location3I(x, y, z), player);
				}
			};
			Scheduler.scheduleTask(task);
		}
	}

	@Override
	public int getChanceValue()
	{
		return 20;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Cake";
	}

	public void update(final int iteration, final World world, final Location3I loc, final EntityPlayer player)
	{
		if(world.getBlockMetadata(loc.getX(), loc.getY(), loc.getZ()) > 0)
		{
			RewardsUtil.placeBlock(Blocks.air, world, loc.getX(), loc.getY(), loc.getZ());
			RewardsUtil.sendMessageToNearPlayers(world, loc.getX(), loc.getY(), loc.getZ(), 32, "It's a lie!!!");
			EntityCreeper creeper = new EntityCreeper(world);
			creeper.setLocationAndAngles(loc.getX(), loc.getY(), loc.getZ(), loc.getX() == 1 ? 90 : -90, 0);
			if(random.nextInt(10) == 1)
				creeper.getDataWatcher().updateObject(17, Byte.valueOf((byte) 1));
			creeper.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 9999, 2));
			creeper.addPotionEffect(new PotionEffect(Potion.resistance.id, 60, 999));
			world.spawnEntityInWorld(creeper);
			player.triggerAchievement(CCubesAchievements.itsALie);
			return;
		}

		if(iteration == 300)
		{
			world.setBlockToAir(loc.getX(), loc.getY(), loc.getZ());
			return;
		}

		Task task = new Task("Cake_Is_A_Lie", 20)
		{
			@Override
			public void callback()
			{
				update(iteration + 1, world, loc, player);
			}
		};
		Scheduler.scheduleTask(task);
	}
}
