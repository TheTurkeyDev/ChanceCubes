package chanceCubes;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.CCubesGuiHandler;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.client.listeners.WorldRenderListener;
import chanceCubes.commands.CCubesServerCommands;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.config.LuckyBlockRewardLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.BlockListener;
import chanceCubes.listeners.PlayerConnectListener;
import chanceCubes.listeners.TickListener;
import chanceCubes.listeners.WorldGen;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.renderer.TileGiantCubeRenderer;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.CCubesRecipies;
import chanceCubes.util.NonreplaceableBlockOverride;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;

@Mod(CCubesCore.MODID)
public class CCubesCore
{
	public static final String MODID = "chancecubes";
	public static final String VERSION = "@VERSION@";

	public static final String gameVersion = "1.13.2";

	public static ItemGroup modTab = new ItemGroup(MODID)
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(CCubesBlocks.CHANCE_CUBE);
		}
	};
	public static final Logger logger = LogManager.getLogger(MODID);;

	public CCubesCore()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientStart);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStart);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onIMCMessage);
	}

	public void commonStart()
	{
		CCubesSounds.loadSolunds();
		CCubesRecipies.loadRecipies();

		MinecraftForge.EVENT_BUS.register(new PlayerConnectListener());
		MinecraftForge.EVENT_BUS.register(new TickListener());
		MinecraftForge.EVENT_BUS.register(new WorldGen());
		MinecraftForge.EVENT_BUS.register(new CCubesBlocks());
		MinecraftForge.EVENT_BUS.register(new CCubesItems());
	}

	public void clientStart(FMLClientSetupEvent event)
	{
		CCubesItems.registerItems();
		CCubesBlocks.registerBlocksItems();

		MinecraftForge.EVENT_BUS.register(new RenderEvent());
		MinecraftForge.EVENT_BUS.register(new WorldRenderListener());
		MinecraftForge.EVENT_BUS.register(new BlockListener());

		ClientRegistry.bindTileEntitySpecialRenderer(TileChanceD20.class, TileChanceD20Renderer.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileCubeDispenser.class, new TileCubeDispenserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileGiantCube.class, new TileGiantCubeRenderer());

		CCubesSounds.loadSolunds();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new CCubesGuiHandler());
	}

	public void serverStart(FMLServerStartingEvent event)
	{
		ConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile());
		CCubesPacketHandler.init();

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

		CCubesSettings.backupNRB.add(RewardsUtil.getBlockStateFromBlockMeta(Block.getBlockFromName("minecraft:bedrock"), 0));
		CCubesSettings.backupNRB.add(RewardsUtil.getBlockStateFromBlockMeta(Block.getBlockFromName("minecraft:obsidian"), 0));
		ChanceCubeRegistry.loadDefaultRewards();
		GiantCubeRegistry.loadDefaultRewards();
		CustomRewardsLoader.instance.loadCustomRewards();
		CustomRewardsLoader.instance.fetchRemoteInfo();
		LuckyBlockRewardLoader.instance.parseLuckyBlockRewards();
		NonreplaceableBlockOverride.loadOverrides();
		ModHookUtil.loadCustomModRewards();
		ConfigLoader.config.save();

		//See ForgeCommand
		new CCubesServerCommands(event.getCommandDispatcher());

		logger.log(Level.INFO, "Death and destruction prepared! (And Cookies. Cookies were also prepared.)");
	}

	public void onIMCMessage(InterModProcessEvent e)
	{
		e.getIMCStream().forEach((message) -> {
			//			if(message..equalsIgnoreCase("add-nonreplaceable") && message.isItemStackMessage())
			//			{
			//				ItemStack stack = message.getItemStackValue();
			//				Block block = Block.getBlockFromItem(stack.getItem());
			//				if(block != null)
			//				{
			//					IBlockState state = RewardsUtil.getBlockStateFromBlockMeta(block, stack.getItemDamage());
			//					CCubesSettings.nonReplaceableBlocksIMC.add(state);
			//					logger.info(message.getSender() + " has added the blockstate of \"" + state.toString() + "\" that Chance Cubes rewards will no longer replace.");
			//				}
			//				else
			//				{
			//					logger.error("Chance Cubes recieved an item stack via IMC from " + message.getSender() + " with an item that cannot be converted to a block. Item: " + stack.getItem().getUnlocalizedName());
			//				}
			//			}
		});
	}
}
