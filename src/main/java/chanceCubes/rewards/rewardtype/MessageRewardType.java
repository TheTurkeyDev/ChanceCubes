package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Arrays;

public class MessageRewardType extends BaseRewardType<MessagePart>
{
	public MessageRewardType(MessagePart... messages)
	{
		super(messages);
	}

	public MessageRewardType(String... messages)
	{
		super(convertToMessageParts(messages));
	}

	private static MessagePart[] convertToMessageParts(String... messages)
	{
		MessagePart[] toReturn = new MessagePart[messages.length];
		for(int i = 0; i < messages.length; i++)
			toReturn[i] = new MessagePart(messages[i]);
		return toReturn;
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
						entityplayer.sendMessage(new TextComponentString(message.getMessage()));
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - entityplayer.posX, 2) + Math.pow(y - entityplayer.posY, 2) + Math.pow(z - entityplayer.posZ, 2));
						if(dist <= message.getRange() || message.isServerWide())
							entityplayer.sendMessage(new TextComponentString(message.getMessage()));
					}
				}
			}
		});
	}
}
