package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CCubesBlocks
{
	public static BaseChanceBlock chanceCube;
	public static BaseChanceBlock chanceIcosahedron;
	public static BaseChanceBlock chanceGiantCube;
	public static BaseChanceBlock chanceCompactGiantCube;
	public static BaseChanceBlock chanceCubeDispenser;

	public static void loadBlocks()
	{
		GameRegistry.register(chanceCube = new BlockChanceCube());
		GameRegistry.register(chanceGiantCube = new BlockGiantCube());
		GameRegistry.register(chanceCompactGiantCube = new BlockCompactGiantCube());
		GameRegistry.register(chanceIcosahedron = new BlockChanceD20());
		GameRegistry.register(chanceCubeDispenser = new BlockCubeDispenser());
		
		GameRegistry.register(new ItemChanceCube(chanceCube).setRegistryName(chanceCube.getRegistryName()));
		GameRegistry.register(new ItemChanceCube(chanceIcosahedron).setRegistryName(chanceIcosahedron.getRegistryName()));

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		GameRegistry.registerTileEntity(TileGiantCube.class, "tileChanceGiant");
		GameRegistry.registerTileEntity(TileCubeDispenser.class, "tileCubeDispenser");
	}

	public static void registerBlocksItems()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceCube.getBlockName(), "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceGiantCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceGiantCube.getBlockName(), "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCompactGiantCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceCompactGiantCube.getBlockName(), "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCubeDispenser), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceCubeDispenser.getBlockName(), "inventory"));
		
	}
}