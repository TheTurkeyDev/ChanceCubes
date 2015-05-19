package chanceCubes.config;

import java.io.File;

public class CustomRewardsLoader
{
	public static CustomRewardsLoader instance;
	private File folder;
	
	public CustomRewardsLoader(File folder)
	{
		instance = this;
		this.folder = folder;
	}
	
	public void loadCustomRewards()
	{
		for(File f: folder.listFiles())
		{
			if(f.getName().substring(f.getName().indexOf(".")).equalsIgnoreCase(".json"))
			{
				
			}
		}
	}
}
