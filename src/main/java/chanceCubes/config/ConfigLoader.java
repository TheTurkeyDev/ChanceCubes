package chanceCubes.config;

import java.io.File;

import chanceCubes.profiles.ProfileManager;
import chanceCubes.util.NonreplaceableBlockOverride;
import net.minecraftforge.common.config.Configuration;

/**
 * Handles Configuration file management
 */
public class ConfigLoader
{
	public static Configuration config;
	public static final String genCat = "General Settings";
	public static final String rewardCat = "Rewards";
	public static final String giantRewardCat = "Giant Chance Cube Rewards";

	public static File folder;
	public static File forgeSuggestedCfgFile;

	/**
	 * Initializes and loads ChanceCubes settings from the config file. <br>
	 * <br>
	 * <b>Do not use outside of postInit eventhandler</b>, use {@link #reloadConfigSettings()}
	 * instead.
	 * 
	 * @param file
	 *            The default configuration file suggested by forge.
	 */
	public static void loadConfigSettings(File file)
	{
		forgeSuggestedCfgFile = file;
		folder = new File(forgeSuggestedCfgFile.getParentFile().getAbsolutePath() + "/ChanceCubes");
		folder.mkdirs();
		config = new Configuration(new File(folder + "/" + forgeSuggestedCfgFile.getName()));
		config.load();

		config.setCategoryComment(rewardCat, "Set to false to disable a specific reward");

		CCubesSettings.nonReplaceableBlocksOverrides = NonreplaceableBlockOverride.parseStrings(config.getStringList("nonreplaceableBlockOverrides", genCat, new String[] { "minecraft:bedrock" }, "Blocks that ChanceCube rewards will be unable to replace or remove, can override IMC-added blocks by prefacing the block ID with \'-\'."));
		CCubesSettings.rangeMin = config.getInt("chanceRangeMin", genCat, 20, 0, 100, "The minimum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your range set to default 20. A chance cube with a chance value of 0 can get rewards of -20 to 20 in chance value.");
		CCubesSettings.rangeMax = config.getInt("chanceRangeMax", genCat, 20, 0, 100, "The maximum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your range set to default 20. A chance cube with a chance value of 0 can get rewards of -20 to 20 in chance value.");
		CCubesSettings.d20UseNormalChances = config.getBoolean("D20UseNormalChanceValues", genCat, false, "Set to true if the D20's should have any chance value from -100 to 100. Set to false to have the D20's only have a chance value of either -100 or 100");

		CCubesSettings.enableHardCodedRewards = config.getBoolean("EnableDefaultRewards", genCat, true, "Set to true if the default rewards should be loaded, false if they shouldn't");

		CCubesSettings.pendantUses = config.getInt("pendantUses", genCat, CCubesSettings.pendantUses, 0, 1000, "Number of uses for a pendant");
		CCubesSettings.oreGeneration = config.getBoolean("GenerateAsOre", genCat, true, "true if Chance Cubes should generate like ores with in the world. false if they should not");
		CCubesSettings.oreGenAmount = config.getInt("oreGenAmount", genCat, 4, 1, 100, "Amount of chance cubes to try and spawn, per chunk, as an ore");
		CCubesSettings.surfaceGeneration = config.getBoolean("GenerateOnSurface", genCat, true, "true if Chance Cubes should generate on the surface of the world. false if they should not");

		int defaultGen = 100;
		if(config.hasKey(genCat, "surfaceGenAmount"))
		{
			defaultGen = 100 / config.getInt("surfaceGenAmount", genCat, 1, 0, 100, "Percentage chance of a chunk to have a chance cube spawned on the surface. (OLD! REMOVE THIS CONFIG OPTION!)");
		}
		CCubesSettings.surfaceGenAmount = config.getInt("surfaceGenerationAmount", genCat, defaultGen, 0, Integer.MAX_VALUE, "Chance of a chunk to have a chance cube spawned on the surface. The math is 1/(surfaceGenerationAmount), so increase to make more rare, and decrese to make more common.");

		CCubesSettings.blockedWorlds = config.getStringList("BlockedWorlds", genCat, new String[0], "Worlds that Chance cubes shold not generate in");
		CCubesSettings.chestLoot = config.getBoolean("ChestLoot", genCat, true, "true if Chance Cubes should generate as chest loot in the world. false if they should not");
		CCubesSettings.craftingRecipie = config.getBoolean("CraftingRecipe", genCat, true, "true if Chance Cubes should have a crafting recipe. false if they should not");

		CCubesSettings.dropHeight = config.getInt("FallingBlockDropHeight", genCat, 20, 0, 256, "How many blocks above the Chance Cube that a block that will fall should be dropped from");

		CCubesSettings.userSpecificRewards = config.getBoolean("UserSpecificRewards", genCat, true, "true if Chance Cubes should load in user specific rewards (for a select few only)");
		CCubesSettings.disabledRewards = config.getBoolean("GloballyDisabledRewards", genCat, true, "true if Chance Cubes should check for globally disabled rewards (Rewards that are usually bugged or not working correctly). NOTE: The mod sends your Chance Cubes mod version to the web server to check for disabled rewards for your given version and the version number is subsequently logged. Feel free to make an inquiry if you wish to know more.");
		CCubesSettings.holidayRewards = config.getBoolean("HolidayRewards", genCat, true, "Set to false if you wish to disable the super special holiday rewards. Why would you want to do that?");
		CCubesSettings.holidayRewardTriggered = config.getBoolean("HolidayRewardTriggered", genCat, false, "Don't touch! Well I mean you can touch it, if you want. I can't stop you. I'm only text.");

		config.save();

		File customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards");
		customConfigFolder.mkdirs();

		new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards/Schematics").mkdirs();
		new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards/Sounds").mkdirs();

		new CustomRewardsLoader(customConfigFolder);

		customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/LuckyBlockRewards");
		customConfigFolder.mkdirs();
		new LuckyBlockRewardLoader(customConfigFolder);

		customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/Profiles");
		customConfigFolder.mkdirs();
		new CustomProfileLoader(customConfigFolder);
		ProfileManager.setupConfig(new Configuration(new File(folder + "/" + "Profiles.cfg")));

	}
}