package chanceCubes.blocks;

import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
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
		GiantCubeUtil.setupStructure(pos.add(-1, 0, -1), world, true);
		
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), CCubesSounds.GIANT_CUBE_SPAWN.getSoundEvent(), CCubesSounds.GIANT_CUBE_SPAWN.getSoundCategory(), 1.0F, 1.0F);
	}
}