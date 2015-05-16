package chanceCubes.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.renderer.SpecialRendererD20;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockChanceD20 extends Block implements ITileEntityProvider
{
	public BlockChanceD20()
	{
		super(Material.iron);
		super.setHardness(1F);
		this.setBlockName("Chance_Icosahedron");
		this.setCreativeTab(CCubesCore.modTab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileChanceD20();
	}
	
	@Override
	public int getRenderType()
	{
		return SpecialRendererD20.renderID;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
	}
}
