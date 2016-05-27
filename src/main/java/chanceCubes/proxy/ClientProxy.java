package chanceCubes.proxy;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.RenderEvent;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.renderer.TileCubeDispenserRenderer;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
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
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		mesher.register(Item.getItemFromBlock(CCubesBlocks.CHANCEICOSAHEDRON), 0, new ModelResourceLocation(CCubesCore.MODID + ":chance_Icosahedron", "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileChanceD20.class, new TileChanceD20Renderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCubeDispenser.class, new TileCubeDispenserRenderer());
		
		OBJLoader.INSTANCE.addDomain(CCubesCore.MODID);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CCubesBlocks.CHANCEICOSAHEDRON), 0, new ModelResourceLocation(CCubesCore.MODID + ":chance_Icosahedron", "inventory"));
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
