package chanceCubes.rewards.type;

import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class MessageRewardType extends BaseRewardType<MessagePart>
{
	public MessageRewardType(MessagePart... messages)
	{
		super(messages);
	}

	@Override
	public void trigger(final MessagePart message, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Message Reward Delay", message.getDelay())
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < world.playerEntities.size(); ++i)
				{
					EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(i);

					if(entityplayer.equals(player))
					{
						entityplayer.addChatMessage(new TextComponentString(message.getMessage()));
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - entityplayer.posX, 2) + Math.pow(y - entityplayer.posY, 2) + Math.pow(z - entityplayer.posZ, 2));
						if(dist <= message.getRange() || message.isServerWide())
							entityplayer.addChatMessage(new TextComponentString(message.getMessage()));
					}
				}
			}
		});
	}
}
