package chanceCubes.commands;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.util.SchematicUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length > 0 && args[0].equalsIgnoreCase("reload"))
		{
			ChanceCubeRegistry.INSTANCE.ClearRewards();
			GiantCubeRegistry.INSTANCE.ClearRewards();
			ChanceCubeRegistry.loadDefaultRewards();
			GiantCubeRegistry.loadDefaultRewards();
			CustomRewardsLoader.instance.loadCustomRewards();
			CustomRewardsLoader.instance.loadHolidayRewards();
			ChanceCubeRegistry.loadCustomUserRewards(server);
			ModHookUtil.loadCustomModRewards();
			sender.addChatMessage(new TextComponentString("Rewards Reloaded"));
		}
		else if(args.length > 0 && args[0].equalsIgnoreCase("version"))
		{
			sender.addChatMessage(new TextComponentString("Chance Cubes Version " + CCubesCore.VERSION));
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
						sender.addChatMessage(new TextComponentString(nbt.toString()));
					else
						sender.addChatMessage(new TextComponentString("This item has no tag nbt data"));
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
					sender.addChatMessage(new TextComponentString(res.getResourceDomain() + ":" + res.getResourcePath()));
					sender.addChatMessage(new TextComponentString("meta: " + stack.getItemDamage()));
				}
			}
		}
		else if(args[0].equalsIgnoreCase("disableReward"))
		{
			if(args.length > 1)
			{
				if(ChanceCubeRegistry.INSTANCE.unregisterReward(args[1]))
					sender.addChatMessage(new TextComponentString(args[1] + " Has been temporarily disabled."));
				else
					sender.addChatMessage(new TextComponentString(args[1] + " is either not currently enabled or is not a valid reward name."));
			}
			else
			{
				sender.addChatMessage(new TextComponentString("Try /chancecubes enableReward <Reward Name>"));
			}
		}
		else if(args[0].equalsIgnoreCase("enableReward"))
		{
			if(args.length > 1)
			{
				if(ChanceCubeRegistry.INSTANCE.enableReward(args[1]))
					sender.addChatMessage(new TextComponentString(args[1] + " Has been enabled."));
				else
					sender.addChatMessage(new TextComponentString(args[1] + " is either not currently disabled or is not a valid reward name."));
			}
			else
			{
				sender.addChatMessage(new TextComponentString("Try /chancecubes disableReward <Reward Name>"));
			}
		}
		else if(args[0].equalsIgnoreCase("schematic"))
		{
			if(Minecraft.getMinecraft().isSingleplayer())
			{
				if(sender instanceof EntityPlayer)
				{
					World world = Minecraft.getMinecraft().getIntegratedServer().getEntityWorld();
					EntityPlayer player = (EntityPlayer) sender;
					if(player.capabilities.isCreativeMode)
					{
						if(args.length >= 3)
						{
							if(args[1].equalsIgnoreCase("setPoint"))
							{
								if(args[2].equalsIgnoreCase("1"))
								{
									SchematicUtil.selectionPoints[0] = new BlockPos((int) player.posX, (int) player.posY - 1, (int) player.posZ);
									sender.addChatMessage(new TextComponentString("Point 1 set"));
								}
								if(args[2].equalsIgnoreCase("2"))
								{
									SchematicUtil.selectionPoints[1] = new BlockPos((int) player.posX, (int) player.posY - 1, (int) player.posZ);
									sender.addChatMessage(new TextComponentString("Point 2 set"));
								}
							}
							else if(args[1].equalsIgnoreCase("create"))
							{
								if(SchematicUtil.selectionPoints[0] == null || SchematicUtil.selectionPoints[1] == null)
								{
									sender.addChatMessage(new TextComponentString("Both points are not set!"));
									return;
								}
								SchematicUtil.createCustomSchematic(world, SchematicUtil.selectionPoints[0], SchematicUtil.selectionPoints[1], args[2].endsWith(".ccs") ? args[2] : args[2] + ".ccs");
								sender.addChatMessage(new TextComponentString("Schematic file named " + (args[2].endsWith(".ccs") ? args[2] : args[2] + ".ccs") + " created!"));
								SchematicUtil.selectionPoints[0] = null;
								SchematicUtil.selectionPoints[1] = null;
							}
						}
						else
						{
							sender.addChatMessage(new TextComponentString("invalid arguments"));
						}
					}
					else
					{
						sender.addChatMessage(new TextComponentString("Sorry, you need to be in creative to use this command"));
					}
				}
			}
			else
			{
				sender.addChatMessage(new TextComponentString("Sorry, but this command only works in single player"));
			}
		}
		else if(args[0].equalsIgnoreCase("test"))
		{
			System.out.println(sender.getEntityWorld().getBlockState(new BlockPos(sender.getPosition().add(0, -1, 0))));
		}
		else
		{
			sender.addChatMessage(new TextComponentString("Invalid arguments for the Chance Cubes command"));
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if(args.length == 0)
			return tab;
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