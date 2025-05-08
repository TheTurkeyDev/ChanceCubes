package chanceCubes;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.ClientHelper;
import chanceCubes.commands.CCubesRewardArguments;
import chanceCubes.commands.CCubesServerCommands;
import chanceCubes.components.CCubesDataComponents;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.containers.CCubesMenus;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.PlayerConnectListener;
import chanceCubes.listeners.TickListener;
import chanceCubes.listeners.WorldGen;
import chanceCubes.modifier.CCubesModifiers;
import chanceCubes.network.CCubesNetwork;
import chanceCubes.rewards.DefaultGiantRewards;
import chanceCubes.rewards.DefaultRewards;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.NonreplaceableBlockOverride;
import chanceCubes.util.StatsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(CCubesCore.MODID)
public class CCubesCore
{
	public static final String MODID = "chancecubes";

	public static final Logger logger = LogManager.getLogger(MODID);

	public CCubesCore(IEventBus eventBus, Dist dist, ModContainer container)
	{
		CCubesDataComponents.DATA_COMPONENT_TYPES.register(eventBus);
		CCubesBlocks.BLOCKS.register(eventBus);
		CCubesBlocks.BLOCK_ENTITIES.register(eventBus);
		CCubesItems.ITEMS.register(eventBus);
		CCubesItems.CREATIVE_MODE_TABS.register(eventBus);
		CCubesSounds.SOUNDS.register(eventBus);
		CCubesMenus.MENUS.register(eventBus);
		CCubesModifiers.BIOME_MODIFIER_SERIALIZERS.register(eventBus);
		CCubesRewardArguments.COMMAND_ARGUMENT_TYPES.register(eventBus);
		StatsRegistry.CUSTOM_STAT.register(eventBus);
		WorldGen.FEATURES.register(eventBus);
		eventBus.addListener(this::commonStart);
		eventBus.addListener(this::onIMCMessage);
		eventBus.addListener(CCubesNetwork::setupPackets);
		if (dist.isClient())
		{
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
			eventBus.addListener(ClientHelper::registerMenuScreens);
			eventBus.addListener(ClientHelper::clientStart);
			eventBus.addListener(ClientHelper::onEntityRenders);
			NeoForge.EVENT_BUS.addListener(ClientHelper::onClientCommandsRegister);
		}
		NeoForge.EVENT_BUS.register(this);
		ConfigLoader.initParentFolder();
		container.registerConfig(ModConfig.Type.COMMON, ConfigLoader.configSpec, "chancecubes" + File.separatorChar + "chancecubes-server.toml");
	}

	public void commonStart(FMLCommonSetupEvent event)
	{
		NeoForge.EVENT_BUS.register(new PlayerConnectListener());
		NeoForge.EVENT_BUS.register(new TickListener());
	}

	@SubscribeEvent
	public void lootTableLoad(LootTableLoadEvent event)
	{
		if(CCubesSettings.chestLoot.get() && event.getName().getPath().contains("chests"))
			event.getTable().addPool(LootPool.lootPool().name("chance_cubes_cubes").add(LootItem.lootTableItem(CCubesItems.CHANCE_CUBE.get())).build());
	}

	@SubscribeEvent
	public void serverStart(ServerStartingEvent event)
	{
		HolderLookup.Provider provider = event.getServer().registryAccess();
		CCubesSettings.backupNRB.add(Blocks.BEDROCK.defaultBlockState());
		CCubesSettings.backupNRB.add(Blocks.OBSIDIAN.defaultBlockState());
		DefaultRewards.loadDefaultRewards(provider);
		DefaultGiantRewards.loadDefaultRewards(provider);
		CustomRewardsLoader.instance.loadCustomRewards(provider);
		NonreplaceableBlockOverride.loadOverrides();

		logger.log(Level.INFO, "Death and destruction prepared! (And Cookies. Cookies were also prepared.)");
	}

	@SubscribeEvent
	public void onCommandsRegister(RegisterCommandsEvent event)
	{
		new CCubesServerCommands(event.getDispatcher());
	}

	public void onIMCMessage(InterModProcessEvent e)
	{
		e.getIMCStream().forEach((message) ->
		{
			Logger logger = LogManager.getLogger(MODID);
			if(message.method().equalsIgnoreCase("add-nonreplaceable"))
			{
				Object obj = message.messageSupplier().get();
				if(obj instanceof BlockState state)
				{
					CCubesSettings.nonReplaceableBlocksIMC.add(state);
					logger.info(message.senderModId() + " has added the blockstate of \"" + state + "\" that Chance Cubes rewards will no longer replace.");
				}
			}
		});
	}
}
