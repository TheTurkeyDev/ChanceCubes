package chanceCubes.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import chanceCubes.client.RenderEvent;
import chanceCubes.config.CCubesSettings;
import chanceCubes.renderer.SpecialRendererD20;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.tileentities.TileChanceD20;


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
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}
