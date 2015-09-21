package chanceCubes.rewards.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.ExpirencePart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ExperienceRewardType extends BaseRewardType<ExpirencePart>
{

	public ExperienceRewardType(ExpirencePart... levels)
	{
		super(levels);
	}

	@Override
	public void trigger(final ExpirencePart levels, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		if(levels.getDelay() != 0)
		{
			Task task = new Task("Expirence Reward Delay", levels.getDelay())
			{
				@Override
				public void callback()
				{
					triggerExpirence(levels, world, x, y, z, player);
				}
			};
			Scheduler.scheduleTask(task);
		}
		else
		{
			triggerExpirence(levels, world, x, y, z, player);
		}
	}
	
	public void triggerExpirence(ExpirencePart levels, World world, int x, int y, int z, EntityPlayer player)
	{
		for(int i = 0; i < levels.getNumberofOrbs(); i++)
		{
			Entity newEnt = new EntityXPOrb(world, x, y, z, (levels.getAmount()/levels.getNumberofOrbs()));
			world.spawnEntityInWorld(newEnt);
		}
	}
}
