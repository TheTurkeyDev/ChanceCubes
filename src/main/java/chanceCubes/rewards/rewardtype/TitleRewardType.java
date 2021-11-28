package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.TitlePart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class TitleRewardType extends BaseRewardType<TitlePart>
{
	public TitleRewardType(TitlePart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(TitlePart part, ServerLevel level, int x, int y, int z, Player player)
	{
		Scheduler.scheduleTask(new Task("Title Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < level.players().size(); ++i)
				{
					ServerPlayer entityPlayer = level.players().get(i);

					if(entityPlayer.equals(player))
					{
						RewardsUtil.setPlayerTitle(player, part.getType(), part.getMessage(), part.getFadeInTime(), part.getDisplayTime(), part.getFadeOutTime());
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - entityPlayer.getX(), 2) + Math.pow(y - entityPlayer.getY(), 2) + Math.pow(z - entityPlayer.getZ(), 2));
						if(dist <= part.getRange() || part.isServerWide())
							RewardsUtil.setPlayerTitle(player, part.getType(), part.getMessage(), part.getFadeInTime(), part.getDisplayTime(), part.getFadeOutTime());
					}
				}

			}
		});
	}
}
