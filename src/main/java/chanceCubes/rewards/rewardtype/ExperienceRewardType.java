package chanceCubes.rewards.rewardtype;

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
		Scheduler.scheduleTask(new Task("Expirence Reward Delay", levels.getDelay())
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < levels.getNumberofOrbs(); i++)
				{
					Entity newEnt = new EntityXPOrb(world, x, y + 1, z, (levels.getAmount() / levels.getNumberofOrbs()));
					world.spawnEntity(newEnt);
				}
			}
		});
	}
}
