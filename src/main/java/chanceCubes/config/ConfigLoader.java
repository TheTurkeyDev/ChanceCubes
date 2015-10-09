package chanceCubes.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigLoader 
{
	public static Configuration config;
	public static final String genCat = "General Settings";
	public static final String rewardCat = "Rewards";
	
	public static void loadConfigSettings(File file, File resources)
	{
		File fileFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes");
		fileFolder.mkdirs();
		config = new Configuration(new File(fileFolder + "/" + file.getName()));
		config.load();
		
		CCubesSettings.rangeMin = config.getInt("chanceRangeMin", genCat,  25, 0, 100, "The minimum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your range set to default 75. A chance cube with a chance value of 0 can get rewards of -75 to 75 in chance value.");
		CCubesSettings.rangeMax = config.getInt("chanceRangeMax", genCat,  25, 0, 100, "The maximum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your range set to default 75. A chance cube with a chance value of 0 can get rewards of -75 to 75 in chance value.");
		
		CCubesSettings.enableHardCodedRewards = config.getBoolean("EnableDefaultRewards", genCat, true, "Set to true if the default rewards should be loaded, false if they shouldn't");
		
		CCubesSettings.pendantUses = config.getInt("pendantUses", genCat, CCubesSettings.pendantUses, 0, 1000, "Number of uses for a pendant");
		CCubesSettings.oreGeneration = config.getBoolean("GenerateAsOre", genCat, true, "true if Chance Cubes should generate like ores with in the world. false if they should not");
		CCubesSettings.surfaceGeneration = config.getBoolean("GenerateOnSurface", genCat, true, "true if Chance Cubes should generate on the surface of the world. false if they should not");
		CCubesSettings.blockedWorlds = config.getStringList("BlockedWorlds", genCat, new String[0], "Worlds that Chance cubes shold not generate in");
		
		CCubesSettings.rangeMax = config.getInt("chanceRangeMax", genCat,  20, 0, 256, "How many blocks above the Chance Cube that a block that will fall should be droped from");
		
		CCubesSettings.userSpecificRewards = config.getBoolean("UserSpecificRewards", genCat, true, "true if Chance Cubes should load in user specific rewards (for a select few only)");
		CCubesSettings.holidayRewards = config.getBoolean("HolidayRewards", genCat, true, "Set to false if you wish to disable the super special holiday rewards. Why would you want to do that?");
		CCubesSettings.holidayRewardTriggered = config.getBoolean("HolidayRewardTriggered", genCat, false, "Don't touch! Well I mean you can touch it, if you want. I can't stop you. I'm only text.");
		
		config.save();
		
		File customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards");
		customConfigFolder.mkdirs();
		
		new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards/Schematics").mkdirs();
		new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards/Sounds").mkdirs();
		
		new CustomRewardsLoader(customConfigFolder, resources);
	}
}