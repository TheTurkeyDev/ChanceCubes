package chanceCubes.rewards.type;

import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.util.CCubesCommandSender;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;

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
				String commandToRun = command.getParsedCommand(world, x, y, z, player);

				CCubesCommandSender sender = new CCubesCommandSender(player, new BlockPos(x, y, z));
				MinecraftServer server = world.getServer();
				WorldServer worldServer = server.getWorld(DimensionType.OVERWORLD);
				Boolean rule = worldServer.getGameRules().getBoolean("commandBlockOutput");
				worldServer.getGameRules().setOrCreateGameRule("commandBlockOutput", "false", server);
				CommandSource cs = new CommandSource(sender, sender.getPos(), 0, world, 2, player.getName().getString(), player.getDisplayName(), server, player);
				server.getCommandManager().handleCommand(cs, command.getParsedCommand(world, x, y, z, player));
				worldServer.getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString(), server);
			}
		});
	}
}
