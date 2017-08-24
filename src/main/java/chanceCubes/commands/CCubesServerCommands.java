package chanceCubes.commands;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.config.ConfigLoader;
import chanceCubes.client.gui.SchematicCreationGui;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.util.NonReplaceableBlockOverride;
import chanceCubes.util.SchematicUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CCubesServerCommands extends CommandBase
{
	private List<String> aliases;
	private List<String> tab;

	public CCubesServerCommands()
	{
		aliases = new ArrayList<String>();
		aliases.add("Chancecubes");
		aliases.add("chancecubes");
		aliases.add("ChanceCube");
		aliases.add("Chancecube");
		aliases.add("chancecube");
		aliases.add("CCubes");

		tab = new ArrayList<String>();
		tab.add("reload");
		tab.add("version");
		tab.add("handNBT");
		tab.add("handID");
		tab.add("disableReward");
		tab.add("enableReward");
		tab.add("schematic");
		tab.add("rewardsInfo");
		tab.add("testRewards");
		tab.add("testCustomRewards");
	}

	@Override
	public String getName()
	{
		return "ChanceCubes";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "/ChanceCubes <reload/version/handNBT/enableReward/disableReward>";
	}

	@Override
	public List<String> getAliases()
	{
		return aliases;
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length > 0 && args[0].equalsIgnoreCase("reload"))
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					ChanceCubeRegistry.INSTANCE.ClearRewards();
					GiantCubeRegistry.INSTANCE.ClearRewards();
					ChanceCubeRegistry.loadDefaultRewards();
					GiantCubeRegistry.loadDefaultRewards();
					CustomRewardsLoader.instance.loadCustomRewards();
					CustomRewardsLoader.instance.fetchRemoteInfo();
					ChanceCubeRegistry.loadCustomUserRewards(server);
					ModHookUtil.loadCustomModRewards();
					ConfigLoader.loadConfigSettings(CCubesCore.cfg);
					CCubesSettings.nonReplaceableBlocks = CCubesSettings.nonReplaceableBlocksIMC;
					NonReplaceableBlockOverride.applyOverrides();
					sender.sendMessage(new TextComponentString("Rewards Reloaded"));
				}
			}).start();
		}
		else if(args.length > 0 && args[0].equalsIgnoreCase("version"))
		{
			sender.sendMessage(new TextComponentString("Chance Cubes Version " + CCubesCore.VERSION));
		}
		else if(args[0].equalsIgnoreCase("handNBT"))
		{
			if(sender instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) sender;
				if(player.inventory.getCurrentItem() != null)
				{
					NBTTagCompound nbt = player.inventory.getCurrentItem().getTagCompound();
					if(nbt != null)
					{
						sender.sendMessage(new TextComponentString(nbt.toString()));
					}
					else
					{
						sender.sendMessage(new TextComponentString("This item has no tag nbt data"));
					}
				}
			}
		}
		else if(args[0].equalsIgnoreCase("handID"))
		{
			if(sender instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) sender;
				ItemStack stack = player.inventory.getCurrentItem();
				if(stack != null)
				{
					ResourceLocation res = stack.getItem().getRegistryName();
					sender.sendMessage(new TextComponentString(res.getResourceDomain() + ":" + res.getResourcePath()));
					sender.sendMessage(new TextComponentString("meta: " + stack.getItemDamage()));
				}
			}
		}
		else if(args[0].equalsIgnoreCase("disableReward"))
		{
			if(args.length > 1)
			{
				if(ChanceCubeRegistry.INSTANCE.unregisterReward(args[1]))
				{
					sender.sendMessage(new TextComponentString(args[1] + " Has been temporarily disabled."));
				}
				else
				{
					sender.sendMessage(new TextComponentString(args[1] + " is either not currently enabled or is not a valid reward name."));
				}
			}
			else
			{
				sender.sendMessage(new TextComponentString("Try /chancecubes enableReward <Reward Name>"));
			}
		}
		else if(args[0].equalsIgnoreCase("enableReward"))
		{
			if(args.length > 1)
			{
				if(ChanceCubeRegistry.INSTANCE.enableReward(args[1]))
				{
					sender.sendMessage(new TextComponentString(args[1] + " Has been enabled."));
				}
				else
				{
					sender.sendMessage(new TextComponentString(args[1] + " is either not currently disabled or is not a valid reward name."));
				}
			}
			else
			{
				sender.sendMessage(new TextComponentString("Try /chancecubes disableReward <Reward Name>"));
			}
		}
		else if(args[0].equalsIgnoreCase("schematic") && args.length == 2)
		{
			if(Minecraft.getMinecraft().isSingleplayer())
			{
				if(sender instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) sender;
					if(player.capabilities.isCreativeMode)
					{
						if(args[1].equalsIgnoreCase("create"))
						{
							if(RenderEvent.isCreatingSchematic())
							{
								if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
									FMLCommonHandler.instance().showGuiScreen(new SchematicCreationGui(player));
								else
									sender.sendMessage(new TextComponentString("Please set both points before moving on!"));
							}
							else
							{
								RenderEvent.setCreatingSchematic(true);
							}
						}
						else if(args[1].equalsIgnoreCase("cancel"))
						{
							RenderEvent.setCreatingSchematic(false);
							SchematicUtil.selectionPoints[0] = null;
							SchematicUtil.selectionPoints[1] = null;
						}
					}
					else
					{
						sender.sendMessage(new TextComponentString("Sorry, you need to be in creative to use this command"));
					}
				}
			}
			else
			{
				sender.sendMessage(new TextComponentString("Sorry, but this command only works in single player"));
			}
		}
		else if(args[0].equalsIgnoreCase("rewardsInfo"))
		{
			sender.sendMessage(new TextComponentString("There are currently " + ChanceCubeRegistry.INSTANCE.getNumberOfLoadedRewards() + " rewards loaded and " + ChanceCubeRegistry.INSTANCE.getNumberOfDisabledRewards() + " rewards disabled"));
		}
		else if(args[0].equalsIgnoreCase("testRewards"))
		{
			CCubesSettings.testRewards = !CCubesSettings.testRewards;
			CCubesSettings.testingRewardIndex = 0;
			if(CCubesSettings.testRewards)
				sender.sendMessage(new TextComponentString("Reward testing is now enabled for all rewards!"));
			else
				sender.sendMessage(new TextComponentString("Reward testing is now disabled and normal randomness is back."));
		}
		else if(args[0].equalsIgnoreCase("testCustomRewards"))
		{
			CCubesSettings.testCustomRewards = !CCubesSettings.testCustomRewards;
			CCubesSettings.testingRewardIndex = 0;
			if(CCubesSettings.testCustomRewards)
				sender.sendMessage(new TextComponentString("Reward testing is now enabled for custom rewards!"));
			else
				sender.sendMessage(new TextComponentString("Reward testing is now disabled and normal randomness is back."));

		}
		else if(args[0].equalsIgnoreCase("test"))
		{
		}
		else
		{
			sender.sendMessage(new TextComponentString("Invalid arguments for the Chance Cubes command"));
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if(args.length == 0)
			return tab;
		return null;
	}
}