package chanceCubes.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketTriggerD20;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class BlockChanceD20 extends BaseChanceBlock implements ITileEntityProvider
{

	public BlockChanceD20()
	{
		super("Chance_Icosahedron");
		super.setHardness(-1F);
		this.setLightLevel(7);
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

	public boolean renderAsNormalBlock()
	{
		return isOpaqueCube();
	}

	@Override
	public int getLightValue()
	{
		/*
		 * BUGFIX:
		 *  
		 * Vanilla has a bug here somehow, regarding light levels. See ChunkCache#getLightBrightnessForSkyBlocks.
		 * 
		 * It seems that this returns the "low" light value, i.e. 0-15. However, the light value of blocks is on a scale of 0-255 (for some ungodly reason).
		 * 
		 * This ruins the bit-packing mojang does for light levels, as the level returned from getLightValue() by defualt is 105 in our case.
		 * 
		 * What should be 0xF00070 turns into 0xF00690, causing the block emitting the light to be dark (and the AO to treat it as dark) but the surrounding blocks are lit up.
		 * 
		 * Anyways, thanks a lot mojang, as usual.
		 */
		return 7;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileChanceD20 te = (TileChanceD20) world.getTileEntity(x, y, z);
		if (te.getStage() > 0)
			setBlockBounds(0, 0, 0, 0, 0, 0);
		else
			setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		this.startd20(world, x, y, z, player);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		return this.startd20(world, x, y, z, player);
	}

	public boolean startd20(World world, int x, int y, int z, EntityPlayer player)
	{
		if (player == null || player instanceof FakePlayer)
			return false;

		TileChanceD20 te = (TileChanceD20) world.getTileEntity(x, y, z);
		if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			if (!world.isRemote)
			{
				ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceIcosahedron), 1);
				((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
				this.dropBlockAsItem(world, x, y, z, stack);
				world.setBlockToAir(x, y, z);
				world.removeTileEntity(x, y, z);
			}
			return true;
		}

		if (te != null)
		{
			if (!world.isRemote)
			{
				te.startBreaking(player);
				CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketTriggerD20(x, y, z), new TargetPoint(world.provider.dimensionId, x, y, z, 50));
			}
			return true;
		}

		return false;
	}
	
	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		return false;
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
}