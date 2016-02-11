package chanceCubes.blocks;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketTriggerD20;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
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

	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public boolean isVisuallyOpaque()
	{
		return false;
	}

	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
	{
		this.startd20(world, pos, player);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return this.startd20(world, pos, player);
	}

	public boolean startd20(World world, BlockPos pos, EntityPlayer player)
	{
		if(world.isRemote || player == null || player instanceof FakePlayer)
			return false;

		TileChanceD20 te = (TileChanceD20) world.getTileEntity(pos);
		if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceIcosahedron), 1);
			((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
			super.dropBlockAsItem(world, pos, this.getDefaultState(), 1);
			world.setBlockToAir(pos);
			world.removeTileEntity(pos);
			return true;
		}

		if(te != null)
		{
			// te.startBreaking(player);
			CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketTriggerD20(pos.getX(), pos.getY(), pos.getZ()), new TargetPoint(world.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), 50));
			return true;
		}

		return false;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileChanceD20 d20 = (TileChanceD20) world.getTileEntity(pos);
		if(d20 == null)
			return state;
		TRSRTransformation transform = new TRSRTransformation(new Vector3f(0.5F, 0.5F + d20.wave * 0.1F, 0.5F), null, null, new Quat4f(0, 1, 0, d20.rotationInc));
		OBJModel.OBJState newState = new OBJModel.OBJState(Lists.newArrayList(OBJModel.Group.ALL), true, transform);
		return ((IExtendedBlockState) state).withProperty(OBJModel.OBJProperty.instance, newState);
	}

	@Override
	public BlockState createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { OBJModel.OBJProperty.instance });
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{

	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}
}