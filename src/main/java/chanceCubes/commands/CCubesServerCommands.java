package chanceCubes.commands;

import chanceCubes.CCubesCore;
import chanceCubes.client.gui.SchematicCreationGui;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomProfileLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.registry.player.PlayerRewardInfo;
import chanceCubes.rewards.DefaultGiantRewards;
import chanceCubes.rewards.DefaultRewards;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.NonreplaceableBlockOverride;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.SchematicUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CCubesServerCommands extends CommandBase
{
	private List<String> aliases;
	private List<String> tab;

	public CCubesServerCommands()
	{
		aliases = new ArrayList<>();
		aliases.add("Chancecubes");
		aliases.add("chancecubes");
		aliases.add("ChanceCube");
		aliases.add("Chancecube");
		aliases.add("chancecube");
		aliases.add("CCubes");

		tab = new ArrayList<>();
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
		tab.add("spawnGiantCube");
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
		if(args.length == 0)
			return;
		if(args[0].equalsIgnoreCase("reload"))
		{
			new Thread(() ->
			{
				GlobalCCRewardRegistry.DEFAULT.ClearRewards();
				GlobalCCRewardRegistry.GIANT.ClearRewards();
				GlobalProfileManager.clearProfiles();
				ConfigLoader.reload();
				DefaultRewards.loadDefaultRewards();
				DefaultGiantRewards.loadDefaultRewards();
				CustomRewardsLoader.instance.loadCustomRewards();
				GlobalCCRewardRegistry.loadCustomUserRewards(server);
				ModHookUtil.loadCustomModRewards();
				NonreplaceableBlockOverride.loadOverrides();
				GlobalProfileManager.initProfiles();
				CustomProfileLoader.instance.loadProfiles();
				GlobalProfileManager.updateProfilesForWorld(server.getEntityWorld());
				sender.sendMessage(new TextComponentString("Rewards Reloaded"));
			}).start();
		}
		else if(args[0].equalsIgnoreCase("version"))
		{
			sender.sendMessage(new TextComponentString("Chance Cubes Version " + CCubesCore.VERSION));
		}
		else if(args[0].equalsIgnoreCase("handNBT"))
		{
			if(sender instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) sender;
				NBTTagCompound nbt = player.inventory.getCurrentItem().getTagCompound();
				if(nbt != null)
					sender.sendMessage(new TextComponentString(nbt.toString()));
				else
					sender.sendMessage(new TextComponentString("This item has no tag nbt data"));
			}
		}
		else if(args[0].equalsIgnoreCase("handID"))
		{
			if(sender instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) sender;
				ItemStack stack = player.inventory.getCurrentItem();
				if(!stack.isEmpty())
				{
					ResourceLocation res = stack.getItem().getRegistryName();
					sender.sendMessage(new TextComponentString(res.getNamespace() + ":" + res.getPath()));
					sender.sendMessage(new TextComponentString("meta: " + stack.getItemDamage()));
				}
			}
		}
		else if(args[0].equalsIgnoreCase("disableReward"))
		{
			if(args.length > 2)
			{
				if(args[1].equalsIgnoreCase("global"))
				{
					//TODO: Giant Cube Rewards
					if(GlobalCCRewardRegistry.DEFAULT.disableReward(args[1]))
						sender.sendMessage(new TextComponentString(args[1] + " Has been temporarily disabled."));
					else
						sender.sendMessage(new TextComponentString(args[1] + " is either not currently enabled or is not a valid reward name."));
				}
				else
				{
					//TODO: per user disable
				}

			}
			else
			{
				sender.sendMessage(new TextComponentString("Try /chancecubes enableReward <global|playername> <Reward Name>"));
			}
		}
		else if(args[0].equalsIgnoreCase("enableReward"))
		{
			if(args.length > 2)
			{
				if(args[1].equalsIgnoreCase("global"))
				{
					//TODO: Giant Cube Rewards
					if(GlobalCCRewardRegistry.DEFAULT.enableReward(args[1]))
						sender.sendMessage(new TextComponentString(args[1] + " Has been enabled."));
					else
						sender.sendMessage(new TextComponentString(args[1] + " is either not currently disabled or is not a valid reward name."));
				}
				else
				{
					//TODO: per user enable
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
			int defaultEnabled = 0;
			int giantEnabled = 0;
			if(sender instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) sender;
				List<PlayerRewardInfo> defaultrewards = GlobalCCRewardRegistry.DEFAULT.getPlayerRewardRegistry(player.getUniqueID().toString()).getPlayersRewards();
				List<PlayerRewardInfo> giantrewards = GlobalCCRewardRegistry.GIANT.getPlayerRewardRegistry(player.getUniqueID().toString()).getPlayersRewards();
				defaultEnabled = defaultrewards.size();
				giantEnabled = giantrewards.size();
				if(args.length > 1 && args[1].equalsIgnoreCase("list"))
				{
					if(args.length > 2 && args[2].equalsIgnoreCase("default"))
					{
						sender.sendMessage(new TextComponentString("===DEFAULT REWARDS==="));
						for(PlayerRewardInfo reward : defaultrewards)
							sender.sendMessage(new TextComponentString(reward.reward.getName()));
					}
					else if(args.length > 2 && args[2].equalsIgnoreCase("giant"))
					{
						sender.sendMessage(new TextComponentString("===GIANT REWARDS==="));
						for(PlayerRewardInfo reward : giantrewards)
							sender.sendMessage(new TextComponentString(reward.reward.getName()));
					}
					else if(args.length > 2 && args[2].equalsIgnoreCase("defaultall"))
					{
						sender.sendMessage(new TextComponentString("===DEFAULT REWARDS==="));
						for(String reward : GlobalCCRewardRegistry.DEFAULT.getRewardNames())
							sender.sendMessage(new TextComponentString(reward));
					}
					else if(args.length > 2 && args[2].equalsIgnoreCase("giantall"))
					{
						sender.sendMessage(new TextComponentString("===GIANT REWARDS==="));
						for(String reward : GlobalCCRewardRegistry.GIANT.getRewardNames())
							sender.sendMessage(new TextComponentString(reward));
					}
					else if(args.length > 2 && args[2].equalsIgnoreCase("defaultdisabled"))
					{
						sender.sendMessage(new TextComponentString("===DEFAULT REWARDS DISABLED==="));
						List<String> playerRewards = new ArrayList<>();
						for(PlayerRewardInfo reward : defaultrewards)
							playerRewards.add(reward.reward.getName());
						for(String reward : GlobalCCRewardRegistry.DEFAULT.getRewardNames())
							if(!playerRewards.contains(reward))
								sender.sendMessage(new TextComponentString(reward));
					}
					else if(args.length > 2 && args[2].equalsIgnoreCase("giantdisabled"))
					{
						sender.sendMessage(new TextComponentString("===GIANT REWARDS DISABLED==="));
						List<String> playerRewards = new ArrayList<>();
						for(PlayerRewardInfo reward : giantrewards)
							playerRewards.add(reward.reward.getName());
						for(String reward : GlobalCCRewardRegistry.GIANT.getRewardNames())
							if(!playerRewards.contains(reward))
								sender.sendMessage(new TextComponentString(reward));
					}
				}
			}

			sender.sendMessage(new TextComponentString("There are currently " + GlobalCCRewardRegistry.DEFAULT.getNumberOfLoadedRewards() + " regular rewards loaded and you have " + defaultEnabled + " rewards enabled"));
			sender.sendMessage(new TextComponentString("There are currently " + GlobalCCRewardRegistry.GIANT.getNumberOfLoadedRewards() + " giant rewards loaded and you have " + giantEnabled + " rewards enabled"));
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
		else if(args[0].equalsIgnoreCase("spawnGiantCube"))
		{
			if(args.length < 4)
			{
				sender.sendMessage(new TextComponentString("Invalid arguments! Try /chancecubes spawnGiantCube <x> <y> <z> (NOTE: You may use ~ with offsets)"));
				return;
			}

			BlockPos pos = parseBlockPos(sender, args, 1, false);

			World world = sender.getEntityWorld();

			if(RewardsUtil.isBlockUnbreakable(world, pos.add(0, 0, 0)) && CCubesSettings.nonReplaceableBlocks.contains(world.getBlockState(pos.add(0, 0, 0))))
				return;

			GiantCubeUtil.setupStructure(pos.add(-1, -1, -1), world, true);

			world.playSound(null, pos, CCubesSounds.GIANT_CUBE_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
		else if(args[0].equalsIgnoreCase("enableProfile"))
		{
			//TODO: Enable again
			sender.sendMessage(new TextComponentString("Temporarily removed!"));
//			if(args.length < 2)
//			{
//				sender.sendMessage(new TextComponentString("Invalid arguments! Try /chancecubes enableProfile <profile_id>"));
//				return;
//			}
//
//			IProfile profile = GlobalProfileManager.getProfileFromID(args[1]);
//			if(profile == null)
//				profile = GlobalProfileManager.getProfilefromName(args[1]);
//
//			if(profile != null)
//				GlobalProfileManager.enableProfile(profile);
//			else
//				sender.sendMessage(new TextComponentString(args[1] + " is not a valid profile id!"));
		}
		else if(args[0].equalsIgnoreCase("disableProfile"))
		{
			//TODO: Enable again
			sender.sendMessage(new TextComponentString("Temporarily removed!"));
//			if(args.length < 2)
//			{
//				sender.sendMessage(new TextComponentString("Invalid arguments! Try /chancecubes disableProfile <profile_id>"));
//				return;
//			}
//
//			IProfile profile = ProfileManager.getProfileFromID(args[1]);
//			if(profile == null)
//				profile = ProfileManager.getProfilefromName(args[1]);
//
//			if(profile != null)
//				ProfileManager.disableProfile(profile);
//			else
//				sender.sendMessage(new TextComponentString(args[1] + " is not a valid profile id!"));
		}
		else
		{
			sender.sendMessage(new TextComponentString("Invalid arguments for the Chance Cubes command"));
		}
	}

	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		if(sender instanceof EntityPlayer)
		{
			if(server.isSinglePlayer())
			{
				return true;
			}

			GameProfile profile = ((EntityPlayer) sender).getGameProfile();
			if(server.getPlayerList().canSendCommands(profile))
			{
				UserListOpsEntry userlistopsentry = server.getPlayerList().getOppedPlayers().getEntry(profile);
				return userlistopsentry != null && userlistopsentry.getPermissionLevel() >= 2;
			}
			else
			{
				return false;
			}
		}
		return false;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if(args.length == 1)
			return tab;
		else if(args.length > 1)
		{
			//TODO: make enable and disable smarter
			if(args[0].equalsIgnoreCase("disableReward"))
				return new ArrayList<>(GlobalCCRewardRegistry.DEFAULT.getRewardNames());
			else if(args[0].equalsIgnoreCase("enableReward"))
				return new ArrayList<>(GlobalCCRewardRegistry.DEFAULT.getRewardNames());
			else if(args[0].equalsIgnoreCase("schematic") && args.length == 2)
				return Arrays.asList("create", "cancel");
		}
		return Collections.emptyList();
	}
}