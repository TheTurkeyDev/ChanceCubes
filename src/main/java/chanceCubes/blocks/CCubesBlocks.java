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
	public static BlockChanceCube chanceCube;
	// public static Block chanceIcosahedron;
	public static BlockGiantCube chanceGiantCube;
	public static BlockCompactGiantCube chanceCompactGiantCube;

	public static void loadBlocks()
	{
		// chanceIcosahedron = new BlockChanceD20();

		GameRegistry.registerBlock(chanceCube = new BlockChanceCube(), ItemChanceCube.class, chanceCube.getName());
		GameRegistry.registerBlock(chanceGiantCube = new BlockGiantCube(), chanceGiantCube.getName());
		GameRegistry.registerBlock(chanceCompactGiantCube = new BlockCompactGiantCube(), chanceCompactGiantCube.getName());

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		// GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		GameRegistry.registerTileEntity(TileGiantCube.class, "tileChanceGiant");
	}

	public static void registerBlocks()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceCube.getName(), "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceGiantCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceGiantCube.getName(), "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCompactGiantCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + chanceCompactGiantCube.getName(), "inventory"));
	}
}