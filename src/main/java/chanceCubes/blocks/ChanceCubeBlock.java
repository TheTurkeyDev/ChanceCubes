package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class ChanceCubeBlock extends Block
{
	public ChanceCubeBlock()
	{
		super(Material.ground);
		super.setHardness(.5f);
		super.setBlockName("Chance_Cube");
		super.setCreativeTab(CCubesCore.modTab);
		super.setBlockTextureName("chancecubes:chanceCube");
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) 
	{
		CCubesCore.cCubeRegistry.triggerRandomReward(world, x, y, z);
	}

	public int quantityDropped(Random p_149745_1_)
	{
		return 0;
	}
}
