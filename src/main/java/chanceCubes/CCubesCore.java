package chanceCubes;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.ClientProxy;
import chanceCubes.commands.CCubesServerCommands;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomProfileLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.listeners.PlayerConnectListener;
import chanceCubes.listeners.TickListener;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.triggerHooks.GameStageTriggerHooks;
import chanceCubes.profiles.triggerHooks.VanillaTriggerHooks;
import chanceCubes.rewards.DefaultGiantRewards;
import chanceCubes.rewards.DefaultRewards;
import chanceCubes.util.NonreplaceableBlockOverride;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(CCubesCore.MODID)
public class CCubesCore
{
	public static final String MODID = "chancecubes";

	public static ItemGroup modTab = new ItemGroup(MODID)
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(CCubesBlocks.CHANCE_CUBE);
		}
	};
	public static final Logger logger = LogManager.getLogger(MODID);

	public CCubesCore()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonStart);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStart);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onIMCMessage);
		MinecraftForge.EVENT_BUS.register(this);
		ConfigLoader.initParentFolder();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigLoader.configSpec, "chancecubes" + File.separatorChar + "chancecubes-server.toml");

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
		{
			new ClientProxy();
			//ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> ProfileGui::openGui);
		});
	}

	@SubscribeEvent
	public void commonStart(FMLCommonSetupEvent event)
	{
		CCubesPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new PlayerConnectListener());
		MinecraftForge.EVENT_BUS.register(new TickListener());
		//MinecraftForge.EVENT_BUS.register(new WorldGen());
		MinecraftForge.EVENT_BUS.register(new VanillaTriggerHooks());
		if(ModList.get().isLoaded("gamestages"))
		{
			MinecraftForge.EVENT_BUS.register(new GameStageTriggerHooks());
			CCubesCore.logger.log(Level.INFO, "Loaded GameStages support!");
		}
	}

	@SubscribeEvent
	public void lootTableLoad(LootTableLoadEvent event)
	{
		if(CCubesSettings.chestLoot.get() && event.getName().getPath().contains("chests"))
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(CCubesCore.MODID, "blocks/chance_cube_chest"))).build());
	}

	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event)
	{
		// ConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile());

		CCubesSettings.backupNRB.add(Blocks.BEDROCK.getDefaultState());
		CCubesSettings.backupNRB.add(Blocks.OBSIDIAN.getDefaultState());
		DefaultRewards.loadDefaultRewards();
		DefaultGiantRewards.loadDefaultRewards();
		CustomRewardsLoader.instance.loadCustomRewards();
		GlobalProfileManager.initProfiles();
		CustomProfileLoader.instance.loadProfiles();
		NonreplaceableBlockOverride.loadOverrides();
		// ConfigLoader.config.save();

		// See ForgeCommand
		new CCubesServerCommands(event.getCommandDispatcher());

		logger.log(Level.INFO, "Death and destruction prepared! (And Cookies. Cookies were also prepared.)");
	}

	@SubscribeEvent
	public void onServerStart(FMLServerStartedEvent event)
	{
		// TODO: 1.16: this is getOverworld
		ServerWorld world = event.getServer().func_241755_D_();
		if(!GlobalProfileManager.isWorldProfilesLoaded())
			GlobalProfileManager.updateProfilesForWorld(world);
	}

	@SubscribeEvent
	public void onServerStop(FMLServerStoppedEvent event)
	{
		if(GlobalProfileManager.isWorldProfilesLoaded())
			GlobalProfileManager.unloadProfilesForWorld();
	}

	@SubscribeEvent
	public void onIMCMessage(InterModProcessEvent e)
	{
		e.getIMCStream().forEach((message) ->
		{
			/*Logger logger = LogManager.getLogger(MODID);
			for(IMCMessage message : e.getMessages())
			{
				if(message.key.equalsIgnoreCase("add-nonreplaceable") && message.isItemStackMessage())
				{
					ItemStack stack = message.getItemStackValue();
					Block block = Block.getBlockFromItem(stack.getItem());
					IBlockState state = RewardsUtil.getBlockStateFromBlockMeta(block, stack.getItemDamage());
					CCubesSettings.nonReplaceableBlocksIMC.add(state);
					logger.info(message.getSender() + " has added the blockstate of \"" + state.toString() + "\" that Chance Cubes rewards will no longer replace.");
				}
			}*/
		});
	}
}
