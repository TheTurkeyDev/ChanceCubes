package chanceCubes.blocks;

import java.util.Random;

import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.Bound;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChanceCube extends Block
{
    public static Bound<Integer> luckBound;
    
	public BlockChanceCube()
	{
		super(Material.ground);
		this.setHardness(0.5f);
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
            ChanceCubeRegistry.INSTANCE.triggerRandomReward(world, new BlockCoord(te), player, te.getLuck(), luckBound);
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
