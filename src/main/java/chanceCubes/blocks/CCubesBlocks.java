package chanceCubes.blocks;

import net.minecraft.block.Block;
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
	public static Block chanceCube;
	// public static Block chanceIcosahedron;
	public static Block chanceGiantCube;
	public static Block chanceCompactGiantCube;

	public static void loadBlocks()
	{
		// chanceIcosahedron = new BlockChanceD20();

		GameRegistry.registerBlock(chanceCube = new BlockChanceCube(), ItemChanceCube.class, "chance_Cube");
		GameRegistry.registerBlock(chanceGiantCube = new BlockGiantCube(), "Chance_Giant_Cube");
		GameRegistry.registerBlock(chanceCompactGiantCube = new BlockCompactGiantCube(), "Compact_Giant_Chance_Cube");

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		// GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		GameRegistry.registerTileEntity(TileGiantCube.class, "tileChanceGiant");
	}

	public static void registerBlocks()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + ((BlockChanceCube) chanceCube).getName(), "inventory"));
	}
}