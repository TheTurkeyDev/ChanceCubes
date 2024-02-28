package chanceCubes.client;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.CreativePendantScreen;
import chanceCubes.client.gui.RewardSelectorPendantScreen;
import chanceCubes.client.gui.SchematicCreationGui;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.client.listeners.WorldRenderListener;
import chanceCubes.commands.CCubesClientCommands;
import chanceCubes.containers.CCubesMenus;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.listeners.BlockListener;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.renderer.TileGiantCubeRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;


public class ClientHelper
{
	@SubscribeEvent
	public static void clientStart(FMLClientSetupEvent event)
	{
		NeoForge.EVENT_BUS.register(new RenderEvent());
		NeoForge.EVENT_BUS.register(new WorldRenderListener());
		NeoForge.EVENT_BUS.register(new BlockListener());

		//TODO
//		RenderTypeLookup.setRenderLayer(CCubesBlocks.CHANCE_ICOSAHEDRON, RenderType.cutoutMipped());
//		RenderTypeLookup.setRenderLayer(CCubesBlocks.COMPACT_GIANT_CUBE, RenderType.cutoutMipped());
	}

	@SubscribeEvent
	public static void onEntityRenders(EntityRenderersEvent.RegisterRenderers event)
	{
		//event.registerBlockEntityRenderer(CCubesBlocks.TILE_CHANCE_ICOSAHEDRON.get(), p_173571_ -> new TileChanceD20Renderer());
		event.registerBlockEntityRenderer(CCubesBlocks.TILE_CUBE_DISPENSER.get(), p_173571_ -> new TileCubeDispenserRenderer());
		event.registerBlockEntityRenderer(CCubesBlocks.TILE_CHANCE_GIANT.get(), p_173571_ -> new TileGiantCubeRenderer());
	}

	@SubscribeEvent
	public static void onRegisterMenuScreens(RegisterMenuScreensEvent event)
	{
		event.register(CCubesMenus.CREATIVE_PENDANT_CONTAINER.get(), CreativePendantScreen::new);
	}

	public static void onClientCommandsRegister(RegisterClientCommandsEvent event)
	{
		new CCubesClientCommands(event.getDispatcher());
	}

	public static void openRewardSelectorGUI(Player player, ItemStack stack)
	{
		Minecraft.getInstance().setScreen(new RewardSelectorPendantScreen(player, stack));
	}

	public static void openCreativePendantGUI(Player player, ItemStack stack)
	{
		player.openMenu(new SimpleMenuProvider((w, p, pl) -> new CreativePendantContainer(w, p), stack.getHoverName()));
	}

	public static void openSchematicCreatorGUI(Player player)
	{
		Minecraft.getInstance().setScreen(new SchematicCreationGui(player));
	}


}
