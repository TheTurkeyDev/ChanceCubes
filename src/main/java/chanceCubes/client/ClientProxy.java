package chanceCubes.client;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.RewardSelectorPendantGui;
import chanceCubes.client.gui.SchematicCreationGui;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.client.listeners.WorldRenderListener;
import chanceCubes.items.CCubesItems;
import chanceCubes.listeners.BlockListener;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.renderer.TileGiantCubeRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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

		ClientRegistry.bindTileEntityRenderer(CCubesBlocks.TILE_CHANCE_ICOSAHEDRON, TileChanceD20Renderer::new);
		ClientRegistry.bindTileEntityRenderer(CCubesBlocks.TILE_CUBE_DISPENSER, TileCubeDispenserRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CCubesBlocks.TILE_CHANCE_GIANT, TileGiantCubeRenderer::new);

		RenderTypeLookup.setRenderLayer(CCubesBlocks.CHANCE_ICOSAHEDRON, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(CCubesBlocks.COMPACT_GIANT_CUBE, RenderType.getCutoutMipped());
	}

	public static void openRewardSelectorGUI(PlayerEntity player, ItemStack stack)
	{
		Minecraft.getInstance().displayGuiScreen(new RewardSelectorPendantGui(player, stack));
	}

	public static void openSchematicCreatorGUI(PlayerEntity player)
	{
		Minecraft.getInstance().displayGuiScreen(new SchematicCreationGui(player));
	}
}
