package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
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

@SuppressWarnings("deprecation")
public class BlockChanceCube extends BaseChanceBlock implements ITileEntityProvider
{
	public static final EnumProperty<BlockChanceCube.EnumTexture> TEXTURE = EnumProperty.<BlockChanceCube.EnumTexture> create("texture", BlockChanceCube.EnumTexture.class);

	public static EnumTexture textureToSet = EnumTexture.DEFAULT;

	public BlockChanceCube()
	{
		super(getBuilder().lightValue(2), "chance_cube");
		this.setDefaultState(this.stateContainer.getBaseState().with(TEXTURE, EnumTexture.DEFAULT));
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		return new TileChanceCube();
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, IFluidState fluid)
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
				EntityItem blockstack = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stackCube);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.removeTileEntity(pos);
				world.spawnEntity(blockstack);
			}

			if(te != null)
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				ChanceCubeRegistry.INSTANCE.triggerRandomReward(world, pos, player, tileCube.getChance());
			}
		}

		return removed;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		GiantCubeUtil.checkMultiBlockForm(pos, worldIn, true);
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		IBlockState iblockstate = super.getStateForPlacement(context);
		return iblockstate.with(TEXTURE, textureToSet);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(TEXTURE);
	}

	public static enum EnumTexture implements IStringSerializable
	{
		// @formatter:off
		DEFAULT("default"), VALENTINES("valentines"), STPATRICKS("stpatricks"), HALLOWEEN("halloween"),
		HOLIDAYS("holidays"), EASTER("easter");
		// @formatter:on

		private final String name;

		private EnumTexture(String name)
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