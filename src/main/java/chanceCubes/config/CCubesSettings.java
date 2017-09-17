package chanceCubes.config;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.NonreplaceableBlockOverride;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class CCubesSettings
{
	public static int d20RenderID = -1;

	public static int pendantUses = 32;

	public static boolean enableHardCodedRewards = true;

	public static int rangeMin = 20;
	public static int rangeMax = 20;
	public static boolean d20UseNormalChances = false;

	public static boolean oreGeneration = true;
	public static int oreGenAmount = 4;
	public static boolean surfaceGeneration = true;
	public static int surfaceGenAmount = 1;
	public static String[] blockedWorlds = new String[] {};
	public static boolean chestLoot = true;
	public static boolean craftingRecipie = true;

	public static boolean userSpecificRewards = true;
	public static boolean disabledRewards = true;

	public static boolean holidayRewards = true;
	public static boolean holidayRewardTriggered = false;
	public static boolean doesHolidayRewardTrigger = false;
	public static IChanceCubeReward holidayReward = null;
	public static boolean hasHolidayTexture = false;
	public static String holidayTextureName = "";

	public static int dropHeight = 20;

	public static List<IBlockState> nonReplaceableBlocksIMC = new ArrayList<IBlockState>();
	public static List<IBlockState> nonReplaceableBlocks = new ArrayList<IBlockState>();
	public static List<NonreplaceableBlockOverride> nonReplaceableBlocksOverrides = new ArrayList();
	public static List<IBlockState> BackupNRB = new ArrayList<>();

	public static boolean testRewards;
	public static boolean testCustomRewards;
	public static int testingRewardIndex = 0;

	public static boolean isBlockedWorld(String world)
	{
		for(String blockedWorld : blockedWorlds)
			if(blockedWorld.equalsIgnoreCase(world))
				return true;
		return false;
	}
}