package chanceCubes.blocks;

import net.minecraft.block.Block;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.common.registry.GameRegistry;

public class CCubesBlocks
{
	public static Block chanceCube;
	public static Block chanceIcosahedron;

	public static void loadBlocks()
	{
		chanceCube = new BlockChanceCube();
		chanceIcosahedron = new BlockChanceD20();
		
		
		GameRegistry.registerBlock(chanceCube, ItemChanceCube.class, "Chance_Cube");
		GameRegistry.registerBlock(chanceIcosahedron, ItemChanceCube.class, "Chance_Icosahedron");
		
		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
	}
}
