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
		config = new Configuration(file);
		config.load();
		
		CCubesCore.cCubeRegistry.setRange(config.getInt(genCat, "Chance Range", 75, 0, 100, "changes the range of chance that the chance block can pick from. ie. if you have your range set to default 75. A chance cube with a chance value of 0 can get rewards of -75 to 75 in chance value."));
		
		config.save();
	}
}