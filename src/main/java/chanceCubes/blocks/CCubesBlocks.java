package chanceCubes.blocks;

import net.minecraft.block.Block;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileGiantCube;
import cpw.mods.fml.common.registry.GameRegistry;

public class CCubesBlocks
{
	public static Block chanceCube;
	public static Block chanceIcosahedron;
	public static Block chanceGiantCube;
	public static Block chanceCompactGiantCube;

	public static void loadBlocks()
	{
		chanceCube = new BlockChanceCube();
		chanceIcosahedron = new BlockChanceD20();
		chanceGiantCube = new BlockGiantCube();
		chanceCompactGiantCube = new BlockCompactGiantCube();

		GameRegistry.registerBlock(chanceCube, ItemChanceCube.class, "Chance_Cube");
		GameRegistry.registerBlock(chanceIcosahedron, ItemChanceCube.class, "Chance_Icosahedron");
		GameRegistry.registerBlock(chanceGiantCube, "Chance_Giant_Cube");
		GameRegistry.registerBlock(chanceCompactGiantCube, "Compact_Giant_Chance_Cube");

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		GameRegistry.registerTileEntity(TileGiantCube.class, "tileChanceGiant");
	}
}
