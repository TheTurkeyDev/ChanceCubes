package chanceCubes.commands;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

public class CCubesCommands implements ICommand
{
	private List<String> aliases;
	List<String> tab;

	public CCubesCommands()
	{
		this.aliases = new ArrayList<String>();
		this.aliases.add("Chancecubes");
		this.aliases.add("chancecubes");
		this.aliases.add("ChanceCube");
		this.aliases.add("Chancecube");
		this.aliases.add("chancecube");
		this.aliases.add("CCubes");

		tab = new ArrayList<String>();
		tab.add("reload");
		tab.add("version");
		tab.add("handNBT");
	}

	@Override
	public String getCommandName()
	{
		return "ChanceCubes";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/ChanceCubes <reload/version/handNBT/enableReward/disableReward>";
	}

	@Override
	public List<String> getCommandAliases()
	{
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		if(astring.length > 0 && astring[0].equalsIgnoreCase("reload"))
		{
			ChanceCubeRegistry.INSTANCE.ClearRewards();
			GiantCubeRegistry.INSTANCE.ClearRewards();
			ChanceCubeRegistry.loadDefaultRewards();
			GiantCubeRegistry.loadDefaultRewards();
			CustomRewardsLoader.instance.loadCustomRewards();
			CustomRewardsLoader.instance.loadHolidayRewards();
			ChanceCubeRegistry.loadCustomUserRewards();
			ModHookUtil.loadCustomModRewards();
			icommandsender.addChatMessage(new ChatComponentText("RewardsReloaded"));
		}
		else if(astring.length > 0 && astring[0].equalsIgnoreCase("version"))
		{
			icommandsender.addChatMessage(new ChatComponentText("Chance Cubes Version " + CCubesCore.VERSION));
		}
		else if(astring[0].equalsIgnoreCase("handNBT"))
		{
			if(icommandsender instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) icommandsender;
				if(player.inventory.getCurrentItem() != null)
				{
					NBTTagCompound nbt = player.inventory.getCurrentItem().stackTagCompound;
					if(nbt != null)
						icommandsender.addChatMessage(new ChatComponentText(nbt.toString()));
					else
						icommandsender.addChatMessage(new ChatComponentText("This item has not tag nbt data"));
				}
			}
		}
		else if(astring[0].equalsIgnoreCase("handID"))
		{
			if(icommandsender instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) icommandsender;
				if(player.inventory.getCurrentItem() != null)
					icommandsender.addChatMessage(new ChatComponentText(player.inventory.getCurrentItem().getUnlocalizedName()));
			}
		}
		else if(astring[0].equalsIgnoreCase("disableReward"))
		{
			if(astring.length > 1)
			{
				if(ChanceCubeRegistry.INSTANCE.unregisterReward(astring[1]))
					icommandsender.addChatMessage(new ChatComponentText(astring[1] + " Has been temporarily disabled."));
				else
					icommandsender.addChatMessage(new ChatComponentText(astring[1] + " is either not currently enabled or is not a valid reward name."));
			}
			else
			{
				icommandsender.addChatMessage(new ChatComponentText("Try /chancecubes enableReward <Reward Name>"));
			}
		}
		else if(astring[0].equalsIgnoreCase("enableReward"))
		{
			if(astring.length > 1)
			{
				if(ChanceCubeRegistry.INSTANCE.enableReward(astring[1]))
					icommandsender.addChatMessage(new ChatComponentText(astring[1] + " Has been enabled."));
				else
					icommandsender.addChatMessage(new ChatComponentText(astring[1] + " is either not currently disabled or is not a valid reward name."));
			}
			else
			{
				icommandsender.addChatMessage(new ChatComponentText("Try /chancecubes disableReward <Reward Name>"));
			}
		}
		else
		{
			icommandsender.addChatMessage(new ChatComponentText("Invalid arguments for the Chance Cubes command"));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
	{
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
	{
		if(astring.length == 0)
			return tab;
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}

	@Override
	public int compareTo(Object o)
	{
		return 0;
	}
}