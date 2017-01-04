package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RainingCatsAndCogsReward implements IChanceCubeReward
{
	private Random rand = new Random();
	
	private String[] names = {"EmoKiba", "Turkey", "MrComputerGhost", "Valsis", "Silver", "Amatt", "Musician", "ReNinjaKitteh"};

	@Override
	public void trigger(final World world, BlockPos position, EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, position, 36, "It's raining Cats and dogs!");
		spawnEntity(0, world, position, player);
	}

	public void spawnEntity(final int count, final World world, final BlockPos pos, final EntityPlayer player)
	{
		if(count < 100)
		{
			int xInc = rand.nextInt(10) * (rand.nextBoolean() ? -1 : 1);
			int zInc = rand.nextInt(10) * (rand.nextBoolean() ? -1 : 1);
			if(rand.nextBoolean())
			{
				final EntityWolf dog = new EntityWolf(world);
				dog.setPositionAndRotation(player.getPosition().getX() + xInc, 256, player.getPosition().getZ() + zInc, 0, 0);
				dog.setTamed(true);
				dog.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 500, 1000));
				dog.setCustomNameTag(names[rand.nextInt(names.length)]);
				dog.setAlwaysRenderNameTag(true);
				world.spawnEntityInWorld(dog);
				Scheduler.scheduleTask(new Task("Despawn Delay", 200)
				{
					@Override
					public void callback()
					{
						dog.setDead();
					}

				});
			}
			else
			{
				final EntityOcelot cat = new EntityOcelot(world);
				cat.setPositionAndRotation(player.getPosition().getX() + xInc, 256, player.getPosition().getZ() + zInc, 0, 0);
				cat.setTameSkin(1);
				cat.setTamed(true);
				cat.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 500, 1000));
				cat.setCustomNameTag(names[rand.nextInt(names.length)]);
				cat.setAlwaysRenderNameTag(true);
				world.spawnEntityInWorld(cat);
				Scheduler.scheduleTask(new Task("Despawn Delay", 200)
				{
					@Override
					public void callback()
					{
						cat.setDead();
					}

				});
			}
			Task task = new Task("Raining Cats and Dogs", 5)
			{
				@Override
				public void callback()
				{
					spawnEntity(count + 1, world, pos, player);
				}

			};
			Scheduler.scheduleTask(task);
		}
	}

	@Override
	public int getChanceValue()
	{
		return -45;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Cats_And_Dogs";
	}
}