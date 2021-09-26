package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class CommandRewardType extends BaseRewardType<CommandPart>
{

	public CommandRewardType(CommandPart... commands)
	{
		super(commands);
	}

	public CommandRewardType(String... commands)
	{
		super(convertToCommandParts(commands));
	}

	private static CommandPart[] convertToCommandParts(String... commands)
	{
		CommandPart[] toReturn = new CommandPart[commands.length];
		for(int i = 0; i < commands.length; i++)
			toReturn[i] = new CommandPart(commands[i]);
		return toReturn;
	}

	@Override
	public void trigger(ServerLevel world, int x, int y, int z, Player player)
	{
		CommandPart.randUUIDs.clear();
		super.trigger(world, x, y, z, player);
	}

	@Override
	public void trigger(final CommandPart command, final ServerLevel level, final int x, final int y, final int z, final Player player)
	{
		Scheduler.scheduleTask(new Task("Command Reward Delay", command.getDelay())
		{
			@Override
			public void callback()
			{
				int copies = command.getCopies().getIntValue() + 1;
				String commandStr = command.getParsedCommand(level, x, y, z, player);
				boolean relativetoPlayer = command.isRelativeToPlayer().getBoolValue();
				for(int i = 0; i < copies; i++)
				{
					if(command.areCopiesSoft().getBoolValue())
					{
						commandStr = command.getParsedCommand(level, x, y, z, player);
						relativetoPlayer = command.isRelativeToPlayer().getBoolValue();
					}

					BlockPos executingPos;
					if(relativetoPlayer)
						executingPos = player.getOnPos();
					else
						executingPos = new BlockPos(x, y, z);

					RewardsUtil.executeCommand(level, player, executingPos, commandStr);
				}
			}
		});
	}
}
