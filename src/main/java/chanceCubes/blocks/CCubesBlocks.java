package chanceCubes.blocks;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class CCubesBlocks
{
	
	public static Block chanceCube;

	public static void loadBlocks()
	{
		chanceCube = new ChanceCubeBlock();
		
		
		GameRegistry.registerBlock(chanceCube, "Chance_Cube");
	}
}
