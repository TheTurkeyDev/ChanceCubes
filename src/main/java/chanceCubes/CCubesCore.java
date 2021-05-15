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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
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
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
		try
		{
			SSLContext context = SSLContext.getInstance("TLSv1.2");
			TrustManager[] trustManager = new TrustManager[]{
					new X509TrustManager()
					{
						public X509Certificate[] getAcceptedIssuers()
						{
							return new X509Certificate[0];
						}

						public void checkClientTrusted(X509Certificate[] certificate, String str)
						{
						}

						public void checkServerTrusted(X509Certificate[] certificate, String str)
						{
						}
					}
			};
			context.init(null, trustManager, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch(Exception e)
		{
			e.printStackTrace();
		}

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
	}

	@SubscribeEvent
	public void lootTableLoad(LootTableLoadEvent event)
	{
		if(CCubesSettings.chestLoot.get() && event.getName().getPath().contains("chests"))
			event.getTable().addPool(LootPool.builder().name("chance_cubes_cubes").addEntry(ItemLootEntry.builder(CCubesItems.CHANCE_CUBE)).build());
	}

	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event)
	{
		CCubesSettings.backupNRB.add(Blocks.BEDROCK.getDefaultState());
		CCubesSettings.backupNRB.add(Blocks.OBSIDIAN.getDefaultState());
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
			if(message.getMethod().equalsIgnoreCase("add-nonreplaceable"))
			{
				Object obj = message.getMessageSupplier().get();
				if(obj instanceof BlockState)
				{
					BlockState state = (BlockState) obj;
					CCubesSettings.nonReplaceableBlocksIMC.add(state);
					logger.info(message.getSenderModId() + " has added the blockstate of \"" + state.toString() + "\" that Chance Cubes rewards will no longer replace.");
				}
			}
		});
	}
}
