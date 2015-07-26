package chanceCubes.proxy;

import net.minecraftforge.common.MinecraftForge;
import chanceCubes.client.RenderEvent;
import chanceCubes.config.CCubesSettings;
import chanceCubes.renderer.SpecialRendererD20;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;


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
	}
	
	public void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(new RenderEvent());
	}
}
