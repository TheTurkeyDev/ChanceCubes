package chanceCubes.util;

import chanceCubes.CCubesCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class CCubesDamageTypes
{
	public static final ResourceKey<DamageType> MATH_FAIL = register("mathdeath");
	public static final ResourceKey<DamageType> MAZE_FAIL = register("mazedeath");
	public static final ResourceKey<DamageType> QUESTION_FAIL = register("questiondeath");
	public static final ResourceKey<DamageType> DIG_BUILD_FAIL = register("digbuilddeath");
	public static final ResourceKey<DamageType> MATCHING_FAIL = register("matchingdeath");

//	public CCubesDamageSource(String id)
//	{
//		super(id);
//		super.bypassArmor();
//		super.bypassMagic();
//	}

	private static ResourceKey<DamageType> register(String id)
	{
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(CCubesCore.MODID, id));
	}
}