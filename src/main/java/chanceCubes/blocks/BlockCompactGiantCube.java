package chanceCubes.blocks;

import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCompactGiantCube extends BaseChanceBlock
{

	public BlockCompactGiantCube()
	{
		super("compact_Giant_Chance_Cube");
		this.setHardness(0.5f);
	}

	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if(world.isRemote)
			return;
		for(int xx = pos.getX() - 1; xx <= pos.getX() + 1; xx++)
		{
			for(int zz = pos.getZ() - 1; zz <= pos.getZ() + 1; zz++)
			{
				for(int yy = pos.getY(); yy <= pos.getY() + 2; yy++)
				{
					BlockPos newPos = new BlockPos(xx, yy, zz);
					world.setBlockState(newPos, CCubesBlocks.chanceGiantCube.getDefaultState());
					TileEntity tile = world.getTileEntity(newPos);
					boolean master = (xx == pos.getX() && yy == pos.getY() + 1 && pos.getZ() == zz);
					if(tile != null && (tile instanceof TileGiantCube))
					{
						((TileGiantCube) tile).setMasterCoords(pos.add(0, 1, 0));
						((TileGiantCube) tile).setHasMaster(true);
						((TileGiantCube) tile).setIsMaster(master);
					}
				}
			}
		}
		//TODO: UPDATE SOUNDS
		//world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvent., SoundCategory.BLOCKS, 1f, 1f, false);
		//world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), CCubesCore.MODID + ":giant_Cube_Spawn", 1, 1);
	}
}