package chanceCubes.datagen;

import chanceCubes.util.CCubesDamageTypes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageType;

public class CCubesDamageTypeProvider {
	public static void bootstrap(BootstapContext<DamageType> context)
	{
		context.register(CCubesDamageTypes.MATH_FAIL, new DamageType("mathdeath", 0.0F));
		context.register(CCubesDamageTypes.MAZE_FAIL, new DamageType("mazedeath", 0.0F));
		context.register(CCubesDamageTypes.QUESTION_FAIL, new DamageType("questiondeath", 0.0F));
		context.register(CCubesDamageTypes.DIG_BUILD_FAIL, new DamageType("digbuilddeath", 0.0F));
		context.register(CCubesDamageTypes.MATCHING_FAIL, new DamageType("matchingdeath", 0.0F));
	}
}
