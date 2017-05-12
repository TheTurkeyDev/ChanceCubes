package chanceCubes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.CCubesGuiHandler;
import chanceCubes.client.listeners.WorldRenderListener;
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
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.CCubesAchievements;
import chanceCubes.util.CCubesRecipies;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = CCubesCore.MODID, version = CCubesCore.VERSION, name = CCubesCore.NAME, guiFactory = "chanceCubes.config.ConfigGuiFactory", dependencies = "required-after:Forge@[12.18.2.2099,)")
public class CCubesCore
{
	public static final String MODID = "chancecubes";
	public static final String VERSION = "@VERSION@";
	public static final String NAME = "Chance Cubes";

	public static final String gameVersion = "1.10.2";

	@Instance(value = MODID)
	public static CCubesCore instance;
	@SidedProxy(clientSide = "chanceCubes.proxy.ClientProxy", serverSide = "chanceCubes.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static CreativeTabs modTab = new CreativeTabs(MODID)
	{
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(CCubesBlocks.CHANCE_CUBE);
		}
	};
	public static Logger logger;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		CCubesRecipies.loadRecipies();
		CCubesSounds.loadSounds();

		if(event.getSide() == Side.CLIENT)
		{
			CCubesItems.registerItems();
			CCubesBlocks.registerBlocksItems();
		}

		proxy.registerRenderings();
	}

	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		ConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile());

		CCubesBlocks.loadBlocks();
		CCubesItems.loadItems();
		CCubesPacketHandler.init();
		CCubesAchievements.loadAchievements();
		proxy.registerEvents();

		MinecraftForge.EVENT_BUS.register(new PlayerConnectListener());
		MinecraftForge.EVENT_BUS.register(new TickListener());
		MinecraftForge.EVENT_BUS.register(new WorldGen());
		MinecraftForge.EVENT_BUS.register(new WorldRenderListener());

		if(CCubesSettings.chestLoot)
		{
			// ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1, 2, 5));
		}

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new CCubesGuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		ChanceCubeRegistry.loadDefaultRewards();
		GiantCubeRegistry.loadDefaultRewards();
		CustomRewardsLoader.instance.loadCustomRewards();
		CustomRewardsLoader.instance.fetchRemoteInfo();
		ConfigLoader.config.save();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		ModHookUtil.loadCustomModRewards();

		/*if(event.getSide().isClient())
		{
			CCubesCore.logger.log(Level.INFO, "Client-side commands loaded");
			ClientCommandHandler.instance.registerCommand(new CCubesClientCommands());
		}
		else if(event.getSide().isServer())
		{
			CCubesCore.logger.log(Level.INFO, "Server-side commands loaded");
			event.registerServerCommand(new CCubesServerCommands());
		}*/
		event.registerServerCommand(new CCubesServerCommands());
	}

	@EventHandler
	public void onIMCMessage(IMCEvent e)
	{
		Logger logger = LogManager.getLogger(MODID);
		for(IMCMessage message : e.getMessages())
		{
			if(message.key.equalsIgnoreCase("add-nonreplaceable") && message.isItemStackMessage())
			{
				ItemStack stack = message.getItemStackValue();
				Block block = Block.getBlockFromItem(stack.getItem());
				if(block != null)
				{
					IBlockState state = block.getStateFromMeta(stack.getItemDamage());
					CCubesSettings.nonReplaceableBlocks.add(state);
					logger.info(message.getSender() + " has added the blockstate of \"" + state.toString() + "\" that Chance Cubes rewards will no longer replace.");
				}
				else
				{
					logger.error("Chance Cubes recieved an item stack via IMC from " + message.getSender() + " with an item that cannot be converted to a block. Item: " + stack.getItem().getUnlocalizedName());
				}
			}
		}
	}
}
