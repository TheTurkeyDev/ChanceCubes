package chanceCubes.rewards.giantRewards;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ThrowablesReward implements IChanceCubeReward
{
	private Random random = new Random();
	
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		this.throwThing(0, world, x, y, z);
	}

	public void throwThing(final int count, final World world, final int x, final int y, final int z)
	{
		int entChoice = random.nextInt(5);
		Entity throwEnt;
		if(entChoice == 0)
		{
			throwEnt = new EntityArrow(world);
		}
		else if(entChoice == 1)
		{
			throwEnt = new EntityLargeFireball(world);
		}
		else if(entChoice == 2)
		{
			throwEnt = new EntityEgg(world);
		}
		else if(entChoice == 3)
		{
			throwEnt = new EntityWitherSkull(world);
		}
		else
		{
			throwEnt = new EntityTNTPrimed(world);
		}
		throwEnt.setLocationAndAngles(x, y, z, 0, 0);
		throwEnt.motionX = -1 + (Math.random() * 2);
		throwEnt.motionY = Math.random();
		throwEnt.motionZ = -1 + (Math.random() * 2);
		world.spawnEntityInWorld(throwEnt);
		System.out.println(throwEnt.posX + " " + throwEnt.posY + " " + throwEnt.posZ);
		System.out.println(x + " " + y + " " + z);

		if(count < 50)
		{
			Task task = new Task("Throw TNT", 5)
			{

				@Override
				public void callback()
				{
					throwThing(count + 1, world, x, y, z);
				}

			};
			Scheduler.scheduleTask(task);
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
		return CCubesCore.MODID + ":Trowables";
	}

}
