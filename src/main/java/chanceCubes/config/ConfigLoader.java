
package chanceCubes.config;

import chanceCubes.CCubesCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = CCubesCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigLoader
{
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static final ForgeConfigSpec configSpec;
	public static final ConfigLoader CONFIG;

	public static File globalDisableConfig;
	private static JsonObject globalDisableConfigJson;
	public static File rewardSettingsConfig;
	private static JsonObject rewardSettingsJson;

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
				.defineInRange("ChanceRangeMin", 10, 0, 100);
		CCubesSettings.rangeMax = builder
				.comment("The maximum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your rangemin set to 10 and range max set to 15. A chance cube with a chance value of 0 can get rewards of -10 to 15 in chance value.")
				.defineInRange("ChanceRangeMax", 10, 0, 100);

		CCubesSettings.d20UseNormalChances = builder
				.comment("Set to true if the D20's should have any chance value from -100 to 100. Set to false to have the D20's only have a chance value of either -100 or 100")
				.define("D20UseNormalChanceValues", false);

		CCubesSettings.rewardsEqualChance = builder
				.comment("Set to true if the mod should ignore chance values and give each reward and equal chance to be picked")
				.define("RewardsEqualChance", false);

		CCubesSettings.disableGiantCC = builder
				.comment("Set to true Giant Chance Cubes should be disabled")
				.define("DisableGiantCC", false);

		CCubesSettings.enableHardCodedRewards = builder
				.comment("Set to true if the default rewards should be loaded, false if they shouldn't")
				.define("EnableDefaultRewards", true);

		CCubesSettings.pendantUses = builder
				.comment("Number of uses for a pendant")
				.defineInRange("PendantUses", 32, 0, 1000);

		CCubesSettings.oreGeneration = builder
				.comment("True if Chance Cubes should generate like ores with in the world. false if they should not")
				.define("GenerateAsOre", true);
		CCubesSettings.surfaceGeneration = builder
				.comment("true if Chance Cubes should generate on the surface of the world. false if they should not")
				.define("GenerateOnSurface", true);
		CCubesSettings.surfaceGenAmount = builder
				.comment("Chance of a chunk to have a chance cube spawned on the surface. The math is 1/(surfaceGenerationAmount), so increase to make more rare, and decrease to make more common.")
				.defineInRange("SurfaceGenerationAmount", 100, 0, Integer.MAX_VALUE);


		CCubesSettings.blockedWorlds = builder
				.comment("Worlds that Chance cubes shold not generate in")
				.defineList("BlockedWorlds", new ArrayList<>(), input -> (input != null && !input.equals("")));
		CCubesSettings.chestLoot = builder
				.comment("True if Chance Cubes should generate as chest loot in the world. false if they should not")
				.define("ChestLoot", true);

		CCubesSettings.blockRestoreBlacklist = builder
				.comment("Blocks that should not be replaced when rewards are \"restored\" after a reward is over, i.e don't remove graves when the boss dome get's cleared")
				.defineList("BlockRestoreBlacklist", new ArrayList<>(), input -> (input != null && !input.equals("")));

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

		globalDisableConfig = new File(folder, "global_rewards.json");
		try
		{
			if(globalDisableConfig.createNewFile())
			{
				globalDisableConfigJson = new JsonObject();
				globalDisableConfigJson.addProperty("Comment", "This file is for enabling and disabling rewards for all users. You can use /chancecubes enableReward and /chancecubes disableReward to enable/ disable in game, though they will not change the value in this config.");
				globalDisableConfigJson.add("rewards", new JsonObject());
				try(Writer writer = new FileWriter(globalDisableConfig))
				{
					gson.toJson(globalDisableConfigJson, writer);
				}
			}
		} catch(IOException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes was unable to create the global rewards config file!");
		}

		rewardSettingsConfig = new File(folder, "reward_settings.json");
		try
		{
			if(rewardSettingsConfig.createNewFile())
			{
				rewardSettingsJson = new JsonObject();
				try(Writer writer = new FileWriter(rewardSettingsConfig))
				{
					gson.toJson(rewardSettingsJson, writer);
				}
			}
		} catch(IOException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes was unable to create the reward settings config file!");
		}

		reload();
	}

	public static void reload()
	{
		try
		{
			globalDisableConfigJson = JsonParser.parseReader(new FileReader(globalDisableConfig)).getAsJsonObject();
			rewardSettingsJson = JsonParser.parseReader(new FileReader(rewardSettingsConfig)).getAsJsonObject();
		} catch(FileNotFoundException e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes could not load the global rewards and/or the reward settings config file!");
			e.printStackTrace();
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

	public static JsonObject getRewardSettings(String reward)
	{
		return rewardSettingsJson.has(reward) ? rewardSettingsJson.getAsJsonObject(reward) : new JsonObject();
	}
}