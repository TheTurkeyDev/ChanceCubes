package chanceCubes.worldgen;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.util.RewardsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class CCSurfaceFeature extends Feature<NoneFeatureConfiguration>
{
	public CCSurfaceFeature()
	{
		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		if(!CCubesSettings.surfaceGeneration.get())
			return false;

		if(RewardsUtil.rand.nextInt(CCubesSettings.surfaceGenAmount.get()) != 0)
			return false;

		BlockPos pos = context.origin();
		WorldGenLevel level = context.level();

		int x = pos.getX() + RewardsUtil.rand.nextInt(16);
		int z = pos.getZ() + RewardsUtil.rand.nextInt(16);
		int y = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, 0, z)).getY() - 1;
		pos = new BlockPos(x, y, z);

		BlockState bs = level.getBlockState(pos);

		if(!bs.getMaterial().isReplaceable() || bs.is(Blocks.WATER))
			return false;

		level.setBlock(pos, CCubesBlocks.CHANCE_CUBE.get().defaultBlockState(), 3);

		return true;
	}
}