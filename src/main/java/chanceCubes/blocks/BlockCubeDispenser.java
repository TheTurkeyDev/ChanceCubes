package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileCubeDispenser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCubeDispenser extends BaseChanceBlock implements ITileEntityProvider
{
	@SideOnly(Side.CLIENT)
	private IIcon top, side;

	public BlockCubeDispenser()
	{
		super("Cube_Dispenser");
		this.setHardness(2f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileCubeDispenser();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if(!(world.getTileEntity(x, y, z) instanceof TileCubeDispenser))
			return false;
		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(x, y, z);
		if(player.isSneaking())
		{
			world.setBlockMetadataWithNotify(x, y, z, (world.getBlockMetadata(x, y, z) + 1) % 3, 3);
		}
		else
		{
			if(player.inventory.getCurrentItem() != null && Block.getBlockFromItem(player.inventory.getCurrentItem().getItem()).equals(te.getCurrentBlock(world.getBlockMetadata(x, y, z))))
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
		}
		return true;
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		if(world.isRemote)
			return;
		if(!(world.getTileEntity(x, y, z) instanceof TileCubeDispenser))
			return;
		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(x, y, z);
		
		double px = player.posX;
		double py = player.posY;
		double pz = player.posZ;

		EntityItem entitem = te.getNewEntityItem(world.getBlockMetadata(x, y, z));
		entitem.setLocationAndAngles(px, py, pz, 0, 0);
		if(player.isSneaking())
		{
			entitem.getEntityItem().stackSize = 1;
			world.spawnEntityInWorld(entitem);
		}
		else
		{
			entitem.getEntityItem().stackSize = 64;
			world.spawnEntityInWorld(entitem);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		this.top = register.registerIcon(CCubesCore.MODID + ":cubeDispenser_Top");
		this.side = register.registerIcon(CCubesCore.MODID + ":cubeDispenser_Side");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if(side == 0 || side == 1)
			return this.top;
		return this.side;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		return !(entity instanceof EntityWither) && super.canEntityDestroy(world, x, y, z, entity);
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{

	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}

	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return null;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random p_149745_1_)
	{
		return 0;
	}
}
