package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.TitlePart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.world.World;

public class TitleRewardType extends BaseRewardType<TitlePart>
{
	public TitleRewardType(TitlePart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(TitlePart part, World world, int x, int y, int z, EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Title Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				SPacketTitle spackettitle = new SPacketTitle(part.getType(), part.getMessage(), part.getFadeInTime(), part.getDisplayTime(), part.getFadeOutTime());
				for(int i = 0; i < world.playerEntities.size(); ++i)
				{
					if(!(world.playerEntities.get(i) instanceof EntityPlayerMP))
						continue;

					EntityPlayerMP entityplayer = (EntityPlayerMP) world.playerEntities.get(i);

					if(entityplayer.equals(player))
					{
						entityplayer.connection.sendPacket(spackettitle);
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - entityplayer.posX, 2) + Math.pow(y - entityplayer.posY, 2) + Math.pow(z - entityplayer.posZ, 2));
						if(dist <= part.getRange() || part.isServerWide())
							entityplayer.connection.sendPacket(spackettitle);
					}
				}

			}
		});
	}
}
