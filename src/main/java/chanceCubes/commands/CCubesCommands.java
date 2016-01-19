package chanceCubes.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import chanceCubes.CCubesCore;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;

public class CCubesCommands implements ICommand
{
	private List<String> aliases;

	public CCubesCommands()
	{
		this.aliases = new ArrayList<String>();
		this.aliases.add("Chancecubes");
		this.aliases.add("chancecubes");
		this.aliases.add("ChanceCube");
		this.aliases.add("Chancecube");
		this.aliases.add("chancecube");
		this.aliases.add("CCubes");
	}

	@Override
	public String getCommandName()
	{
		return "ChanceCubes";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/ChanceCubes <reload/version>";
	}

	@Override
	public List<String> getCommandAliases()
	{
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length > 0 && args[0].equalsIgnoreCase("reload"))
		{
			ChanceCubeRegistry.INSTANCE.ClearRewards();
			GiantCubeRegistry.INSTANCE.ClearRewards();
			ChanceCubeRegistry.loadDefaultRewards();
			GiantCubeRegistry.loadDefaultRewards();
			CustomRewardsLoader.instance.loadCustomRewards();
			CustomRewardsLoader.instance.loadHolidayRewards();
			ChanceCubeRegistry.loadCustomUserRewards();
			ModHookUtil.loadCustomModRewards();
			sender.addChatMessage(new ChatComponentText("RewardsReloaded"));
		}
		else if(args.length > 0 && args[0].equalsIgnoreCase("version"))
		{
			sender.addChatMessage(new ChatComponentText("Chance Cubes Version " + CCubesCore.VERSION));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}

	@Override
	public int compareTo(ICommand o)
	{
		return 0;
	}
}