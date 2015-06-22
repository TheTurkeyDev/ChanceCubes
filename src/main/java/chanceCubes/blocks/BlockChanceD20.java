package chanceCubes.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.items.CCubesItems;
import chanceCubes.renderer.SpecialRendererD20;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockChanceD20 extends Block implements ITileEntityProvider
{
	public BlockChanceD20()
	{
		super(Material.iron);
		super.setHardness(-1F);
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

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			this.dropBlockAsItem(world, x, y, z, new ItemStack(CCubesBlocks.chanceIcosahedron, 1));
			world.setBlockToAir(x, y, z);
			world.removeTileEntity(x, y, z);
			return;
		}
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileChanceD20)
			((TileChanceD20) te).startBreaking(player);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			this.dropBlockAsItem(world, x, y, z, new ItemStack(CCubesBlocks.chanceIcosahedron, 1));
			world.setBlockToAir(x, y, z);
			world.removeTileEntity(x, y, z);
			return true;
		}
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileChanceD20)
		{
			((TileChanceD20) te).startBreaking(player);
			return true;
		}
		return false;
	}

}