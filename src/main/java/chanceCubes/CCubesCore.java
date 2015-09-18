package chanceCubes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.CCubesGuiHandler;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.TickListener;
import chanceCubes.listeners.UpdateNotificationListener;
import chanceCubes.listeners.WorldGen;
import chanceCubes.network.CCubesPacket;
import chanceCubes.proxy.CommonProxy;
import chanceCubes.registry.ChanceCubeRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = CCubesCore.MODID, version = CCubesCore.VERSION, name = CCubesCore.NAME, guiFactory = "chanceCubes.config.ConfigGuiFactory")
public class CCubesCore
{
    public static final String MODID = "chancecubes";
    public static final String VERSION = "@VERSION@";
    public static final String NAME = "Chance Cubes";
    
	public SimpleNetworkWrapper network;

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
		ChanceCubeRegistry.loadDefaultRewards();
		ConfigLoader.config.save();
		proxy.registerRenderings();
		proxy.registerEvents();

        FMLCommonHandler.instance().bus().register(new UpdateNotificationListener());
        FMLCommonHandler.instance().bus().register(new TickListener());
        MinecraftForge.EVENT_BUS.register(new WorldGen());
        
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube),1,2,20));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron),1,2,20));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube),1,2,20));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron),1,2,20));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube),1,2,20));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron),1,2,20));
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube),1,2,20));
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron),1,2,20));
        
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CCubesGuiHandler());
        
        this.network = NetworkRegistry.INSTANCE.newSimpleChannel("ChanceCubes");
		this.network.registerMessage(CCubesPacket.HandlerServer.class, CCubesPacket.class, 0, Side.SERVER);
		this.network.registerMessage(CCubesPacket.HandlerClient.class, CCubesPacket.class, 1, Side.CLIENT);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        CustomRewardsLoader.instance.loadCustomRewards();
        CustomRewardsLoader.instance.loadHolidayRewards();
        ModHookUtil.loadCustomModRewards();
		ConfigLoader.config.save();
    }
}
