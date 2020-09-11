package chanceCubes.config;

import chanceCubes.CCubesCore;
import chanceCubes.util.NonreplaceableBlockOverride;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Handles Configuration file management
 */
public class ConfigLoader
{
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static File globalDisableConfig;
	private static JsonObject globalDisableConfigJson;

	public static Configuration config;
	public static final String genCat = "General Settings";

	public static File folder;
	public static File forgeSuggestedCfgFile;

	/**
	 * Initializes and loads ChanceCubes settings from the config file. <br>
	 *
	 * @param file The default configuration file suggested by forge.
	 */
	public static void loadConfigSettings(File file)
	{
		forgeSuggestedCfgFile = file;
		folder = new File(forgeSuggestedCfgFile.getParentFile().getAbsolutePath() + "/ChanceCubes");
		folder.mkdirs();
		config = new Configuration(new File(folder + "/" + forgeSuggestedCfgFile.getName()));
		config.load();

		CCubesSettings.nonReplaceableBlocksOverrides = NonreplaceableBlockOverride.parseStrings(config.getStringList("nonreplaceableBlockOverrides", genCat, new String[]{"minecraft:bedrock"}, "Blocks that ChanceCube rewards will be unable to replace or remove, can override IMC-added blocks by prefacing the block ID with '-'."));
		CCubesSettings.rangeMin = config.getInt("chanceRangeMin", genCat, 10, 0, 100, "The minimum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your rangemin set to 10 and range max set to 15. A chance cube with a chance value of 0 can get rewards of -10 to 15 in chance value.");
		CCubesSettings.rangeMax = config.getInt("chanceRangeMax", genCat, 10, 0, 100, "The maximum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your rangemin set to 10 and range max set to 15. A chance cube with a chance value of 0 can get rewards of -10 to 15 in chance value.");
		CCubesSettings.d20UseNormalChances = config.getBoolean("D20UseNormalChanceValues", genCat, false, "Set to true if the D20's should have any chance value from -100 to 100. Set to false to have the D20's only have a chance value of either -100 or 100");

		CCubesSettings.enableHardCodedRewards = config.getBoolean("EnableDefaultRewards", genCat, true, "Set to true if the default rewards should be loaded, false if they shouldn't");

		CCubesSettings.pendantUses = config.getInt("pendantUses", genCat, CCubesSettings.pendantUses, 0, 1000, "Number of uses for a pendant");
		CCubesSettings.oreGeneration = config.getBoolean("GenerateAsOre", genCat, true, "true if Chance Cubes should generate like ores with in the world. false if they should not");
		CCubesSettings.oreGenAmount = config.getInt("oreGenAmount", genCat, 4, 1, 100, "Amount of chance cubes to try and spawn, per chunk, as an ore");
		CCubesSettings.surfaceGeneration = config.getBoolean("GenerateOnSurface", genCat, true, "true if Chance Cubes should generate on the surface of the world. false if they should not");

		int defaultGen = 100;
		if(config.hasKey(genCat, "surfaceGenAmount"))
		{
			int oldGen = config.getInt("surfaceGenAmount", genCat, 1, 0, 100, "Percentage chance of a chunk to have a chance cube spawned on the surface. (OLD! REMOVE THIS CONFIG OPTION!)");
			if(oldGen == 0)
				defaultGen = 1;
			else
				defaultGen = 100 / oldGen;
		}

		CCubesSettings.surfaceGenAmount = config.getInt("surfaceGenerationAmount", genCat, defaultGen, 1, Integer.MAX_VALUE, "Chance of a chunk to have a chance cube spawned on the surface. The math is 1/(surfaceGenerationAmount), so increase to make more rare, and decrese to make more common.");

		CCubesSettings.blockedWorlds = config.getStringList("BlockedWorlds", genCat, new String[0], "Worlds that Chance cubes should not generate in");
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

		//		customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/LuckyBlockRewards");
		//		customConfigFolder.mkdirs();
		//		new LuckyBlockRewardLoader(customConfigFolder);

		customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/Profiles");
		customConfigFolder.mkdirs();
		new CustomProfileLoader(customConfigFolder);

		globalDisableConfig = new File(folder, "global_rewards.json");
		try
		{
			if(globalDisableConfig.createNewFile())
			{
				globalDisableConfigJson = new JsonObject();
				globalDisableConfigJson.addProperty("Comment", "This file is for enabling and disabling rewards globally for all users! This cannot be reversed without a mod reload! Reward Profiles are the suggested method over this. This is basically a do all be all override.");
				globalDisableConfigJson.addProperty("Comment2", "Note: Even if a reward is marked true here, does not mean its fully enabled. Rewards like clear inventory are disabled via default profiles!");
				globalDisableConfigJson.add("rewards", new JsonObject());
				try(Writer writer = new FileWriter(globalDisableConfig))
				{
					gson.toJson(globalDisableConfigJson, writer);
				}
			}
			else
			{
				reload();
			}
		} catch(IOException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes was unable to create the global rewards Config file!");
		}
	}

	public static void reload()
	{
		try
		{
			globalDisableConfigJson = new JsonParser().parse(new FileReader(globalDisableConfig)).getAsJsonObject();
		} catch(FileNotFoundException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes could not load the global rewards Config file!");
		}
	}

	public static boolean getRewardConfigStatus(String reward, boolean defaultVal)
	{
		System.out.println(reward);
		JsonObject rewardsJson = globalDisableConfigJson.getAsJsonObject("rewards");
		if(rewardsJson.has(reward))
			return rewardsJson.get(reward).getAsBoolean();
		rewardsJson.addProperty(reward, defaultVal);
		try(Writer writer = new FileWriter(globalDisableConfig))
		{
			gson.toJson(globalDisableConfigJson, writer);
		} catch(IOException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to save to the global rewards Config file!");
		}
		return defaultVal;
	}
}