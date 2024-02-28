package chanceCubes.util;

import chanceCubes.CCubesCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class StatsRegistry {
	public static final DeferredRegister<ResourceLocation> CUSTOM_STAT = DeferredRegister.create(BuiltInRegistries.CUSTOM_STAT, CCubesCore.MODID);

	public static final String OPENED_CHANCE_CUBE_NAME = "opened_chance_cube";
	public static final String OPENED_GIANT_CHANCE_CUBE_NAME = "opened_giant_chance_cube";
	public static final String OPENED_D20_NAME = "opened_d20";

	public static Supplier<ResourceLocation> OPENED_CHANCE_CUBE = makeCustomStat(OPENED_CHANCE_CUBE_NAME);
	public static Supplier<ResourceLocation> OPENED_GIANT_CHANCE_CUBE = makeCustomStat(OPENED_GIANT_CHANCE_CUBE_NAME);
	public static Supplier<ResourceLocation> OPENED_D20 = makeCustomStat(OPENED_D20_NAME);

	private static Supplier<ResourceLocation> makeCustomStat(String pKey) {
		ResourceLocation resourcelocation = new ResourceLocation(CCubesCore.MODID, pKey);
		return CUSTOM_STAT.register(pKey, () -> resourcelocation);
	}
}