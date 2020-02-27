package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

	private static CommandPart[] convertToCommandParts(String... messages)
	{
		CommandPart[] toReturn = new CommandPart[messages.length];
		for(int i = 0; i < messages.length; i++)
			toReturn[i] = new CommandPart(messages[i]);
		return toReturn;
	}

	@Override
	public void trigger(final CommandPart command, final World world, final int x, final int y, final int z, final PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("Command Reward Delay", command.getDelay())
		{
			@Override
			public void callback()
			{
				RewardsUtil.executeCommand(world, player, new BlockPos(x, y, z), command.getParsedCommand(world, x, y, z, player));
			}
		});
	}
}
