package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileChanceCube;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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
		TileChanceCube te = (TileChanceCube) world.getTileEntity(x, y, z);
		if (te != null)
		{
			CCubesCore.cCubeRegistry.triggerRandomReward(world, x, y, z, player, te.getLuck());
		}
		
	}

	@Override
	public int quantityDropped(Random p_149745_1_)
	{
		return 0;
	}
	
	@Override
	public boolean hasTileEntity(int meta)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		return new TileChanceCube();
	}
}
