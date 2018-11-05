package chanceCubes.proxy;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.client.listeners.WorldRenderListener;
import chanceCubes.listeners.BlockListener;
import chanceCubes.renderer.TileChanceD20ItemRenderer;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.renderer.TileGiantCubeRenderer;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{

	@Override
	public boolean isClient()
	{
		return true;
	}

	public void registerRenderings()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileChanceD20.class, TileChanceD20Renderer.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileCubeDispenser.class, new TileCubeDispenserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileGiantCube.class, new TileGiantCubeRenderer());
		
		Item.getItemFromBlock(CCubesBlocks.CHANCE_ICOSAHEDRON).setTileEntityItemStackRenderer(new TileChanceD20ItemRenderer());
	}

	public void registerEvents()
	{
		super.registerEvents();
		MinecraftForge.EVENT_BUS.register(new RenderEvent());
		MinecraftForge.EVENT_BUS.register(new WorldRenderListener());
		MinecraftForge.EVENT_BUS.register(new BlockListener());
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}
}
