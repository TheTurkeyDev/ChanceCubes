package chanceCubes.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CCubesClientCommands extends CCubesServerCommands
{
	public CCubesClientCommands()
	{
		super();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		super.execute(server, sender, args);
	}
}