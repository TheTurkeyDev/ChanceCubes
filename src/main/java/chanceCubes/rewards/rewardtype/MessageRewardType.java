package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

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
	public void trigger(final MessagePart message, final ServerLevel level, final int x, final int y, final int z, final Player player)
	{
		Scheduler.scheduleTask(new Task("Message Reward Delay", message.getDelay())
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < level.players().size(); ++i)
				{
					Player entityplayer = level.players().get(i);

					if(entityplayer.equals(player))
					{
						RewardsUtil.sendMessageToPlayer(entityplayer, message.getMessage());
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - entityplayer.getX(), 2) + Math.pow(y - entityplayer.getY(), 2) + Math.pow(z - entityplayer.getZ(), 2));
						if(dist <= message.getRange() || message.isServerWide())
							RewardsUtil.sendMessageToPlayer(entityplayer, message.getMessage());
					}
				}
			}
		});
	}
}
