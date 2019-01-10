package chanceCubes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.CCubesGuiHandler;
import chanceCubes.commands.CCubesServerCommands;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.items.CCubesItems;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.proxy.CommonProxy;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.CCubesRecipies;
import chanceCubes.util.NonreplaceableBlockOverride;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
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
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import net.minecraftforge.fml.relauncher.Side;

@Mod(CCubesCore.MODID)
public class CCubesCore
{
	public static final String MODID = "chancecubes";
	public static final String VERSION = "@VERSION@";

	public static final String gameVersion = "1.12.1";

	@SidedProxy(clientSide = "chanceCubes.proxy.ClientProxy", serverSide = "chanceCubes.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static ItemGroup modTab = new ItemGroup(MODID)
	{

		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(CCubesBlocks.CHANCE_CUBE);
		}
	};
	public static Logger logger;
	
	public CCubesCore() {
        FMLModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLModLoadingContext.get().getModEventBus().addListener(this::postInit);
	}

	public void init(FMLInitializationEvent event)
	{
		CCubesRecipies.loadRecipies();

		if(event.getSide() == Dist.CLIENT)
		{
			CCubesItems.registerItems();
			CCubesBlocks.registerBlocksItems();
		}

		proxy.registerRenderings();
	}

	public void preInit(FMLPreInitializationEvent event)
	{
		logger = LogManager.getLogger(MODID);
		ConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile());

		CCubesPacketHandler.init();
		proxy.registerEvents();
		CCubesSounds.loadSolunds();

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
		CCubesSettings.backupNRB.add(RewardsUtil.getBlockStateFromBlockMeta(Block.getBlockFromName("minecraft:bedrock"), 0));
		CCubesSettings.backupNRB.add(RewardsUtil.getBlockStateFromBlockMeta(Block.getBlockFromName("minecraft:obsidian"), 0));
		ChanceCubeRegistry.loadDefaultRewards();
		GiantCubeRegistry.loadDefaultRewards();
		CustomRewardsLoader.instance.loadCustomRewards();
		CustomRewardsLoader.instance.fetchRemoteInfo();
		NonreplaceableBlockOverride.loadOverrides();
		ConfigLoader.config.save();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		ModHookUtil.loadCustomModRewards();
		ConfigLoader.config.save();

		// if(event.getSide().isClient())
		// {
		// CCubesCore.logger.log(Level.INFO, "Client-side commands loaded");
		// ClientCommandHandler.instance.registerCommand(new CCubesClientCommands());
		// }
		// else if(event.getSide().isServer())
		// {
		// CCubesCore.logger.log(Level.INFO, "Server-side commands loaded");
		// event.registerServerCommand(new CCubesServerCommands());
		// }
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
					IBlockState state = RewardsUtil.getBlockStateFromBlockMeta(block, stack.getItemDamage());
					CCubesSettings.nonReplaceableBlocksIMC.add(state);
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
