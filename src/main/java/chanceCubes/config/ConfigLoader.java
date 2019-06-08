package chanceCubes.config;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * Handles Configuration file management
 */

public class ConfigLoader
{
	public static final ForgeConfigSpec configSpec;
	public static final ConfigLoader CONFIG;
	static
	{
		final Pair<ConfigLoader, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigLoader::new);
		configSpec = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	public static final String genCat = "General Settings";
	public static final String rewardCat = "Rewards";
	public static final String giantRewardCat = "Giant Chance Cube Rewards";

	public static File folder;
	
	public static void initParentFolder() {
		(new File(FMLPaths.CONFIGDIR.get().toString(), "chancecubes")).mkdir();
	}

	/**
	 * Initializes and loads ChanceCubes settings from the config file. <br>
	 * <br>
	 * <b>Do not use outside of postInit eventhandler</b>, use {@link #reloadConfigSettings()}
	 * instead.
	 * 
	 * @param file
	 *            The default configuration file suggested by forge.
	 */
	public ConfigLoader(ForgeConfigSpec.Builder builder)
	{
		builder.push(rewardCat).comment("Set to false to disable a specific reward");

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
                .defineList("BlockedWorlds", new ArrayList<String>(), input -> (input != null && !input.equals("")));
		CCubesSettings.chestLoot = builder
                .comment("True if Chance Cubes should generate as chest loot in the world. false if they should not")
                .define("ChestLoot", true);
		CCubesSettings.craftingRecipie = builder
                .comment("True if Chance Cubes should have a crafting recipe. false if they should not")
                .define("CraftingRecipe", true);

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
	public void onConfigLoad(ModConfigEvent event)
	{
		File folder = new File(event.getConfig().getFullPath().toUri());

		File customConfigFolder = new File(folder.getAbsolutePath() + "/CustomRewards");
		customConfigFolder.mkdirs();
		new CustomRewardsLoader(customConfigFolder);

		new File(folder.getAbsolutePath() + "/CustomRewards/Schematics").mkdirs();
		new File(folder.getAbsolutePath() + "/CustomRewards/Sounds").mkdirs();

		File customProfileFolder = new File(folder.getAbsolutePath() + "/Profiles");
		customProfileFolder.mkdirs();
		new CustomProfileLoader(customConfigFolder);

		//ProfileManager.setupConfig(new Configuration(new File(folder + "/" + "Profiles.cfg")));
	}
}