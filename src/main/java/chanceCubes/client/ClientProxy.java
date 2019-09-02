package chanceCubes.client;

import chanceCubes.CCubesCore;
import chanceCubes.client.gui.CreativePendantGui;
import chanceCubes.client.gui.ProfileGui;
import chanceCubes.client.gui.RewardSelectorPendantGui;
import chanceCubes.client.gui.SchematicCreationGui;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.client.listeners.WorldRenderListener;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.BlockListener;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.renderer.TileGiantCubeRenderer;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy
{
	public ClientProxy()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientStart);
	}

	@SubscribeEvent
	public void clientStart(FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new RenderEvent());
		MinecraftForge.EVENT_BUS.register(new WorldRenderListener());
		MinecraftForge.EVENT_BUS.register(new BlockListener());

		OBJLoader.INSTANCE.addDomain(CCubesCore.MODID);

		ClientRegistry.bindTileEntitySpecialRenderer(TileChanceD20.class, new TileChanceD20Renderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCubeDispenser.class, new TileCubeDispenserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileGiantCube.class, new TileGiantCubeRenderer());

		ScreenManager.registerFactory(CCubesItems.CREATIVE_PENDANT_CONTAINER, CreativePendantGui::new);
	}

	public static void openRewardSelectorGUI(PlayerEntity player, ItemStack stack)
	{
		Minecraft.getInstance().displayGuiScreen(new RewardSelectorPendantGui(player, stack));
	}

	public static void openSchematicCreatorGUI(PlayerEntity player)
	{
		Minecraft.getInstance().displayGuiScreen(new SchematicCreationGui(player));
	}

	public static void openProfilesGUI()
	{
		Minecraft.getInstance().displayGuiScreen(new ProfileGui());
	}
}
