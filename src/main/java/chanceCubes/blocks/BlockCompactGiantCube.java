package chanceCubes.blocks;

import chanceCubes.config.CCubesSettings;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCompactGiantCube extends BaseChanceBlock
{
	public BlockCompactGiantCube()
	{
		super(getBuilder(), "compact_giant_chance_cube");
	}

	public void onBlockPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		super.onBlockPlacedBy(level, pos, state, placer, stack);
		if(level.isClientSide() || (RewardsUtil.isBlockUnbreakable(level, pos.offset(0, 1, 0)) && CCubesSettings.nonReplaceableBlocks.contains(level.getBlockState(pos.add(0, 1, 0)))))
			return;

		GiantCubeUtil.setupStructure(pos.offset(-1, 0, -1), level, true);

		level.playSound(null, pos, CCubesSounds.GIANT_CUBE_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}