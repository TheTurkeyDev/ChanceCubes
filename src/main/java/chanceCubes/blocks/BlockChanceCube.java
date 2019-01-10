package chanceCubes.blocks;

import javax.annotation.Nullable;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class BlockChanceCube extends BaseChanceBlock implements ITileEntityProvider
{
	public static final EnumProperty<BlockChanceCube.EnumTexture> TEXTURE = EnumProperty.<BlockChanceCube.EnumTexture> create("texture", BlockChanceCube.EnumTexture.class);

	public static EnumTexture textureToSet = EnumTexture.DEFAULT;

	public BlockChanceCube()
	{
		super(getBuilder(), "chance_Cube");
		//this.setLightLevel(2);
		//this.setDefaultState(this.blockState.getBaseState().withProperty(TEXTURE, textureToSet));
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader paramIBlockReader)
	{
		return new TileChanceCube();
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer) && te instanceof TileChanceCube)
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
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		GiantCubeUtil.checkMultiBlockForm(pos, worldIn, true);
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		IBlockState iblockstate = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
		return iblockstate.with(TEXTURE, textureToSet);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().with(TEXTURE, textureToSet);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	public IBlockState getActualState(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return this.getDefaultState().with(TEXTURE, textureToSet);
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { TEXTURE });
	}

	public static enum EnumTexture implements IStringSerializable
	{
		// @formatter:off
		DEFAULT("default"), VALENTINES("valentines"), STPATRICKS("stpatricks"), 
		HALLOWEEN("halloween"), HOLIDAYS("holidays"), EASTER("easter");
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