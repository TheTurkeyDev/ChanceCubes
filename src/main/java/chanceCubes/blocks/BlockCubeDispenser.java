package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCubeDispenser extends BaseChanceBlock implements ITileEntityProvider
{
	public static final PropertyEnum<BlockCubeDispenser.DispenseType> DISPENSING = PropertyEnum.<BlockCubeDispenser.DispenseType>create("dispensing", BlockCubeDispenser.DispenseType.class);

	public BlockCubeDispenser()
	{
		super("cube_dispenser");
		this.setHardness(2f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DISPENSING, DispenseType.CHANCE_CUBE));
		this.setLightOpacity(0);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileCubeDispenser();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY)
	{
		if(world.isRemote)
			return true;
		if(!(world.getTileEntity(pos) instanceof TileCubeDispenser))
			return true;

		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(pos);
		if(player.isSneaking())
		{
			state = state.cycleProperty(DISPENSING);
			world.setBlockState(pos, state, 3);
		}
		else
		{
			Block block = Block.getBlockFromItem(player.inventory.getCurrentItem().getItem());
			if(block.equals(te.getCurrentBlock(BlockCubeDispenser.getCurrentState(state))))
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
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

		if(te == null)
			return;

		EntityItem entitem = te.getNewEntityItem(BlockCubeDispenser.getCurrentState(world.getBlockState(pos)));
		entitem.setLocationAndAngles(player.posX, player.posY, player.posZ, 0, 0);
		if(player.isSneaking())
		{
			entitem.getItem().setCount(1);
			world.spawnEntity(entitem);
		}
		else
		{
			entitem.getItem().setCount(64);
			world.spawnEntity(entitem);
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	public int getMetaFromState(IBlockState state)
	{
		DispenseType type = getCurrentState(state);
		if(type == DispenseType.CHANCE_CUBE)
			return 0;
		else if(type == DispenseType.CHANCE_ICOSAHEDRON)
			return 1;
		else if(type == DispenseType.COMPACT_GIANTCUBE)
			return 2;
		else
			return 0;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		if(meta == 0)
			return this.getDefaultState().withProperty(DISPENSING, DispenseType.CHANCE_CUBE);
		else if(meta == 1)
			return this.getDefaultState().withProperty(DISPENSING, DispenseType.CHANCE_ICOSAHEDRON);
		else if(meta == 2)
			return this.getDefaultState().withProperty(DISPENSING, DispenseType.COMPACT_GIANTCUBE);
		else
			return this.getDefaultState().withProperty(DISPENSING, DispenseType.CHANCE_CUBE);
	}

	public enum DispenseType implements IStringSerializable
	{
		CHANCE_CUBE("chance_cube"), CHANCE_ICOSAHEDRON("chance_icosahedron"), COMPACT_GIANTCUBE("compact_giant_cube");

		private String type;

		DispenseType(String name)
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
				case CHANCE_CUBE:
					return CHANCE_ICOSAHEDRON;
				case CHANCE_ICOSAHEDRON:
					return COMPACT_GIANTCUBE;
				case COMPACT_GIANTCUBE:
				default:
					return CHANCE_CUBE;

			}
		}
	}

	public static DispenseType getNextState(IBlockState state)
	{
		return state.getValue(DISPENSING).getNextState();
	}

	public static DispenseType getCurrentState(IBlockState state)
	{
		return state.getValue(DISPENSING);
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DISPENSING);
	}
}