package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CommandRewardType extends BaseRewardType<CommandPart>
{

	public CommandRewardType(CommandPart... commands)
	{
		super(commands);
	}

	@Override
	public void trigger(final CommandPart command, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Command Reward Delay", command.getDelay())
		{
			@Override
			public void callback()
			{
				RewardsUtil.executeCommand(world, player, new Vec3d(x, y, z), command.getParsedCommand(world, x, y, z, player));
			}
		});
	}
}
