package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CCubesBlocks
{
	public static BaseChanceBlock CHANCECUBE;
	public static BaseChanceBlock CHANCEICOSAHEDRON;
	public static BaseChanceBlock CHANCEGIANTCUBE;
	public static BaseChanceBlock CHANCECOMPACTGIANTCUBE;
	public static BaseChanceBlock CHANCECUBEDISPENSER;

	public static void loadBlocks()
	{
		GameRegistry.register(CHANCECUBE = new BlockChanceCube());
		GameRegistry.register(CHANCEGIANTCUBE = new BlockGiantCube());
		GameRegistry.register(CHANCECOMPACTGIANTCUBE = new BlockCompactGiantCube());
		GameRegistry.register(CHANCEICOSAHEDRON = new BlockChanceD20());
		GameRegistry.register(CHANCECUBEDISPENSER = new BlockCubeDispenser());
		
		GameRegistry.register(new ItemChanceCube(CHANCECUBE).setRegistryName(CHANCECUBE.getRegistryName()));
		GameRegistry.register(new ItemChanceCube(CHANCEICOSAHEDRON).setRegistryName(CHANCEICOSAHEDRON.getRegistryName()));

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		GameRegistry.registerTileEntity(TileGiantCube.class, "tileChanceGiant");
		GameRegistry.registerTileEntity(TileCubeDispenser.class, "tileCubeDispenser");
	}

	public static void registerBlocksItems()
	{
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		mesher.register(Item.getItemFromBlock(CHANCECUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCECUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(CHANCEGIANTCUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCEGIANTCUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(CHANCECOMPACTGIANTCUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCECOMPACTGIANTCUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(CHANCECUBEDISPENSER), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCECUBEDISPENSER.getBlockName(), "inventory"));
		
	}
}