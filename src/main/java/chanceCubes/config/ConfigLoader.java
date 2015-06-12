package chanceCubes.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigLoader 
{
	public static Configuration config;
	private static final String genCat = "General Settings";
	
	public static void loadConfigSettings(File file)
	{
		File fileFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes");
		fileFolder.mkdirs();
		config = new Configuration(new File(fileFolder + "/" + file.getName()));
		config.load();
		
		CCubesSettings.rangeMin = config.getInt("chanceRangeMin", genCat,  75, 0, 100, "The minimum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your range set to default 75. A chance cube with a chance value of 0 can get rewards of -75 to 75 in chance value.");
		CCubesSettings.rangeMax = config.getInt("chanceRangeMax", genCat,  75, 0, 100, "The maximum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your range set to default 75. A chance cube with a chance value of 0 can get rewards of -75 to 75 in chance value.");
		
		CCubesSettings.pendantUses = config.getInt("pendantUses", genCat, CCubesSettings.pendantUses, 0, 1000, "Number of uses for a pendant");
		CCubesSettings.oreGeneration = config.getBoolean("GenerateAsOre", genCat, true, "true if Chance Cubes should generate like ores with in the world. false if they should not");
		CCubesSettings.surfaceGeneration = config.getBoolean("GenerateOnSurface", genCat, true, "true if Chance Cubes should generate on the surface of the world. false if they should not");
		CCubesSettings.blockedWorlds = config.getStringList("BlockedWorlds", genCat, new String[0], "Worlds that Chance cubes shold not generate in");
		
		CCubesSettings.rangeMax = config.getInt("chanceRangeMax", genCat,  20, 0, 256, "How many blocks above the Chance Cube that a block that will fall should be droped from");
		
		config.save();
		
		File customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards");
		customConfigFolder.mkdirs();
		new CustomRewardsLoader(customConfigFolder);
	}
}