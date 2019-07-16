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

					ServerPlayerEntity PlayerEntity = (ServerPlayerEntity) world.getPlayers().get(i);

					if(PlayerEntity.equals(player))
					{
						PlayerEntity.connection.sendPacket(spackettitle);
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - PlayerEntity.posX, 2) + Math.pow(y - PlayerEntity.posY, 2) + Math.pow(z - PlayerEntity.posZ, 2));
						if(dist <= part.getRange() || part.isServerWide())
							PlayerEntity.connection.sendPacket(spackettitle);
					}
				}

			}
		});
	}
}
