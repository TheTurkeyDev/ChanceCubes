package chanceCubes.config;

import java.io.File;

import chanceCubes.CCubesCore;
import net.minecraftforge.common.config.Configuration;

public class ConfigLoader 
{
	private static Configuration config;
	private static final String genCat = "General Settings";
	
	public static void loadConfigSettings(File file)
	{
		File fileFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes");
		fileFolder.mkdirs();
		config = new Configuration(new File(fileFolder + "/" + file.getName()));
		config.load();
		
		CCubesCore.cCubeRegistry.setRange(config.getInt(genCat, "Chance Range", 75, 0, 100, "changes the range of chance that the chance block can pick from. ie. if you have your range set to default 75. A chance cube with a chance value of 0 can get rewards of -75 to 75 in chance value."));
		
		CCubesSettings.pendantUses = config.get(genCat, "pendantUses", CCubesSettings.pendantUses, "Number of uses for a pendant").getInt();
		
		config.save();
		
		File customConfigFolder = new File(file.getParentFile().getAbsolutePath() + "/ChanceCubes/CustomRewards");
		customConfigFolder.mkdirs();
		new CustomRewardsLoader(customConfigFolder);
	}
}