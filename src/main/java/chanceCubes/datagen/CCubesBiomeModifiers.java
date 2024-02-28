package chanceCubes.datagen;

import chanceCubes.CCubesCore;
import chanceCubes.listeners.WorldGen;
import chanceCubes.modifier.AddCCubesFeaturesBiomeModifier;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class CCubesBiomeModifiers
{
	public static ResourceKey<BiomeModifier> CC_SURFACE = createKey("chance_cube_worldgen");
	public static ResourceKey<BiomeModifier> CC_ORE = createKey("chance_cube_oregen");

	private static ResourceKey<BiomeModifier> createKey(String id)
	{
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CCubesCore.MODID, id));
	}

	public static void bootstrap(BootstapContext<BiomeModifier> context)
	{
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedGetter = context.lookup(Registries.PLACED_FEATURE);

		context.register(CC_SURFACE, new AddCCubesFeaturesBiomeModifier(
				0,
				biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(placedGetter.getOrThrow(WorldGen.CC_SURFACE)),
				GenerationStep.Decoration.TOP_LAYER_MODIFICATION));

		context.register(CC_ORE, new AddCCubesFeaturesBiomeModifier(
				1,
				biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(placedGetter.getOrThrow(WorldGen.CC_ORE)),
				GenerationStep.Decoration.UNDERGROUND_ORES));
	}
}
