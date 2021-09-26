package chanceCubes.blocks;

import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BlockCubeDispenser extends BaseChanceBlock implements EntityBlock
{
	public static final EnumProperty<DispenseType> DISPENSING = EnumProperty.create("dispensing", BlockCubeDispenser.DispenseType.class);

	public BlockCubeDispenser()
	{
		super(getBuilder().strength(2f, Integer.MAX_VALUE), "cube_dispenser");
		this.registerDefaultState(this.getStateDefinition().any().setValue(DISPENSING, DispenseType.CHANCE_CUBE));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileCubeDispenser(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_)
	{
		if(level.isClientSide())
			return InteractionResult.PASS;
		if(!(level.getBlockEntity(pos) instanceof TileCubeDispenser te))
			return InteractionResult.PASS;

		if(player.isCrouching())
		{
			state = state.cycle(DISPENSING);
			level.setBlockAndUpdate(pos, state);
		}
		else
		{
			Block block = Block.byItem(player.getInventory().getSelected().getItem());
			if(block.equals(te.getCurrentBlock(BlockCubeDispenser.getCurrentState(state))))
				player.getInventory().getSelected().shrink(1);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void attack(BlockState state, Level level, BlockPos pos, Player player)
	{
		if(level.isClientSide())
			return;
		if(!(level.getBlockEntity(pos) instanceof TileCubeDispenser te))
			return;

		ItemEntity entItem = te.getNewEntityItem(BlockCubeDispenser.getCurrentState(state));
		entItem.moveTo(player.getX(), player.getY(), player.getZ(), 0, 0);
		entItem.getItem().setCount(player.isCrouching() ? 1 : 64);
		level.addFreshEntity(entItem);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(DISPENSING);
	}

	public enum DispenseType implements StringRepresentable
	{
		CHANCE_CUBE("chance_cube"), CHANCE_ICOSAHEDRON("chance_icosahedron"), COMPACT_GIANTCUBE("compact_giant_cube");

		private final String type;

		DispenseType(String name)
		{
			this.type = name;
		}


		@Override
		public String getSerializedName()
		{
			return this.type;
		}
	}

	public static DispenseType getCurrentState(BlockState state)
	{
		return state.getValue(DISPENSING);
	}
}