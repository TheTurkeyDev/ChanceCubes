package chanceCubes.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
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
		return CCubesSettings.d20RenderID;
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
		TileChanceD20 te = (TileChanceD20) world.getTileEntity(x, y, z);
		if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceIcosahedron), 1);
			((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
			this.dropBlockAsItem(world, x, y, z, stack);
			world.setBlockToAir(x, y, z);
			world.removeTileEntity(x, y, z);
			return;
		}

		if(te != null)
			te.startBreaking(player);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		TileChanceD20 te = (TileChanceD20) world.getTileEntity(x, y, z);
		if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceIcosahedron), 1);
			((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
			this.dropBlockAsItem(world, x, y, z, stack);
			world.setBlockToAir(x, y, z);
			world.removeTileEntity(x, y, z);
			return true;
		}

		if(te != null)
		{
			te.startBreaking(player);
			return true;
		}
		return false;
	}

}