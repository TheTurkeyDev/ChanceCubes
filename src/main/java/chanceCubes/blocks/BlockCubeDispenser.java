package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCubeDispenser extends BaseChanceBlock implements ITileEntityProvider
{
	public static final PropertyEnum<BlockCubeDispenser.DispenseType> DISPENSING = PropertyEnum.<BlockCubeDispenser.DispenseType> create("dispenseType", BlockCubeDispenser.DispenseType.class);

	public BlockCubeDispenser()
	{
		super("cube_Dispenser");
		this.setHardness(2f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileCubeDispenser();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!(world.getTileEntity(pos) instanceof TileCubeDispenser))
			return false;
		
		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(pos);
		if(player.isSneaking())
		{
			world.setBlockState(pos, this.getDefaultState().withProperty(DISPENSING, BlockCubeDispenser.getNextState(state)));
		}
		else
		{
			if(player.inventory.getCurrentItem() != null && Block.getBlockFromItem(player.inventory.getCurrentItem().getItem()).equals(te.getCurrentBlock(BlockCubeDispenser.getCurrentState(state))))
			{
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
			}
		}
		return true;
	}

	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
	{
		if(world.isRemote)
			return;
		if(!(world.getTileEntity(pos) instanceof TileCubeDispenser))
			return;
		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(pos);

		double px = player.posX;
		double py = player.posY;
		double pz = player.posZ;

		EntityItem entitem = te.getNewEntityItem(BlockCubeDispenser.getCurrentState(world.getBlockState(pos)));
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

	public boolean isOpaqueCube()
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

	public static enum DispenseType implements IStringSerializable
	{
		ChanceCube("Chance_Cube"), ChanceIcosahedron("Chance_Icosahedron"), CompactGaintCube("Compact_Gaint_Cube");

		private String type;

		private DispenseType(String name)
		{
			this.type = name;
		}

		@Override
		public String getName()
		{
			return this.type;
		}

		public DispenseType getNextState()
		{
			switch(this)
			{
				case ChanceCube:
					return ChanceIcosahedron;
				case ChanceIcosahedron:
					return CompactGaintCube;
				case CompactGaintCube:
					return ChanceCube;
				default:
					return ChanceCube;

			}
		}
	}

	public static DispenseType getNextState(IBlockState state)
	{
		return state.getValue(BlockCubeDispenser.DISPENSING).getNextState();
	}

	public static DispenseType getCurrentState(IBlockState state)
	{
		return state.getValue(BlockCubeDispenser.DISPENSING);
	}
}