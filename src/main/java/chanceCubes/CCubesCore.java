package chanceCubes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.Logger;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.items.CCubesItems;
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
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = CCubesCore.MODID, version = CCubesCore.VERSION)
public class CCubesCore
{
    public static final String MODID = "chancecubes";
    public static final String VERSION = "0.1";
    public static final String NAME = "Chance Cubes";

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
    	GameRegistry.registerWorldGenerator(new WorldGen(), 0);
    }

    @EventHandler
    public void load(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        ConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile());
        
		CCubesBlocks.loadBlocks();
		CCubesItems.loadItems();
		CraftingRecipies.loadRecipies();
		ChanceCubeRegistry.loadDefaultRewards();
		proxy.registerRenderings();
		proxy.registerEvents();

        FMLCommonHandler.instance().bus().register(new UpdateNotificationHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        CustomRewardsLoader.instance.loadCustomRewards();
    }
}
