package chanceCubes.config;

import chanceCubes.rewards.defaultRewards.IChanceCubeReward;

public class CCubesSettings
{
	public static int d20RenderID = -1;

	public static int pendantUses = 100;

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

	public static boolean holidayRewards = true;
	public static boolean holidayRewardTriggered = false;
	public static boolean doesHolidayRewardTrigger = false;
	public static IChanceCubeReward holidayReward = null;
	public static boolean hasHolidayTexture = false;
	public static String holidayTextureName = "";

	public static int dropHeight = 20;

	public static String rewardURL = "https://raw.githubusercontent.com/wyldmods/ChanceCubes/master/customRewardsV2";

	public static boolean isBlockedWorld(String world)
	{
		for(String blockedWorld : blockedWorlds)
			if(blockedWorld.equalsIgnoreCase(world))
				return true;
		return false;
	}
}