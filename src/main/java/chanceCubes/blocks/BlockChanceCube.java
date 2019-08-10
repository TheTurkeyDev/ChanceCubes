package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class BlockChanceCube extends BaseChanceBlock
{
	public static final EnumProperty<BlockChanceCube.EnumTexture> TEXTURE = EnumProperty.create("texture", BlockChanceCube.EnumTexture.class);

	public static EnumTexture textureToSet = EnumTexture.DEFAULT;

	public BlockChanceCube()
	{
		super(getBuilder().lightValue(2), "chance_cube");
		this.setDefaultState(this.stateContainer.getBaseState().with(TEXTURE, EnumTexture.DEFAULT));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileChanceCube();
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid)
	{
		TileEntity te = world.getTileEntity(pos);
		boolean removed = super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		if(removed && !world.isRemote() && player != null && !(player instanceof FakePlayer) && te instanceof TileChanceCube)
		{
			TileChanceCube tileCube = (TileChanceCube) te;
			if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
			{
				ItemStack stackCube = new ItemStack(Item.getItemFromBlock(CCubesBlocks.CHANCE_CUBE), 1);
				((ItemChanceCube) stackCube.getItem()).setChance(stackCube, tileCube.isScanned() ? tileCube.getChance() : -101);
				ItemEntity blockstack = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stackCube);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.removeTileEntity(pos);
				world.addEntity(blockstack);
			}

			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			ChanceCubeRegistry.INSTANCE.triggerRandomReward(world, pos, player, tileCube.getChance());
		}

		return removed;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		GiantCubeUtil.checkMultiBlockForm(pos, worldIn, true);
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState iblockstate = super.getStateForPlacement(context);
		return iblockstate.with(TEXTURE, textureToSet);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(TEXTURE);
	}

	public enum EnumTexture implements IStringSerializable
	{
		// @formatter:off
		DEFAULT("default"), VALENTINES("valentines"), STPATRICKS("stpatricks"), HALLOWEEN("halloween"),
		HOLIDAYS("holidays"), EASTER("easter");
		// @formatter:on

		private final String name;

		EnumTexture(String name)
		{
			this.name = name;
		}

		public String toString()
		{
			return this.name;
		}

		public String getName()
		{
			return this.name;
		}
	}
}