package chanceCubes;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.client.listeners.WorldRenderListener;
import chanceCubes.commands.CCubesServerCommands;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomProfileLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.hookins.ModHookUtil;
import chanceCubes.listeners.BlockListener;
import chanceCubes.listeners.PlayerConnectListener;
import chanceCubes.listeners.TickListener;
import chanceCubes.listeners.WorldGen;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.profiles.triggerHooks.GameStageTriggerHooks;
import chanceCubes.profiles.triggerHooks.VanillaTriggerHooks;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.renderer.TileGiantCubeRenderer;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.NonreplaceableBlockOverride;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(CCubesCore.MODID)
public class CCubesCore
{
	public static final String MODID = "chancecubes";

	public static final String gameVersion = "1.14.4";

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
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientStart);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStart);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onIMCMessage);
		MinecraftForge.EVENT_BUS.register(this);
		ConfigLoader.initParentFolder();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigLoader.configSpec, "chancecubes" + File.separatorChar + "chancecubes-server.toml");
		// ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, ()
		// -> CCubesGuiHandler::openGui);
	}

	@SubscribeEvent
	public void commonStart(FMLCommonSetupEvent event)
	{
		CCubesPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new PlayerConnectListener());
		MinecraftForge.EVENT_BUS.register(new TickListener());
		MinecraftForge.EVENT_BUS.register(new WorldGen());
		MinecraftForge.EVENT_BUS.register(new VanillaTriggerHooks());
		if(ModList.get().isLoaded("gamestages"))
		{
			MinecraftForge.EVENT_BUS.register(new GameStageTriggerHooks());
			CCubesCore.logger.log(Level.INFO, "Loaded GameStages support!");
		}
	}

	@SubscribeEvent
	public void clientStart(FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new RenderEvent());
		MinecraftForge.EVENT_BUS.register(new WorldRenderListener());
		MinecraftForge.EVENT_BUS.register(new BlockListener());

		OBJLoader.INSTANCE.addDomain(CCubesCore.MODID);

//		ClientRegistry.bindTileEntitySpecialRenderer(TileChanceD20.class, new TileChanceD20Renderer());
//		ClientRegistry.bindTileEntitySpecialRenderer(TileCubeDispenser.class, new TileCubeDispenserRenderer());
//		ClientRegistry.bindTileEntitySpecialRenderer(TileGiantCube.class, new TileGiantCubeRenderer());
	}

	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event)
	{
		// ConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile());

		if(CCubesSettings.chestLoot.get())
		{
			// ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new
			// WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new
			// WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1,
			// 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new
			// WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new
			// WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1,
			// 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new
			// WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new
			// WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1,
			// 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new
			// WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceCube), 1, 2, 5));
			// ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new
			// WeightedRandomChestContent(new ItemStack(CCubesBlocks.chanceIcosahedron), 1,
			// 2, 5));
		}

		CCubesSettings.backupNRB.add(Blocks.BEDROCK.getDefaultState());
		CCubesSettings.backupNRB.add(Blocks.OBSIDIAN.getDefaultState());
		ChanceCubeRegistry.loadDefaultRewards();
		GiantCubeRegistry.loadDefaultRewards();
		CustomRewardsLoader.instance.loadCustomRewards();
		CustomRewardsLoader.instance.fetchRemoteInfo();
		ProfileManager.initProfiles();
		CustomProfileLoader.instance.loadProfiles();
		NonreplaceableBlockOverride.loadOverrides();
		ModHookUtil.loadCustomModRewards();
		// ConfigLoader.config.save();

		// See ForgeCommand
		new CCubesServerCommands(event.getCommandDispatcher());

		logger.log(Level.INFO, "Death and destruction prepared! (And Cookies. Cookies were also prepared.)");
	}

	@SubscribeEvent
	public void onIMCMessage(InterModProcessEvent e)
	{
		e.getIMCStream().forEach((message) ->
		{
			// if(message..equalsIgnoreCase("add-nonreplaceable") &&
			// message.isItemStackMessage())
			// {
			// ItemStack stack = message.getItemStackValue();
			// Block block = Block.getBlockFromItem(stack.getItem());
			// if(block != null)
			// {
			// IBlockState state = RewardsUtil.getBlockStateFromBlockMeta(block,
			// stack.getItemDamage());
			// CCubesSettings.nonReplaceableBlocksIMC.add(state);
			// logger.info(message.getSender() + " has added the blockstate of \"" +
			// state.toString() + "\" that Chance Cubes rewards will no longer replace.");
			// }
			// else
			// {
			// logger.error("Chance Cubes recieved an item stack via IMC from " +
			// message.getSender() + " with an item that cannot be converted to a block.
			// Item: " + stack.getItem().getUnlocalizedName());
			// }
			// }
		});
	}
}
