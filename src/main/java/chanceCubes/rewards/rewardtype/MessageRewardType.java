package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

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
	public void trigger(final MessagePart message, final World world, final int x, final int y, final int z, final PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("Message Reward Delay", message.getDelay())
		{
			@Override
			public void callback()
			{
				for(int i = 0; i < world.getPlayers().size(); ++i)
				{
					PlayerEntity entityplayer = world.getPlayers().get(i);

					if(entityplayer.equals(player))
					{
						RewardsUtil.sendMessageToPlayer(entityplayer, message.getMessage());
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - entityplayer.getPosX(), 2) + Math.pow(y - entityplayer.getPosY(), 2) + Math.pow(z - entityplayer.getPosZ(), 2));
						if(dist <= message.getRange() || message.isServerWide())
							RewardsUtil.sendMessageToPlayer(entityplayer, message.getMessage());
					}
				}
			}
		});
	}
}
