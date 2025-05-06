package chanceCubes.modifier;

import chanceCubes.CCubesCore;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CCubesModifiers
{
	public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, CCubesCore.MODID);

	public static final Supplier<MapCodec<AddCCubesFeaturesBiomeModifier>> ADD_CCUBES_FEATURES_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_chancecubes_features", () ->
			RecordCodecBuilder.mapCodec(builder -> builder.group(
					Codec.intRange(0, 1).fieldOf("id").forGetter(AddCCubesFeaturesBiomeModifier::id),
					Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddCCubesFeaturesBiomeModifier::biomes),
					PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddCCubesFeaturesBiomeModifier::features),
					GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddCCubesFeaturesBiomeModifier::step)
			).apply(builder, AddCCubesFeaturesBiomeModifier::new))
	);
}
