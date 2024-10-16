package chanceCubes;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.ClientHelper;
import chanceCubes.commands.CCubesServerCommands;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.config.CustomRewardsLoader;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.PlayerConnectListener;
import chanceCubes.listeners.TickListener;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.rewards.DefaultGiantRewards;
import chanceCubes.rewards.DefaultRewards;
import chanceCubes.util.NonreplaceableBlockOverride;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppedEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Mod(CCubesCore.MODID)
public class CCubesCore
{
	public static final String MODID = "chancecubes";

	public static CreativeModeTab modTab = new CreativeModeTab(MODID)
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(CCubesBlocks.CHANCE_CUBE);
		}
	};
	public static final Logger logger = LogManager.getLogger(MODID);

	public CCubesCore()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonStart);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onIMCMessage);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
		{
			FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientHelper::clientStart);
		});
		MinecraftForge.EVENT_BUS.register(this);
		ConfigLoader.initParentFolder();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigLoader.configSpec, "chancecubes" + File.separatorChar + "chancecubes-server.toml");
	}

	public void commonStart(FMLCommonSetupEvent event)
	{
		CCubesPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new PlayerConnectListener());
		MinecraftForge.EVENT_BUS.register(new TickListener());
		//MinecraftForge.EVENT_BUS.register(new WorldGen());
		//WorldGen.initWorldGen();
	}

	@SubscribeEvent
	public void lootTableLoad(LootTableLoadEvent event)
	{
//		if(CCubesSettings.chestLoot.get() && event.getName().getPath().contains("chests"))
//			event.getTable().addPool(LootPool.lootPool().name("chance_cubes_cubes").add(LootPoolSingletonContainer.simpleBuilder(new LootItem(CCubesItems.CHANCE_CUBE))).build());
	}

	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event)
	{
		CCubesSettings.backupNRB.add(Blocks.BEDROCK.defaultBlockState());
		CCubesSettings.backupNRB.add(Blocks.OBSIDIAN.defaultBlockState());
		DefaultRewards.loadDefaultRewards();
		DefaultGiantRewards.loadDefaultRewards();
		CustomRewardsLoader.instance.loadCustomRewards();
		NonreplaceableBlockOverride.loadOverrides();

		logger.log(Level.INFO, "Death and destruction prepared! (And Cookies. Cookies were also prepared.)");
	}

	@SubscribeEvent
	public void onCommandsRegister(RegisterCommandsEvent event)
	{
		new CCubesServerCommands(event.getDispatcher());
	}

	@SubscribeEvent
	public void onServerStart(FMLServerStartedEvent event)
	{
	}

	@SubscribeEvent
	public void onServerStop(FMLServerStoppedEvent event)
	{
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
