package chanceCubes.config;

import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.NonreplaceableBlockOverride;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

import java.util.ArrayList;
import java.util.List;

public class CCubesSettings
{
	public static IntValue pendantUses;

	public static BooleanValue enableHardCodedRewards;
	public static BooleanValue disableGiantCC;

	public static IntValue rangeMin;
	public static IntValue rangeMax;
	public static BooleanValue d20UseNormalChances;
	public static BooleanValue rewardsEqualChance;

	public static BooleanValue oreGeneration;
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

	public static final List<BlockState> nonReplaceableBlocksIMC = new ArrayList<>();
	public static List<BlockState> nonReplaceableBlocks = new ArrayList<>();
	public static final List<NonreplaceableBlockOverride> nonReplaceableBlocksOverrides = new ArrayList<>();
	public static final List<BlockState> backupNRB = new ArrayList<>();

	public static ConfigValue<List<? extends String>> blockRestoreBlacklist;

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