package chanceCubes.config;

import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.NonreplaceableBlockOverride;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.ArrayList;
import java.util.List;

public class CCubesSettings
{
	public static IntValue pendantUses;

	public static BooleanValue enableHardCodedRewards;

	public static IntValue rangeMin;
	public static IntValue rangeMax;
	public static BooleanValue d20UseNormalChances;
	public static BooleanValue rewardsEqualChance;

	public static BooleanValue oreGeneration;
	public static IntValue oreGenAmount;
	public static BooleanValue surfaceGeneration;
	public static IntValue surfaceGenAmount;
	public static ConfigValue<List<? extends String>> blockedWorlds;
	public static BooleanValue chestLoot;

	public static BooleanValue userSpecificRewards;
	public static BooleanValue disabledRewards;

	public static BooleanValue holidayRewards;
	public static BooleanValue holidayRewardTriggered;
	public static boolean doesHolidayRewardTrigger = false;
	public static IChanceCubeReward holidayReward = null;
	public static boolean hasHolidayTexture = false;
	public static String holidayTextureName = "";

	public static IntValue dropHeight;

	public static List<BlockState> nonReplaceableBlocksIMC = new ArrayList<>();
	public static List<BlockState> nonReplaceableBlocks = new ArrayList<>();
	public static List<NonreplaceableBlockOverride> nonReplaceableBlocksOverrides = new ArrayList<>();
	public static List<BlockState> backupNRB = new ArrayList<>();

	public static boolean testRewards;
	public static boolean testCustomRewards;
	public static int testingRewardIndex = 0;

	public static boolean isBlockedWorld(String world)
	{
		for(String blockedWorld : blockedWorlds.get())
			if(blockedWorld.equalsIgnoreCase(world))
				return true;
		return false;
	}
}