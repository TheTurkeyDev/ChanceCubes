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

public class CCubesBlocks
{
	public static Block chanceCube;

	// public static Block chanceIcosahedron;

	public static void loadBlocks()
	{
		// chanceIcosahedron = new BlockChanceD20();

		GameRegistry.registerBlock(chanceCube = new BlockChanceCube(), ItemChanceCube.class, "chance_Cube");

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		// GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
	}

	public static void registerBlocks()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(chanceCube), 0, new ModelResourceLocation(CCubesCore.MODID + ":" + ((BlockChanceCube) chanceCube).getName(), "inventory"));
	}
}