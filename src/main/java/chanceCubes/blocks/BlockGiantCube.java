package chanceCubes.blocks;

import chanceCubes.items.CCubesItems;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
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

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileGiantCube();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
	{
		TileEntity te = world.getTileEntity(pos);
		if(!(te instanceof TileGiantCube))
			return VoxelShapes.fullCube();

		TileGiantCube gc = (TileGiantCube) te;
		BlockPos diff = pos.subtract(gc.getMasterPostion());
		int xDiff = (diff.getX() * 16);
		int yDiff = (diff.getY() * 16);
		int zDiff = (diff.getZ() * 16);
		return Block.makeCuboidShape(-16 - xDiff, -16 - yDiff, -16 - zDiff, 32 - xDiff, 32 - yDiff, 32 - zDiff);
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid)
	{
		TileEntity te = world.getTileEntity(pos);
		boolean removed = super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		if(!world.isRemote && !(player instanceof FakePlayer))
		{
			if(te != null)
			{
				TileGiantCube gcte = (TileGiantCube) te;
				if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
				{
					spawnAsEntity(world, pos, new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE));
					GiantCubeUtil.removeStructure(gcte.getMasterPostion(), world);
					return true;
				}

				if(!gcte.hasMaster() || !gcte.checkForMaster())
				{
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				RewardsUtil.executeCommand(world, player, player.getPosition(), "/advancement grant @p only chancecubes:giant_chance_cube");
				GlobalCCRewardRegistry.GIANT.triggerRandomReward(world, gcte.getMasterPostion(), player, 0);
				GiantCubeUtil.removeStructure(gcte.getMasterPostion(), world);
			}
		}
		return removed;
	}
}