package chanceCubes.blocks;

import chanceCubes.renderer.SpecialRendererD20;
import chanceCubes.renderer.TileChanceD20Renderer;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.block.Block;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CCubesBlocks
{
	
	public static Block chanceCube;
	public static Block chanceIcosahedron;

	public static void loadBlocks()
	{
		chanceCube = new BlockChanceCube();
		chanceIcosahedron = new BlockChanceD20();
		
		GameRegistry.registerBlock(chanceCube, "Chance_Cube");
		GameRegistry.registerBlock(chanceIcosahedron, "Chance_Icosahedron");
		
		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileChanceD20.class, new TileChanceD20Renderer());
		SpecialRendererD20.renderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(SpecialRendererD20.renderID, new SpecialRendererD20());
	}
}
