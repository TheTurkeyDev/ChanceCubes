package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
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

	public static void loadBlocks()
	{
		GameRegistry.registerBlock(chanceCube = new BlockChanceCube(), ItemChanceCube.class, chanceCube.getBlockName());
		GameRegistry.registerBlock(chanceIcosahedron = new BlockChanceD20(), ItemChanceCube.class, chanceIcosahedron.getBlockName());
		GameRegistry.registerBlock(chanceGiantCube = new BlockGiantCube(), chanceGiantCube.getBlockName());
		GameRegistry.registerBlock(chanceCompactGiantCube = new BlockCompactGiantCube(), chanceCompactGiantCube.getBlockName());

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
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