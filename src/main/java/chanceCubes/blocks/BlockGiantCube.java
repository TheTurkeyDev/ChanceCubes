package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class BlockGiantCube extends BaseChanceBlock implements ITileEntityProvider
{
	public BlockGiantCube()
	{
		super(getBuilder(), "giant_chance_cube");
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		return new TileGiantCube();
	}

	public static boolean isOpaque(VoxelShape shape)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, IFluidState fluid)
	{
		TileEntity te = world.getTileEntity(pos);
		boolean removed = super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		if(removed && !world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			if(te != null)
			{
				TileGiantCube gcte = (TileGiantCube) te;
				if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
				{
					spawnAsEntity(world, pos, new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE));
					GiantCubeUtil.removeStructure(gcte.getMasterPostion(), world);
				}

				if(!gcte.hasMaster() || !gcte.checkForMaster())
				{
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				RewardsUtil.executeCommand(world, player, player.getPositionVector(), "/advancement grant @p only chancecubes:giant_chance_cube");
				GiantCubeRegistry.INSTANCE.triggerRandomReward(world, gcte.getMasterPostion(), player, 0);
				GiantCubeUtil.removeStructure(gcte.getMasterPostion(), world);
			}
		}
		return removed;
	}
}