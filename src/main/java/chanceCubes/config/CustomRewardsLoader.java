package chanceCubes.config;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.BlockChanceCube;
import chanceCubes.blocks.BlockChanceCube.EnumTexture;
import chanceCubes.parsers.RewardParser;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.sounds.CustomSoundsLoader;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.HTTPUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

public class CustomRewardsLoader
{
	public static CustomRewardsLoader instance;

	private static JsonParser json;

	private File folder;

	public CustomRewardsLoader(File folder)
	{
		instance = this;
		this.folder = folder;
		json = new JsonParser();

		CustomSoundsLoader customSounds = new CustomSoundsLoader(folder, new File(folder.getAbsolutePath() + "/CustomSounds-Resourcepack"), "Chance Cubes Resource Pack");
		customSounds.addCustomSounds();
		try
		{
			customSounds.assemble();
		} catch(Exception e)
		{
			e.printStackTrace();
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to create a file or folder necessary to have custom sound rewards. No custom sounds will be added.");
		}
	}

	public void loadCustomRewards()
	{
		for(File f : folder.listFiles())
		{
			if(!f.isFile() || !f.getName().contains(".json"))
				continue;
			if(f.getName().substring(f.getName().indexOf(".")).equalsIgnoreCase(".json"))
			{
				JsonElement fileJson;
				try
				{
					CCubesCore.logger.log(Level.INFO, "Loading custom rewards file " + f.getName());
					fileJson = json.parse(new FileReader(f));
				} catch(Exception e)
				{
					CCubesCore.logger.log(Level.ERROR, "Unable to parse the file " + f.getName() + ". Skipping file loading.");
					CCubesCore.logger.log(Level.ERROR, "Parse Error: " + e.getMessage());
					continue;
				}

				int addedRewards = 0;
				for(Entry<String, JsonElement> reward : fileJson.getAsJsonObject().entrySet())
				{
					CustomEntry<BasicReward, Boolean> parsedReward = RewardParser.parseReward(reward);
					BasicReward basicReward = parsedReward.getKey();
					if(basicReward == null)
					{
						CCubesCore.logger.log(Level.ERROR, "Seems your reward is setup incorrectly, or is disabled for this version of minecraft with a depedency, and Chance Cubes was not able to parse the reward " + reward.getKey() + " for the file " + f.getName());
						continue;
					}

					if(parsedReward.getValue())
						GlobalCCRewardRegistry.GIANT.registerReward(basicReward);
					else
						GlobalCCRewardRegistry.DEFAULT.registerReward(basicReward);

					addedRewards++;
				}

				CCubesCore.logger.log(Level.INFO, "Loaded custom rewards file " + f.getName() + "! Added " + addedRewards + " rewards");
			}
		}

		try
		{
			String today = new SimpleDateFormat("MM/dd").format(new Date());
			JsonObject json = HTTPUtil.getWebFile("POST", "https://api.theprogrammingturkey.com/chance_cubes/ChanceCubesAPI.php", new CustomEntry<>("version", CCubesCore.VERSION), new CustomEntry<>("date", today)).getAsJsonObject();
			this.loadDisabledRewards(json.get("Disabled Rewards").getAsJsonArray());
			this.loadHolidayRewards(json.get("Holiday Rewards"));
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Failed to fetch remote information for the mod!");
			e.printStackTrace();
		}
	}

	private void loadHolidayRewards(JsonElement json)
	{
		if(!CCubesSettings.holidayRewards)
			return;

		JsonObject holidays = json.getAsJsonObject();
		if(holidays.has("Texture") && !(holidays.get("Texture") instanceof JsonNull))
		{
			CCubesSettings.hasHolidayTexture = true;
			CCubesSettings.holidayTextureName = holidays.get("Texture").getAsString();
		}
		else
		{
			CCubesSettings.hasHolidayTexture = false;
			CCubesSettings.holidayTextureName = "default";
		}

		for(EnumTexture t : EnumTexture.values())
			if(t.getName().equalsIgnoreCase(CCubesSettings.holidayTextureName))
				BlockChanceCube.textureToSet = t;

		if(!CCubesSettings.holidayRewardTriggered)
		{
			if(holidays.has("Holiday") && !(holidays.get("Holiday") instanceof JsonNull) && holidays.has("Reward") && !(holidays.get("Reward") instanceof JsonNull))
			{
				String holidayName = holidays.get("Holiday").getAsString();
				BasicReward basicReward = RewardParser.parseReward(new CustomEntry<>(holidayName, holidays.get("Reward"))).getKey();
				if(basicReward != null)
				{
					CCubesSettings.doesHolidayRewardTrigger = true;
					CCubesSettings.holidayReward = basicReward;
					CCubesCore.logger.log(Level.ERROR, "Custom holiday reward \"" + holidayName + "\" loaded!");
				}
				else
				{
					CCubesCore.logger.log(Level.ERROR, "Failed to load the Custom holiday reward \"" + holidayName + "\"!");
				}
			}
		}

	}

	private void loadDisabledRewards(JsonArray disabledRewards)
	{
		if(CCubesSettings.disabledRewards)
		{
			for(JsonElement reward : disabledRewards)
			{
				boolean removed = GlobalCCRewardRegistry.DEFAULT.disableReward(reward.getAsString());
				if(!removed)
					removed = GlobalCCRewardRegistry.GIANT.disableReward(reward.getAsString());

				if(removed)
					CCubesCore.logger.log(Level.WARN, "The reward " + reward.getAsString() + " has been disabled by the mod author due to a bug or some other reason.");
			}
		}
	}
}