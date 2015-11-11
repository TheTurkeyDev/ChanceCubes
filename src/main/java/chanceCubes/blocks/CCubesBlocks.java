package chanceCubes.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;

public class CCubesBlocks
{
	public static Block chanceCube;
	public static Block chanceIcosahedron;

	public static void loadBlocks()
	{
		chanceCube = new BlockChanceCube();
		chanceIcosahedron = new BlockChanceD20();
		
		
		GameRegistry.registerBlock(chanceCube, ItemChanceCube.class, "Chance_Cube");
		
		GameRegistry.registerTileEntity(TileChanceCube.class, "tileChanceCube");
		GameRegistry.registerTileEntity(TileChanceD20.class, "tileChanceIcosahedron");
	}
}
