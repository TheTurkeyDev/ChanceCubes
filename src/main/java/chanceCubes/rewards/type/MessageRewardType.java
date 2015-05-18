package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class MessageRewardType implements IRewardType
{

	private String[] messages;

	public MessageRewardType(String... messages)
	{
		this.messages = messages;
	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			if(messages != null)
				for(String message: messages)
				{
					player.addChatMessage(new ChatComponentText(message));
				}
		}
	}

}
