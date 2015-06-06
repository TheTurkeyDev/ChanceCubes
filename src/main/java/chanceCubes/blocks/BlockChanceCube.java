package chanceCubes.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;

public class BlockChanceCube extends Block implements ITileEntityProvider
{ 
	public BlockChanceCube()
	{
		super(Material.ground);
		this.setHardness(0.5f);
		this.setBlockName("Chance_Cube");
		this.setCreativeTab(CCubesCore.modTab);
		this.setBlockTextureName("chancecubes:chanceCube");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileChanceCube();
	}

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player)
    {
    	super.onBlockHarvested(world, x, y, z, side, player);
        TileChanceCube te = (TileChanceCube) world.getTileEntity(x, y, z);
        if (te != null)
        {
            ChanceCubeRegistry.INSTANCE.triggerRandomReward(world, x, y, z, player, te.getChance());
        }
    }

	@Override
	public int quantityDropped(Random p_149745_1_)
	{
		return 0;
	}
}