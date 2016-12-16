package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesAchievements;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockCake;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CakeIsALieReward implements IChanceCubeReward
{
	private Random random = new Random();

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "But is it a lie?");

		RewardsUtil.placeBlock(Blocks.CAKE.getDefaultState(), world, pos);

		if(random.nextInt(3) == 1)
		{
			Task task = new Task("Cake_Is_A_Lie", 20)
			{
				@Override
				public void callback()
				{
					update(0, world, pos, player);
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

	public void update(final int iteration, final World world, final BlockPos pos, final EntityPlayer player)
	{
		if(!world.getBlockState(pos).getBlock().equals(Blocks.CAKE))
			return;
		if(world.getBlockState(pos).getValue(BlockCake.BITES) > 0)
		{
			world.setBlockToAir(pos);
			RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "It's a lie!!!");
			EntityCreeper creeper = new EntityCreeper(world);
			creeper.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), pos.getX() == 1 ? 90 : -90, 0);
			if(random.nextInt(10) == 1)
				creeper.onStruckByLightning(null);
			creeper.addPotionEffect(new PotionEffect(MobEffects.SPEED, 9999, 2));
			creeper.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 999));
			world.spawnEntityInWorld(creeper);
			player.addStat(CCubesAchievements.itsALie);
			return;
		}

		if(iteration == 300)
		{
			world.setBlockToAir(pos);
			return;
		}

		Task task = new Task("Cake_Is_A_Lie", 20)
		{
			@Override
			public void callback()
			{
				update(iteration + 1, world, pos, player);
			}
		};
		Scheduler.scheduleTask(task);
	}
}