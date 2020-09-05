package chanceCubes.blocks;

import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCubeDispenser extends BaseChanceBlock
{
	public static final EnumProperty<DispenseType> DISPENSING = EnumProperty.create("dispensing", BlockCubeDispenser.DispenseType.class);

	public BlockCubeDispenser()
	{
		super(getBuilder().hardnessAndResistance(2f, Integer.MAX_VALUE), "cube_dispenser");
		this.setDefaultState(this.stateContainer.getBaseState().with(DISPENSING, DispenseType.CHANCE_CUBE));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileCubeDispenser();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
	{
		if(world.isRemote)
			return ActionResultType.PASS;
		if(!(world.getTileEntity(pos) instanceof TileCubeDispenser))
			return ActionResultType.PASS;

		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(pos);
		//isCrouching ???? wtf
		if(player.isSneaking())
		{
			//TODO is this cycle?
			state = state.func_235896_a_(DISPENSING);
			world.setBlockState(pos, state, 3);
		}
		else
		{
			Block block = Block.getBlockFromItem(player.inventory.getCurrentItem().getItem());
			if(block.equals(te.getCurrentBlock(BlockCubeDispenser.getCurrentState(state))))
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
		}
		return ActionResultType.SUCCESS;
	}

	public void onBlockClicked(World world, BlockPos pos, PlayerEntity player)
	{
		if(world.isRemote)
			return;
		if(!(world.getTileEntity(pos) instanceof TileCubeDispenser))
			return;
		TileCubeDispenser te = (TileCubeDispenser) world.getTileEntity(pos);

		if(te == null)
			return;

		ItemEntity entitem = te.getNewEntityItem(BlockCubeDispenser.getCurrentState(world.getBlockState(pos)));
		entitem.setLocationAndAngles(player.getPosX(), player.getPosY(), player.getPosZ(), 0, 0);
		if(player.isSneaking())
		{
			entitem.getItem().setCount(1);
			world.addEntity(entitem);
		}
		else
		{
			entitem.getItem().setCount(64);
			world.addEntity(entitem);
		}
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(DISPENSING);
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
		public String getString()
		{
			return this.type;
		}
	}

	public static DispenseType getCurrentState(BlockState state)
	{
		return state.get(DISPENSING);
	}
}