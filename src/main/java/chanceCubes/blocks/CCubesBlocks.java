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
	public static BaseChanceBlock CHANCE_CUBE;
	public static BaseChanceBlock CHANCE_ICOSAHEDRON;
	public static BaseChanceBlock GIANT_CUBE;
	public static BaseChanceBlock COMPACT_GIANT_CUBE;
	public static BaseChanceBlock CUBE_DISPENSER;

	public static void loadBlocks()
	{
		GameRegistry.register(CHANCE_CUBE = new BlockChanceCube());
		GameRegistry.register(GIANT_CUBE = new BlockGiantCube());
		GameRegistry.register(COMPACT_GIANT_CUBE = new BlockCompactGiantCube());
		GameRegistry.register(CHANCE_ICOSAHEDRON = new BlockChanceD20());
		GameRegistry.register(CUBE_DISPENSER = new BlockCubeDispenser());
		
		GameRegistry.register(new ItemChanceCube(CHANCE_CUBE).setRegistryName(CHANCE_CUBE.getRegistryName()));
		GameRegistry.register(new ItemChanceCube(CHANCE_ICOSAHEDRON).setRegistryName(CHANCE_ICOSAHEDRON.getRegistryName()));

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		GameRegistry.registerTileEntity(TileGiantCube.class, "tileChanceGiant");
		GameRegistry.registerTileEntity(TileCubeDispenser.class, "tileCubeDispenser");
	}

	public static void registerBlocksItems()
	{
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		mesher.register(Item.getItemFromBlock(CHANCE_CUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CHANCE_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(GIANT_CUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + GIANT_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(COMPACT_GIANT_CUBE), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + COMPACT_GIANT_CUBE.getBlockName(), "inventory"));
		mesher.register(Item.getItemFromBlock(CUBE_DISPENSER), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + CUBE_DISPENSER.getBlockName(), "inventory"));
	}
}