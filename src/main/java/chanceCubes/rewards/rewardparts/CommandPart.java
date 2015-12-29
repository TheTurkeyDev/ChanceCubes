package chanceCubes.rewards.rewardparts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommandPart
{
	public static String[] elements = new String[]{"command:S", "delay:I"};
	
	private String command;

	private int delay = 0;

	public CommandPart(String command)
	{
		this.command = command;
	}

	public String getRawCommand()
	{
		return command;
	}

	public int getDelay()
	{
		return delay;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	public String getParsedCommand(World world, int x, int y, int z, EntityPlayer player)
	{
		String parsedCommand = command;
		parsedCommand = parsedCommand.replace("%player", player.getCommandSenderName());
		parsedCommand = parsedCommand.replace("%x", "" + x);
		parsedCommand = parsedCommand.replace("%y", "" + y);
		parsedCommand = parsedCommand.replace("%z", "" + z);
		parsedCommand = parsedCommand.replace("%px", "" + player.posX);
		parsedCommand = parsedCommand.replace("%py", "" + player.posY);
		parsedCommand = parsedCommand.replace("%pz", "" + player.posZ);

		return parsedCommand;
	}
}
