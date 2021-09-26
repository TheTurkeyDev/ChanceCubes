package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.TitlePart;
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
				STitlePacket spackettitle = new STitlePacket(part.getType(), part.getMessage(), part.getFadeInTime(), part.getDisplayTime(), part.getFadeOutTime());
				for(int i = 0; i < level.players().size(); ++i)
				{
					ServerPlayer entityplayer = level.players().get(i);

					if(entityplayer.equals(player))
					{
						entityplayer.connection.send(spackettitle);
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - entityplayer.getX(), 2) + Math.pow(y - entityplayer.getY(), 2) + Math.pow(z - entityplayer.getZ(), 2));
						if(dist <= part.getRange() || part.isServerWide())
							entityplayer.connection.send(spackettitle);
					}
				}

			}
		});
	}
}
