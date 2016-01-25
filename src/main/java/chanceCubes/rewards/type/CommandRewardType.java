package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.util.CCubesCommandSender;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class CommandRewardType extends BaseRewardType<CommandPart>
{

	public CommandRewardType(CommandPart... commands)
	{
		super(commands);
	}

	@Override
	public void trigger(final CommandPart command, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		if(command.getDelay() != 0)
		{
			Task task = new Task("Command Reward Delay", command.getDelay())
			{
				@Override
				public void callback()
				{
					triggerCommand(command, world, x, y, z, player);
				}
			};
			Scheduler.scheduleTask(task);
		}
		else
		{
			triggerCommand(command, world, x, y, z, player);
		}
	}
	
	public void triggerCommand(CommandPart command, World world, int x, int y, int z, EntityPlayer player)
	{
		String commandToRun = command.getParsedCommand(world, x, y, z, player);

		CCubesCommandSender sender = new CCubesCommandSender(player, new BlockPos(x, y, z));

		Boolean rule = MinecraftServer.getServer().worldServers[0].getGameRules().getBoolean("commandBlockOutput");
		MinecraftServer.getServer().worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
		MinecraftServer.getServer().getCommandManager().executeCommand(sender, commandToRun);
		MinecraftServer.getServer().worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString());
	}
}
