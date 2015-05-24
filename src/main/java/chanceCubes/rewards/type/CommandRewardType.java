package chanceCubes.rewards.type;

import chanceCubes.util.CCubesCommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommandRewardType extends BaseRewardType<String>
{

	public CommandRewardType(String... commands)
    {
        super(commands);
    }

    @Override
    public void trigger(String command, World world, int x, int y, int z, EntityPlayer player)
    {
        if (!world.isRemote)
        {
        	command = command.replace("%player", player.getCommandSenderName());
        	CCubesCommandSender sender = new CCubesCommandSender(player, x, y, z);
        	MinecraftServer.getServer().getCommandManager().executeCommand(sender, command);
        }
    }
}
