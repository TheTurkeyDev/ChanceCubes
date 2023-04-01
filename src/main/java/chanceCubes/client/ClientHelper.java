package chanceCubes.client;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.RewardSelectorPendantScreen;
import chanceCubes.client.gui.SchematicCreationGui;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.client.listeners.WorldRenderListener;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.listeners.BlockListener;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.renderer.TileGiantCubeRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkHooks;

public class ClientHelper
{

	@SubscribeEvent
	public static void clientStart(FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new RenderEvent());
		MinecraftForge.EVENT_BUS.register(new WorldRenderListener());
		MinecraftForge.EVENT_BUS.register(new BlockListener());

		BlockEntityRenderers.register(CCubesBlocks.TILE_CHANCE_ICOSAHEDRON, p_173571_ -> new TileChanceD20Renderer());
		BlockEntityRenderers.register(CCubesBlocks.TILE_CUBE_DISPENSER, p_173571_ -> new TileCubeDispenserRenderer());
		BlockEntityRenderers.register(CCubesBlocks.TILE_CHANCE_GIANT, p_173571_ -> new TileGiantCubeRenderer());

		//TODO
		//RenderTypeLookup.setRenderLayer(CCubesBlocks.CHANCE_ICOSAHEDRON, RenderType.getCutoutMipped());
		//RenderTypeLookup.setRenderLayer(CCubesBlocks.COMPACT_GIANT_CUBE, RenderType.getCutoutMipped());
	}

	public static void openRewardSelectorGUI(Player player, ItemStack stack)
	{
		Minecraft.getInstance().setScreen(new RewardSelectorPendantScreen(player, stack));
	}

	public static void openCreativePendantGUI(Player player, ItemStack stack)
	{
		NetworkHooks.openGui((ServerPlayer) player, new SimpleMenuProvider((w, p, pl) -> new CreativePendantContainer(w, p), stack.getHoverName()));
	}

	public static void openSchematicCreatorGUI(Player player)
	{
		Minecraft.getInstance().setScreen(new SchematicCreationGui(player));
	}


}
