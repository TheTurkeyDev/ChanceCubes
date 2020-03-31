package chanceCubes.blocks;

import chanceCubes.config.CCubesSettings;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCompactGiantCube extends BaseChanceBlock
{
	public BlockCompactGiantCube()
	{
		super(getBuilder(), "compact_giant_chance_cube");
	}

	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if(world.isRemote || (RewardsUtil.isBlockUnbreakable(world, pos.add(0, 1, 0)) && CCubesSettings.nonReplaceableBlocks.contains(world.getBlockState(pos.add(0, 1, 0)))))
			return;

		GiantCubeUtil.setupStructure(pos.add(-1, 0, -1), world, true);

		//TODO: Not working
		world.playSound(null, pos, CCubesSounds.GIANT_CUBE_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}