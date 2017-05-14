package chanceCubes.commands;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.util.Location3I;
import chanceCubes.util.SchematicUtil;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CCubesServerCommands implements ICommand
{
	private List<String> aliases;
	List<String> tab;

	public CCubesServerCommands()
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
			CustomRewardsLoader.instance.fetchRemoteInfo();
			ChanceCubeRegistry.loadCustomUserRewards();
			ModHookUtil.loadCustomModRewards();
			icommandsender.addChatMessage(new ChatComponentText("Rewards Reloaded"));
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
						icommandsender.addChatMessage(new ChatComponentText("This item has no tag nbt data"));
				}
			}
		}
		else if(astring[0].equalsIgnoreCase("handID"))
		{
			if(icommandsender instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) icommandsender;
				ItemStack stack = player.inventory.getCurrentItem();
				if(stack != null)
				{
					String info = GameData.getItemRegistry().getNameForObject(stack.getItem());
					if(info == null || info == "")
						info = GameData.getBlockRegistry().getNameForObject(stack.getItem());
					icommandsender.addChatMessage(new ChatComponentText(info));
					icommandsender.addChatMessage(new ChatComponentText("meta: " + stack.getItemDamage()));
				}
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
		if(astring[0].equalsIgnoreCase("schematic"))
		{
			if(Minecraft.getMinecraft().isSingleplayer())
			{
				if(icommandsender instanceof EntityPlayer)
				{
					World world = Minecraft.getMinecraft().getIntegratedServer().getEntityWorld();
					EntityPlayer player = (EntityPlayer) icommandsender;
					if(player.capabilities.isCreativeMode)
					{
						if(astring.length >= 3)
						{
							if(astring[1].equalsIgnoreCase("setPoint"))
							{
								if(astring[2].equalsIgnoreCase("1"))
								{
									SchematicUtil.selectionPoints[0] = new Location3I((int) player.posX, (int) player.posY - 1, (int) player.posZ);
									icommandsender.addChatMessage(new ChatComponentText("Point 1 set"));
								}
								if(astring[2].equalsIgnoreCase("2"))
								{
									SchematicUtil.selectionPoints[1] = new Location3I((int) player.posX, (int) player.posY - 1, (int) player.posZ);
									icommandsender.addChatMessage(new ChatComponentText("Point 2 set"));
								}
							}
							else if(astring[1].equalsIgnoreCase("create"))
							{
								if(SchematicUtil.selectionPoints[0] == null || SchematicUtil.selectionPoints[1] == null)
								{
									icommandsender.addChatMessage(new ChatComponentText("Both points are not set!"));
									return;
								}
								SchematicUtil.createCustomSchematic(world, SchematicUtil.selectionPoints[0], SchematicUtil.selectionPoints[1], astring[2].endsWith(".ccs") ? astring[2] : astring[2] + ".ccs");
								icommandsender.addChatMessage(new ChatComponentText("Schematic file named " + (astring[2].endsWith(".ccs") ? astring[2] : astring[2] + ".ccs") + " created!"));
								SchematicUtil.selectionPoints[0] = null;
								SchematicUtil.selectionPoints[1] = null;
							}
						}
						else
						{
							icommandsender.addChatMessage(new ChatComponentText("invalid arguments"));
						}
					}
					else
					{
						icommandsender.addChatMessage(new ChatComponentText("Sorry, you need to be in creative to use this command"));
					}
				}
			}
			else
			{
				icommandsender.addChatMessage(new ChatComponentText("Sorry, but this command only works in single player"));
			}
		}
		else if(astring[0].equalsIgnoreCase("test"))
		{

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
		System.out.println("TESTING: " + o);
		return 0;
	}
}