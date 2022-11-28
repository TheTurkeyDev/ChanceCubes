package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.worldgen.CCSurfaceFeature;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WorldGen
{
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CCubesCore.MODID);
	private static final ResourceLocation CC_SURFACE_ID = new ResourceLocation(CCubesCore.MODID, "chance_cube_worldgen");
	private static final ResourceLocation CC_ORE_ID = new ResourceLocation(CCubesCore.MODID, "chance_cube_oregen");
	private static final RegistryObject<Feature<NoneFeatureConfiguration>> CC_SURFACE_FEATURE = FEATURES.register("chance_cube_worldgen", CCSurfaceFeature::new);
	private static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> CC_SURFACE_CONF = FeatureUtils.register(WorldGen.CC_SURFACE_ID.toString(), WorldGen.CC_SURFACE_FEATURE.get());
	public static final Holder<PlacedFeature> CC_SURFACE = PlacementUtils.register(WorldGen.CC_SURFACE_ID.toString(), WorldGen.CC_SURFACE_CONF);
	private static final List<OreConfiguration.TargetBlockState> CC_ORE_TARGET = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, CCubesBlocks.CHANCE_CUBE.get().defaultBlockState()));
	private static final Holder<ConfiguredFeature<OreConfiguration, ?>> CC_ORE_CONF = FeatureUtils.register(CC_ORE_ID.toString(), Feature.ORE, new OreConfiguration(CC_ORE_TARGET, 100));
	public static Holder<PlacedFeature> CC_ORE = PlacementUtils.register(CC_ORE_ID.toString(), CC_ORE_CONF, commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192))));


	private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_)
	{
		return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_)
	{
		return orePlacement(CountPlacement.of(p_195344_), p_195345_);
	}
}