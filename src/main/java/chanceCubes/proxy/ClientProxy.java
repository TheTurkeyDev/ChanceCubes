package chanceCubes.proxy;

import chanceCubes.client.RenderEvent;
import chanceCubes.config.CCubesSettings;
import chanceCubes.renderer.SpecialRendererD20;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;


public class ClientProxy extends CommonProxy
{

	@Override
	public boolean isClient()
	{
		return true;
	}

	public void registerRenderings()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileChanceD20.class, new TileChanceD20Renderer());
		CCubesSettings.d20RenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(CCubesSettings.d20RenderID, new SpecialRendererD20());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCubeDispenser.class, new TileCubeDispenserRenderer());
	}
	
	public void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(new RenderEvent());
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}
