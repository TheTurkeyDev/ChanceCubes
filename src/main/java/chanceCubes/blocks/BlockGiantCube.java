package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.items.CCubesItems;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGiantCube extends BaseChanceBlock implements ITileEntityProvider
{
	public BlockGiantCube()
	{
		super("giant_chance_cube");
		this.setCreativeTab(null);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileGiantCube();
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isOpaqueCube(IBlockState blockState)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if(!world.isRemote && !(player instanceof FakePlayer))
		{
			TileGiantCube te = (TileGiantCube) world.getTileEntity(pos);

			if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
			{
				CCubesBlocks.COMPACT_GIANT_CUBE.dropBlockAsItem(world, pos, CCubesBlocks.COMPACT_GIANT_CUBE.getDefaultState(), 1);
				GiantCubeUtil.removeStructure(te.getMasterPostion(), world);
				return true;
			}

			if(te != null)
			{
				if(!te.hasMaster() || !te.checkForMaster())
				{
					world.setBlockToAir(pos);
					return false;
				}
				RewardsUtil.executeCommand(world, player, "/advancement grant @p only chancecubes:giant_chance_cube");
				GiantCubeRegistry.INSTANCE.triggerRandomReward(world, te.getMasterPostion(), player, 0);
				GiantCubeUtil.removeStructure(te.getMasterPostion(), world);
			}
		}
		return true;
	}
}