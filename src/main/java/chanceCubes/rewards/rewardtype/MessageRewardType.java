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
					PlayerEntity PlayerEntity = (PlayerEntity) world.getPlayers().get(i);

					if(PlayerEntity.equals(player))
					{
						PlayerEntity.sendMessage(new StringTextComponent(message.getMessage()));
					}
					else
					{
						double dist = Math.sqrt(Math.pow(x - PlayerEntity.posX, 2) + Math.pow(y - PlayerEntity.posY, 2) + Math.pow(z - PlayerEntity.posZ, 2));
						if(dist <= message.getRange() || message.isServerWide())
							PlayerEntity.sendMessage(new StringTextComponent(message.getMessage()));
					}
				}
			}
		});
	}
}
