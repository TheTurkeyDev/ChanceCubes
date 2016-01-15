package chanceCubes.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileGiantCube;

public class CCubesBlocks
{
	public static BaseChanceBlock chanceCube;
	// public static Block chanceIcosahedron;
	public static BaseChanceBlock chanceGiantCube;
	public static BaseChanceBlock chanceCompactGiantCube;

	public static void loadBlocks()
	{
		// chanceIcosahedron = new BlockChanceD20();

		GameRegistry.registerBlock(chanceCube = new BlockChanceCube(), ItemChanceCube.class, chanceCube.getBlockName());
		GameRegistry.registerBlock(chanceGiantCube = new BlockGiantCube(), chanceGiantCube.getBlockName());
		GameRegistry.registerBlock(chanceCompactGiantCube = new BlockCompactGiantCube(), chanceCompactGiantCube.getBlockName());

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		// GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		GameRegistry.registerTileEntity(TileGiantCube.class, "tileChanceGiant");
	}

	public static void registerBlocks()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceCube.getBlockName(), "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceGiantCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceGiantCube.getBlockName(), "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCompactGiantCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceCompactGiantCube.getBlockName(), "inventory"));
	}
}