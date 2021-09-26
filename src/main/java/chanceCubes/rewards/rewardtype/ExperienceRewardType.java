package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ExpirencePart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;

public class ExperienceRewardType extends BaseRewardType<ExpirencePart>
{

	public ExperienceRewardType(ExpirencePart... levels)
	{
		super(levels);
	}

	@Override
	public void trigger(final ExpirencePart levels, final ServerLevel level, final int x, final int y, final int z, final Player player)
	{
		Scheduler.scheduleTask(new Task("Expirence Reward Delay", levels.getDelay())
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < levels.getNumberofOrbs(); i++)
				{
					Entity newEnt = new ExperienceOrb(level, x, y + 1, z, (levels.getAmount() / levels.getNumberofOrbs()));
					level.addFreshEntity(newEnt);
				}
			}
		});
	}
}
