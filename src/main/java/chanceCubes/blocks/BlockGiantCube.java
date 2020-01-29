package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class BlockGiantCube extends BaseChanceBlock
{
	public BlockGiantCube()
	{
		super(getBuilder(), "giant_chance_cube");
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileGiantCube();
	}


	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid)
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