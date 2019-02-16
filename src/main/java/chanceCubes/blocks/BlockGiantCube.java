package chanceCubes.blocks;

import javax.annotation.Nullable;

import chanceCubes.items.CCubesItems;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
		super(getBuilder(), "giant_Chance_Cube");
		//this.setCreativeTab(null);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader paramIBlockReader)
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
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			if(te != null)
			{
				TileGiantCube gcte = (TileGiantCube) world.getTileEntity(pos);
				if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
				{
					spawnAsEntity(world, pos, new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE));
					GiantCubeUtil.removeStructure(gcte.getMasterPostion(), world);
				}

				if(!gcte.hasMaster() || !gcte.checkForMaster())
				{
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				RewardsUtil.executeCommand(world, player, "/advancement grant @p only chancecubes:giant_chance_cube");
				GiantCubeRegistry.INSTANCE.triggerRandomReward(world, gcte.getMasterPostion(), player, 0);
				GiantCubeUtil.removeStructure(gcte.getMasterPostion(), world);
			}
		}
	}
}