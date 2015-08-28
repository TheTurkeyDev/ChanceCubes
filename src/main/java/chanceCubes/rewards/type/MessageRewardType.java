package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class MessageRewardType extends BaseRewardType<String>
{
	public MessageRewardType(String... messages)
	{
		super(messages);
	}

	@Override
	public void trigger(String message, World world, int x, int y, int z, EntityPlayer player)
	{
		for(int i = 0; i < world.playerEntities.size(); ++i)
		{
			EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(i);

			double dist = Math.sqrt(Math.pow(x - entityplayer.posX, 2) + Math.pow(y - entityplayer.posY, 2) + Math.pow(z - entityplayer.posZ, 2));
			if(dist <= 16)
				entityplayer.addChatMessage(new ChatComponentText(message));
		}
	}
}
