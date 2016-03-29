package chanceCubes.rewards.giantRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class PotionsReward implements IChanceCubeReward
{
	private int[] metas = new int[] { 16385, 16450, 16419, 16420, 16452, 16453, 16422, 16424, 16393, 16426, 16428, 16460, 16461, 16430 };
	private Random rand = new Random();

	private EntityPotion pot;

	@Override
	public void trigger(final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		player.addChatMessage(new ChatComponentText("It's called art! Look it up!"));
		throwPoitonCircle(0, world, x, y, z, player);

		Scheduler.scheduleTask(new Task("Potion Circle", 140)
		{
			@Override
			public void callback()
			{
				throwPoiton(0, world, x, y, z, player);
			}
		});
	}

	private void throwPoitonCircle(final int itteration, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 20))
		{
			pot = new EntityPotion(world, player, new ItemStack(Items.potionitem, 1, metas[rand.nextInt(metas.length)]));
			pot.setLocationAndAngles(x + 0.5, y, z + 0.5, 0, 0);
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
					throwPoitonCircle(itteration + 1, world, x, y, z, player);
				}
			});
		}
	}

	private void throwPoiton(final int itteration, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		for(double yy = -0.2; yy <= 1; yy += 0.1)
		{
			pot = new EntityPotion(world, player, new ItemStack(Items.potionitem, 1, metas[rand.nextInt(metas.length)]));
			pot.setLocationAndAngles(x + 0.5, y, z + 0.5, 0, 0);
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
					throwPoiton(itteration + 1, world, x, y, z, player);
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
