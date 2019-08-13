package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class MessageRewardType extends BaseRewardType<MessagePart>
{
	public MessageRewardType(MessagePart... messages)
	{
		super(messages);
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
					PlayerEntity playerEntity = world.getPlayers().get(i);

					if(playerEntity.equals(player))
					{
						playerEntity.sendMessage(new StringTextComponent(message.getMessage()));
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - playerEntity.posX, 2) + Math.pow(y - playerEntity.posY, 2) + Math.pow(z - playerEntity.posZ, 2));
						if(dist <= message.getRange() || message.isServerWide())
							playerEntity.sendMessage(new StringTextComponent(message.getMessage()));
					}
				}
			}
		});
	}
}
