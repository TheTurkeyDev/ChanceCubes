package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketTriggerD20;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.util.CCubesAchievements;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class BlockChanceD20 extends BaseChanceBlock implements ITileEntityProvider
{

	public BlockChanceD20()
	{
		super("chance_Icosahedron");
		super.setHardness(-1F);
		this.setLightLevel(7);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileChanceD20();
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isVisuallyOpaque()
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	/*
	 * @Override public EnumWorldBlockLayer getBlockLayer() { return EnumWorldBlockLayer.CUTOUT_MIPPED; }
	 */

	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
	{
		this.startd20(world, pos, player);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return this.startd20(world, pos, player);
	}

	public boolean startd20(World world, BlockPos pos, EntityPlayer player)
	{
		if(world.isRemote || player == null || player instanceof FakePlayer)
			return true;

		TileChanceD20 te = (TileChanceD20) world.getTileEntity(pos);
		if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.CHANCE_ICOSAHEDRON), 1);
			((ItemChanceCube) stack.getItem()).setChance(stack, te.isScanned() ? te.getChance() : -101);
			super.dropBlockAsItem(world, pos, this.getDefaultState(), 1);
			world.setBlockToAir(pos);
			world.removeTileEntity(pos);
			return true;
		}

		if(te != null)
		{
			player.addStat(CCubesAchievements.chanceIcosahedron);
			te.startBreaking(player);
			CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketTriggerD20(pos.getX(), pos.getY(), pos.getZ()), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 50));
			return true;
		}

		return false;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);

		if(tile != null && tile instanceof TileChanceD20)
		{
			TileChanceD20 d20 = (TileChanceD20) tile;
			if(d20.transform != TRSRTransformation.identity())
				return ((IExtendedBlockState) state).withProperty(Properties.AnimationProperty, d20.transform);
		}

		return state;
	}

	@Override
	public ExtendedBlockState createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { Properties.AnimationProperty });
	}
}