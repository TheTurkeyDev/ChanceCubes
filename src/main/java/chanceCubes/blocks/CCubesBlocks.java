package chanceCubes.blocks;

import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.tileentities.TileCubeDispenser;
import chanceCubes.tileentities.TileGiantCube;
import cpw.mods.fml.common.registry.GameRegistry;

public class CCubesBlocks
{
	public static BaseChanceBlock chanceCube;
	public static BaseChanceBlock chanceIcosahedron;
	public static BaseChanceBlock chanceGiantCube;
	public static BaseChanceBlock chanceCompactGiantCube;
	public static BaseChanceBlock chanceCubeDispenser;

	public static void loadBlocks()
	{
		GameRegistry.registerBlock(chanceCube = new BlockChanceCube(), ItemChanceCube.class, chanceCube.getBlockName());
		GameRegistry.registerBlock(chanceIcosahedron = new BlockChanceD20(), ItemChanceCube.class, chanceIcosahedron.getBlockName());
		GameRegistry.registerBlock(chanceGiantCube = new BlockGiantCube(), chanceGiantCube.getBlockName());
		GameRegistry.registerBlock(chanceCompactGiantCube = new BlockCompactGiantCube(), chanceCompactGiantCube.getBlockName());
		GameRegistry.registerBlock(chanceCubeDispenser = new BlockCubeDispenser(), chanceCubeDispenser.getBlockName());

		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
		GameRegistry.registerTileEntity(TileGiantCube.class, "tileChanceGiant");
		GameRegistry.registerTileEntity(TileCubeDispenser.class, "tileCubeDispenser");
	}
}
