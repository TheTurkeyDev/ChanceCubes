package chanceCubes.rewards.giantRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PotionsReward implements IChanceCubeReward
{
	private Random rand = new Random();

	private EntityPotion pot;

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player)
	{
		player.addChatMessage(new TextComponentString("It's called art! Look it up!"));
		throwPoitonCircle(0, world, pos, player);

		Scheduler.scheduleTask(new Task("Potion Circle", 140)
		{
			@Override
			public void callback()
			{
				throwPoiton(0, world, pos, player);
			}
		});
	}

	private void throwPoitonCircle(final int itteration, final World world, final BlockPos pos, final EntityPlayer player)
	{
		for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 20))
		{
			PotionType potionType = PotionType.REGISTRY.getObjectById(rand.nextInt(PotionType.REGISTRY.getKeys().size()));
			pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
			pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
			pot.motionX = Math.cos(rad) * (0.1 + (0.05 * itteration));
			pot.motionY = 1;
			pot.motionZ = Math.sin(rad) * (0.1 + (0.05 * itteration));
			world.spawnEntityInWorld(pot);
		}

		if(itteration < 5)
		{
			Scheduler.scheduleTask(new Task("Potion Circle", 20)
			{
				@Override
				public void callback()
				{
					throwPoitonCircle(itteration + 1, world, pos, player);
				}
			});
		}
	}

	private void throwPoiton(final int itteration, final World world, final BlockPos pos, final EntityPlayer player)
	{
		for(double yy = -0.2; yy <= 1; yy += 0.1)
		{
			PotionType potionType = PotionType.REGISTRY.getObjectById(rand.nextInt(PotionType.REGISTRY.getKeys().size()));
			pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
			pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
			pot.motionX = Math.cos(itteration * (Math.PI / 30));
			pot.motionY = yy;
			pot.motionZ = Math.sin(itteration * (Math.PI / 30));
			world.spawnEntityInWorld(pot);
		}

		if(itteration < 200)
		{
			Scheduler.scheduleTask(new Task("Potion Circle", 2)
			{
				@Override
				public void callback()
				{
					throwPoiton(itteration + 1, world, pos, player);
				}
			});
		}
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Raining_Potions";
	}

}