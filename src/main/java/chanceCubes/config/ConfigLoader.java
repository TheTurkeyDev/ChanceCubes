
package chanceCubes.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.tuple.Pair;

import chanceCubes.CCubesCore;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigLoader
{
	public static final ForgeConfigSpec configSpec;
	public static final ConfigLoader CONFIG;

	public static File globalDisableConfig;
	private static JsonObject globalDisableConfigJson;
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	static
	{
		final Pair<ConfigLoader, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigLoader::new);
		configSpec = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	public static final String genCat = "General Settings";

	public static File schematicsFolder;

	public static void initParentFolder()
	{
		(new File(FMLPaths.CONFIGDIR.get().toString(), "chancecubes")).mkdir();
	}

	/**
	 * Initializes and loads ChanceCubes settings from the config file. <br>
	 * <br>
	 * <b>Do not use outside of postInit eventhandler</b>
	 * instead.
	 */
	public ConfigLoader(ForgeConfigSpec.Builder builder)
	{
		builder.push(genCat).comment("Set to false to disable a specific reward");

		//CCubesSettings.nonReplaceableBlocksOverrides = NonreplaceableBlockOverride.parseStrings(config.getStringList("nonreplaceableBlockOverrides", genCat, new String[] { "minecraft:bedrock" }, "Blocks that ChanceCube rewards will be unable to replace or remove, can override IMC-added blocks by prefacing the block ID with \'-\'."));

		// @formatter:off
		CCubesSettings.rangeMin = builder
				.comment("The minimum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your rangemin set to 10 and range max set to 15. A chance cube with a chance value of 0 can get rewards of -10 to 15 in chance value.")
				.defineInRange("chanceRangeMin", 10, 0, 100);
		CCubesSettings.rangeMax = builder
				.comment("The maximum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your rangemin set to 10 and range max set to 15. A chance cube with a chance value of 0 can get rewards of -10 to 15 in chance value.")
				.defineInRange("chanceRangeMax", 10, 0, 100);

		CCubesSettings.d20UseNormalChances = builder
				.comment("Set to true if the D20's should have any chance value from -100 to 100. Set to false to have the D20's only have a chance value of either -100 or 100")
				.define("D20UseNormalChanceValues", false);

		CCubesSettings.enableHardCodedRewards = builder
				.comment("Set to true if the default rewards should be loaded, false if they shouldn't")
				.define("EnableDefaultRewards", true);

		CCubesSettings.pendantUses = builder
				.comment("Number of uses for a pendant")
				.defineInRange("pendantUses", 32, 0, 1000);

		CCubesSettings.oreGeneration = builder
				.comment("True if Chance Cubes should generate like ores with in the world. false if they should not")
				.define("GenerateAsOre", true);
		CCubesSettings.oreGenAmount = builder
				.comment("Amount of chance cubes to try and spawn, per chunk, as an ore")
				.defineInRange("oreGenAmount", 4, 1, 100);
		CCubesSettings.surfaceGeneration = builder
				.comment("true if Chance Cubes should generate on the surface of the world. false if they should not")
				.define("GenerateOnSurface", true);
		CCubesSettings.surfaceGenAmount = builder
				.comment("Chance of a chunk to have a chance cube spawned on the surface. The math is 1/(surfaceGenerationAmount), so increase to make more rare, and decrese to make more common.")
				.defineInRange("surfaceGenerationAmount", 100, 0, Integer.MAX_VALUE);


		CCubesSettings.blockedWorlds = builder
				.comment("Worlds that Chance cubes shold not generate in")
				.defineList("BlockedWorlds", new ArrayList<>(), input -> (input != null && !input.equals("")));
		CCubesSettings.chestLoot = builder
				.comment("True if Chance Cubes should generate as chest loot in the world. false if they should not")
				.define("ChestLoot", true);

		CCubesSettings.dropHeight = builder
				.comment("How many blocks above the Chance Cube that a block that will fall should be dropped from")
				.defineInRange("FallingBlockDropHeight", 20, 0, 256);

		CCubesSettings.userSpecificRewards = builder
				.comment("True if Chance Cubes should load in user specific rewards (for a select few only)")
				.define("UserSpecificRewards", true);
		CCubesSettings.disabledRewards = builder
				.comment( "True if Chance Cubes should check for globally disabled rewards (Rewards that are usually bugged or not working correctly). NOTE: The mod sends your Chance Cubes mod version to the web server to check for disabled rewards for your given version and the version number is subsequently logged. Feel free to make an inquiry if you wish to know more.")
				.define("GloballyDisabledRewards", true);
		CCubesSettings.holidayRewards = builder
				.comment("Set to false if you wish to disable the super special holiday rewards. Why would you want to do that?")
				.define("HolidayRewards", true);
		CCubesSettings.holidayRewardTriggered = builder
				.comment("Don't touch! Well I mean you can touch it, if you want. I can't stop you. I'm only text.")
				.define("HolidayRewardTriggered", false);
		builder.pop();
		// @formatter:on
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent event)
	{
		File folder = (new File(event.getConfig().getFullPath().toUri())).getParentFile();

		File customConfigFolder = new File(folder.getAbsolutePath(), "custom_rewards");
		customConfigFolder.mkdirs();

		new File(customConfigFolder, "sounds").mkdirs();

		schematicsFolder = new File(customConfigFolder, "schematics");
		schematicsFolder.mkdirs();

		new CustomRewardsLoader(customConfigFolder);

		File customProfileFolder = new File(folder.getAbsolutePath(), "profiles");
		customProfileFolder.mkdirs();
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