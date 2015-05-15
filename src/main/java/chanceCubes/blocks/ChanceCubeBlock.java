package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ChanceCubeBlock extends Block
{
	public ChanceCubeBlock()
	{
		super(Material.ground);
		this.setHardness(.5f);
		this.setBlockName("Chance_Cube");
		this.setCreativeTab(CCubesCore.modTab);
		this.setBlockTextureName("chancecubes:chanceCube");
	}
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player)
	{
		CCubesCore.cCubeRegistry.triggerRandomReward(world, x, y, z, player);
	}

	@Override
	public int quantityDropped(Random p_149745_1_)
	{
		return 0;
	}
}
