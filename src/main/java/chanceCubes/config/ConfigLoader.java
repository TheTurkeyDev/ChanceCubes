package chanceCubes.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import chanceCubes.blocks.BlockChanceCube;

import com.enderio.core.common.util.Bound;

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
		
		int rangeMin = config.getInt("chanceRangeMin", genCat, -75, -100, 0, "The minimum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your range set to default 75. A chance cube with a chance value of 0 can get rewards of -75 to 75 in chance value.");
        int rangeMax = config.getInt("chanceRangeMax", genCat, 75, 0, 100, "The maximum chance range value. Changes the range of chance that the chance block can pick from. i.e. If you have your range set to default 75. A chance cube with a chance value of 0 can get rewards of -75 to 75 in chance value.");
		BlockChanceCube.luckBound = Bound.of(rangeMin, rangeMax);
		
		CCubesSettings.pendantUses = config.get(genCat, "pendantUses", CCubesSettings.pendantUses, "Number of uses for a pendant").getInt();
		CCubesSettings.oreGeneration = config.getBoolean(genCat, "BlockedWorlds", true, "true if Chance Cubes should generate like ores with in the world. false if they should not");
		CCubesSettings.surfaceGeneration = config.getBoolean(genCat, "BlockedWorlds", true, "true if Chance Cubes should generate on the surface of the world. false if they should not");
		CCubesSettings.blockedWorlds = config.getStringList(genCat, "BlockedWorlds", new String[0], "Worlds that Chance cubes shold not generate in");
		
		config.save();
		
		File customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards");
		customConfigFolder.mkdirs();
		new CustomRewardsLoader(customConfigFolder);
	}
}