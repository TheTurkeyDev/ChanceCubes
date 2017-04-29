package chanceCubes.rewards.giantRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThrowablesReward implements IChanceCubeReward
{
	private Random random = new Random();

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		this.throwThing(0, world, pos);
	}

	public void throwThing(final int count, final World world, final BlockPos pos)
	{
		int entChoice = random.nextInt(4);
		Entity throwEnt;
		if(entChoice == 0)
		{
			throwEnt = new EntityTippedArrow(world);
		}
		else if(entChoice == 1)
		{
			throwEnt = new EntityLargeFireball(world);
			((EntityLargeFireball) throwEnt).accelerationX = 0.1f * (-1 + (Math.random() * 2));
			((EntityLargeFireball) throwEnt).accelerationY = 0.1f * (-1 + (Math.random() * 2));
			((EntityLargeFireball) throwEnt).accelerationZ = 0.1f * (-1 + (Math.random() * 2));
		}
		else if(entChoice == 2)
		{
			throwEnt = new EntityEgg(world);
		}
		else
		{
			throwEnt = new EntityTNTPrimed(world);
			((EntityTNTPrimed) throwEnt).setFuse(20);
		}
		throwEnt.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
		throwEnt.motionX = -1 + (Math.random() * 2);
		throwEnt.motionY = -1 + (Math.random() * 2);
		throwEnt.motionZ = -1 + (Math.random() * 2);
		world.spawnEntityInWorld(throwEnt);

		if(count < 50)
		{
			Task task = new Task("Throw TNT", 5)
			{

				@Override
				public void callback()
				{
					throwThing(count + 1, world, pos);
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
		return CCubesCore.MODID + ":Throwables";
	}

}