package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.worldgen.CCSurfaceFeature;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class WorldGen
{
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, CCubesCore.MODID);
	private static final ResourceLocation CC_SURFACE_ID = new ResourceLocation(CCubesCore.MODID, "chance_cube_worldgen");
	private static final ResourceLocation CC_ORE_ID = new ResourceLocation(CCubesCore.MODID, "chance_cube_oregen");
	private static final Supplier<Feature<NoneFeatureConfiguration>> CC_SURFACE_FEATURE = FEATURES.register("chance_cube_worldgen", CCSurfaceFeature::new);


	public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_CC_SURFACE = FeatureUtils.createKey(CC_SURFACE_ID.toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_CC_ORE = FeatureUtils.createKey(CC_ORE_ID.toString());

	public static void configuredBootstrap(BootstapContext<ConfiguredFeature<?, ?>> context)
	{
		FeatureUtils.register(context, CONFIGURED_CC_SURFACE, WorldGen.CC_SURFACE_FEATURE.get());

		RuleTest ruletest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest ruletest2 = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
		List<OreConfiguration.TargetBlockState> ccOreTarget = List.of(OreConfiguration.target(ruletest, CCubesBlocks.CHANCE_CUBE.get().defaultBlockState()), OreConfiguration.target(ruletest2, CCubesBlocks.CHANCE_CUBE.get().defaultBlockState()));
		FeatureUtils.register(context, CONFIGURED_CC_ORE, Feature.ORE, new OreConfiguration(ccOreTarget, 4));
	}

	public static ResourceKey<PlacedFeature> CC_SURFACE = PlacementUtils.createKey(CC_SURFACE_ID.toString());
	public static ResourceKey<PlacedFeature> CC_ORE = PlacementUtils.createKey(CC_ORE_ID.toString());

	public static void placedBootstrap(BootstapContext<PlacedFeature> context)
	{
		HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

		PlacementUtils.register(context, CC_SURFACE, holdergetter.getOrThrow(CONFIGURED_CC_SURFACE));
		PlacementUtils.register(context, CC_ORE, holdergetter.getOrThrow(CONFIGURED_CC_ORE), commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192))));
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier1)
	{
		return List.of(modifier, InSquarePlacement.spread(), modifier1, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier)
	{
		return orePlacement(CountPlacement.of(count), modifier);
	}
}