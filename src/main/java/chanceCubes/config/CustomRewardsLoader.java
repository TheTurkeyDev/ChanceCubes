package chanceCubes.config;

import java.io.File;
import java.io.FileReader;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CustomRewardsLoader
{
	public static CustomRewardsLoader instance;
	private File folder;
	private static JsonParser json;

	public CustomRewardsLoader(File folder)
	{
		instance = this;
		this.folder = folder;
		json = new JsonParser();
	}

	public void loadCustomRewards()
	{
		for(File f: folder.listFiles())
		{
			if(f.getName().substring(f.getName().indexOf(".")).equalsIgnoreCase(".json"))
			{
				JsonElement  fileJson;
				try{
					CCubesCore.logger.log(Level.INFO,"Loading custom rewards file " + f.getName());
					fileJson = json.parse(new FileReader(f));
					CCubesCore.logger.log(Level.INFO,"Loaded custom rewards file " + f.getName());
				}catch(Exception e){
					CCubesCore.logger.log(Level.ERROR, "Unable to parse the file " + f.getName() + ". Skipping file loading.");
					continue;
				}
				
				for(Entry<String, JsonElement> reward :fileJson.getAsJsonObject().entrySet())
				{
					JsonObject rewardElements = reward.getValue().getAsJsonObject();
					for(Entry<String, JsonElement> rewardElement : rewardElements.entrySet())
					{
						JsonArray rewardTypes = rewardElement.getValue().getAsJsonArray();
						if(rewardElement.getKey().equalsIgnoreCase("Item"))
							this.loadItemReward(rewardTypes);
					}
				}
			}
		}
	}
	
	public void loadItemReward(JsonArray rawReward)
	{
		
	}
}
