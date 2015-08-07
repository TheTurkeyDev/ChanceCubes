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
		command = command.replace("%player", player.getCommandSenderName());
		command = command.replace("%x", "" + x);
		command = command.replace("%y", "" + y);
		command = command.replace("%z", "" + z);
		command = command.replace("%px", "" + player.posX);
		command = command.replace("%py", "" + player.posY);
		command = command.replace("%pz", "" + player.posZ);

		CCubesCommandSender sender = new CCubesCommandSender(player, x, y, z);

		Boolean rule = MinecraftServer.getServer().worldServers[0].getGameRules().getGameRuleBooleanValue("commandBlockOutput");
		MinecraftServer.getServer().worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
		MinecraftServer.getServer().getCommandManager().executeCommand(sender, command);
		MinecraftServer.getServer().worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString());
	}
}
