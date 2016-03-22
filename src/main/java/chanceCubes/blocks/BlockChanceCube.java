package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class BlockChanceCube extends BaseChanceBlock implements ITileEntityProvider
{

	public BlockChanceCube()
	{
		super("chance_Cube");
		this.setLightLevel(2);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileChanceCube();
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			TileChanceCube te = (TileChanceCube) world.getTileEntity(pos);
			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
			{
				ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceCube), 1);
				((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
				EntityItem blockstack = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.setBlockToAir(pos);
				world.removeTileEntity(pos);
				world.spawnEntityInWorld(blockstack);
				return true;
			}

			if(te != null)
			{
				world.setBlockToAir(pos);
				ChanceCubeRegistry.INSTANCE.triggerRandomReward(world, pos, player, te.getChance());
			}
		}
		return true;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 0;
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
	{

	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if(GiantCubeUtil.checkMultiBlockForm(pos, world))
		{
			GiantCubeUtil.setupStructure(pos, world);
		}
	}
}