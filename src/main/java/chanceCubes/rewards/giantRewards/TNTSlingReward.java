package chanceCubes.rewards.giantRewards;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class TNTSlingReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		this.throwTNT(0, world, x, y, z);
	}

	public void throwTNT(final int count, final World world, final int x, final int y, final int z)
	{
		EntityTNTPrimed tnt = new EntityTNTPrimed(world, x, y + 1D, z, null);
		world.spawnEntityInWorld(tnt);
		tnt.fuse = 60;
		tnt.motionX = -1 + (Math.random() * 2);
		tnt.motionY = Math.random();
		tnt.motionZ = -1 + (Math.random() * 2);

		if(count < 25)
		{
			Task task = new Task("Throw TNT", 10)
			{

				@Override
				public void callback()
				{
					throwTNT(count + 1, world, x, y, z);
				}

			};
			Scheduler.scheduleTask(task);
		}
		else
		{
			for(double xx = 1; xx > -1; xx -= 0.25)
			{
				for(double zz = 1; zz > -1; zz -= 0.25)
				{
					tnt = new EntityTNTPrimed(world, x, y + 1D, z, null);
					world.spawnEntityInWorld(tnt);
					tnt.fuse = 60;
					tnt.motionX = xx;
					tnt.motionY = Math.random();
					tnt.motionZ = zz;
				}
			}
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
		return CCubesCore.MODID + ":TNT_Throw";
	}

}
