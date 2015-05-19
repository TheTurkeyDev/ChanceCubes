package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class MessageRewardType extends MultipleRewardType<String>
{
    public MessageRewardType(String... messages)
    {
        super(messages);
    }

    @Override
    public void trigger(String message, World world, int x, int y, int z, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            player.addChatMessage(new ChatComponentText(message));
        }
    }
}
