package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGen
{
	//I have no idea what I'm doing. Please help me...

	public static final ConfiguredFeature<BlockClusterFeatureConfig, ?> CHANCE_CUBE_SURFACE_FEATURE = Feature.RANDOM_PATCH.withConfiguration(
			(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(CCubesBlocks.CHANCE_CUBE.getDefaultState()), new SimpleBlockPlacer()))
					.tries(64)
					.xSpread(1)
					.ySpread(128)
					.zSpread(1)
					.build()
	);

	public static final ConfiguredFeature<OreFeatureConfig, ?> CHANCE_CUBE_ORE_FEATURE = Feature.ORE.withConfiguration(
			new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, CCubesBlocks.CHANCE_CUBE.getDefaultState(), 1)
	);

	public static void initWorldGen()
	{
		Registry<ConfiguredFeature<?, ?>> r = WorldGenRegistries.CONFIGURED_FEATURE;
		Registry.register(r, new ResourceLocation(CCubesCore.MODID, "chance_cube_surface"), CHANCE_CUBE_SURFACE_FEATURE);
		Registry.register(r, new ResourceLocation(CCubesCore.MODID, "chance_cube_ore"), CHANCE_CUBE_ORE_FEATURE);
	}

	@SubscribeEvent
	public void onBiomeLoadingEvent(BiomeLoadingEvent event)
	{
		if(CCubesSettings.surfaceGeneration.get())
			event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, CHANCE_CUBE_SURFACE_FEATURE);
		if(CCubesSettings.oreGeneration.get())
			event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, CHANCE_CUBE_SURFACE_FEATURE);
	}
}