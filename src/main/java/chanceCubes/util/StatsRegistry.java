package chanceCubes.util;

import chanceCubes.CCubesCore;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class StatsRegistry {
	public static final String OPENED_CHANCE_CUBE_NAME = "opened_chance_cube";
	public static final String OPENED_GIANT_CHANCE_CUBE_NAME = "opened_giant_chance_cube";
	public static final String OPENED_D20_NAME = "opened_d20";
	public static ResourceLocation OPENED_CHANCE_CUBE;
	public static ResourceLocation OPENED_GIANT_CHANCE_CUBE;
	public static ResourceLocation OPENED_D20;

	public static void init() {
		OPENED_CHANCE_CUBE = makeCustomStat(OPENED_CHANCE_CUBE_NAME);
		OPENED_GIANT_CHANCE_CUBE = makeCustomStat(OPENED_GIANT_CHANCE_CUBE_NAME);
		OPENED_D20 = makeCustomStat(OPENED_D20_NAME);
	}

	private static ResourceLocation makeCustomStat(String pKey) {
		ResourceLocation resourcelocation = new ResourceLocation(CCubesCore.MODID, pKey);
		Registry.register(Registry.CUSTOM_STAT, pKey, resourcelocation);
		Stats.CUSTOM.get(resourcelocation, StatFormatter.DEFAULT);
		return resourcelocation;
	}
}