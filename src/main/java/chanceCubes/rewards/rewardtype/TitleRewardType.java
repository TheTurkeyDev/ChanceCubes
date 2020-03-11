package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.TitlePart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.world.World;

public class TitleRewardType extends BaseRewardType<TitlePart>
{
	public TitleRewardType(TitlePart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(TitlePart part, World world, int x, int y, int z, PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("Title Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				STitlePacket spackettitle = new STitlePacket(part.getType(), part.getMessage(), part.getFadeInTime(), part.getDisplayTime(), part.getFadeOutTime());
				for(int i = 0; i < world.getPlayers().size(); ++i)
				{
					if(!(world.getPlayers().get(i) instanceof ServerPlayerEntity))
						continue;

					ServerPlayerEntity entityplayer = (ServerPlayerEntity) world.getPlayers().get(i);

					if(entityplayer.equals(player))
					{
						entityplayer.connection.sendPacket(spackettitle);
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - entityplayer.getPosX(), 2) + Math.pow(y - entityplayer.getPosY(), 2) + Math.pow(z - entityplayer.getPosZ(), 2));
						if(dist <= part.getRange() || part.isServerWide())
							entityplayer.connection.sendPacket(spackettitle);
					}
				}

			}
		});
	}
}
