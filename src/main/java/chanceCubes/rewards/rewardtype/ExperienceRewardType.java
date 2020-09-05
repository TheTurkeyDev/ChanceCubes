package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ExpirencePart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class ExperienceRewardType extends BaseRewardType<ExpirencePart>
{

	public ExperienceRewardType(ExpirencePart... levels)
	{
		super(levels);
	}

	@Override
	public void trigger(final ExpirencePart levels, final ServerWorld world, final int x, final int y, final int z, final PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("Expirence Reward Delay", levels.getDelay())
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < levels.getNumberofOrbs(); i++)
				{
					Entity newEnt = new ExperienceOrbEntity(world, x, y + 1, z, (levels.getAmount() / levels.getNumberofOrbs()));
					world.addEntity(newEnt);
				}
			}
		});
	}
}
