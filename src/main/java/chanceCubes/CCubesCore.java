package chanceCubes;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.CCubesGuiHandler;
import chanceCubes.client.listeners.WorldRenderListener;
import chanceCubes.commands.CCubesClientCommands;
import chanceCubes.commands.CCubesServerCommands;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.PlayerConnectListener;
import chanceCubes.listeners.TickListener;
import chanceCubes.listeners.WorldGen;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.proxy.CommonProxy;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.util.CCubesAchievements;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = CCubesCore.MODID, version = CCubesCore.VERSION, name = CCubesCore.NAME, guiFactory = "chanceCubes.config.ConfigGuiFactory")
public class CCubesCore
{
	public static final String MODID = "chancecubes";
	public static final String VERSION = "@VERSION@";
	public static final String NAME = "Chance Cubes";

	public static final String gameVersion = "1.7.10";

	@Instance(value = MODID)
	public static CCubesCore instance;
	@SidedProxy(clientSide = "chanceCubes.proxy.ClientProxy", serverSide = "chanceCubes.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static CreativeTabs modTab = new CreativeTabs(MODID)
	{
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(CCubesBlocks.chanceCube);
		}
	};
	public static Logger logger;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

	}

	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		ConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile(), event.getSourceFile());

		CCubesBlocks.loadBlocks();
		CCubesItems.loadItems();
		CraftingRecipies.loadRecipies();
		CCubesPacketHandler.init();
		CCubesAchievements.loadAchievements();
		proxy.registerRenderings();
		proxy.registerEvents();

		FMLCommonHandler.instance().bus().register(new PlayerConnectListener());
		FMLCommonHandler.instance().bus().register(new TickListener());
		MinecraftForge.EVENT_BUS.register(new WorldGen());
		MinecraftForge.EVENT_BUS.register(new WorldRenderListener());

		if(CCubesSettings.chestLoot)
		{
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1, 2, 5));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1, 2, 5));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1, 2, 5));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1, 2, 5));
		}

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new CCubesGuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		ChanceCubeRegistry.loadDefaultRewards();
		GiantCubeRegistry.loadDefaultRewards();
		CustomRewardsLoader.instance.loadCustomRewards();
		CustomRewardsLoader.instance.loadHolidayRewards();
		CustomRewardsLoader.instance.loadDisabledRewards();
		ConfigLoader.config.save();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		ModHookUtil.loadCustomModRewards();
		
		if(event.getSide().isClient())
		{
			CCubesCore.logger.log(Level.INFO, "Client-side commands loaded");
			ClientCommandHandler.instance.registerCommand(new CCubesClientCommands());
		}
		else if(event.getSide().isServer())
		{
			CCubesCore.logger.log(Level.INFO, "Server-side commands loaded");
			event.registerServerCommand(new CCubesServerCommands());
		}
		
	}
}
