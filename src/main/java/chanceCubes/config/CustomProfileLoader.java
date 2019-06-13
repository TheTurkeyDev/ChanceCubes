package chanceCubes.config;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chanceCubes.CCubesCore;
import chanceCubes.profiles.BasicProfile;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.profiles.triggers.DifficultyTrigger;
import chanceCubes.profiles.triggers.DimensionChangeTrigger;
import chanceCubes.profiles.triggers.GameStageTrigger;
import net.minecraft.world.EnumDifficulty;

public class CustomProfileLoader
{
	public static CustomProfileLoader instance;
	private static JsonParser json;
	private File folder;

	public CustomProfileLoader(File folder)
	{
		instance = this;
		this.folder = folder;
		json = new JsonParser();
	}

	public void loadProfiles()
	{
		Map<BasicProfile, List<String>> unfoundSubProfiles = new HashMap<>();

		for(File f : folder.listFiles())
		{
			if(!f.isFile() || !f.getName().contains(".json"))
				continue;
			if(f.getName().substring(f.getName().indexOf(".")).equalsIgnoreCase(".json"))
			{
				JsonElement fileJson;
				try
				{
					CCubesCore.logger.log(Level.INFO, "Loading profile file " + f.getName());
					fileJson = json.parse(new FileReader(f));
				} catch(Exception e)
				{
					CCubesCore.logger.log(Level.ERROR, "Unable to parse the profile file " + f.getName() + ". Skipping file loading.");
					CCubesCore.logger.log(Level.ERROR, "Parse Error: " + e.getMessage());
					continue;
				}

				for(Entry<String, JsonElement> profileEntry : fileJson.getAsJsonObject().entrySet())
				{
					String profileID = profileEntry.getKey();
					JsonObject profileJson = profileEntry.getValue().getAsJsonObject();

					if(!profileJson.has("name") || !profileJson.has("description"))
					{
						CCubesCore.logger.log(Level.ERROR, "Unable to parse profile \"" + profileID + "\". Missing \"name\" and/or \"description\" json entries.");
						continue;
					}

					String profileName = profileJson.get("name").getAsString();
					String profileDesc = profileJson.get("description").getAsString();

					BasicProfile profile = new BasicProfile(profileID, profileName, profileDesc);
					ProfileManager.registerProfile(profile);

					if(profileJson.has("rewards_to_enable"))
					{
						JsonArray rewardsToEnable = profileJson.getAsJsonArray("rewards_to_enable");
						rewardsToEnable.forEach(element -> {
							profile.addEnabledRewards(element.getAsString());
						});
					}

					if(profileJson.has("rewards_to_disable"))
					{
						JsonArray rewardsToDisable = profileJson.getAsJsonArray("rewards_to_disable");
						rewardsToDisable.forEach(element -> {
							profile.addDisabledRewards(element.getAsString());
						});
					}

					if(profileJson.has("sub_profiles"))
					{
						JsonArray subProfiles = profileJson.getAsJsonArray("sub_profiles");
						subProfiles.forEach(element -> {
							IProfile subProf = ProfileManager.getProfileFromID(element.getAsString());
							if(subProf != null)
							{
								profile.addSubProfile(subProf);
							}
							else
							{
								List<String> subProfs = unfoundSubProfiles.get(profile);
								if(subProfs == null)
								{
									subProfs = new ArrayList<String>();
									unfoundSubProfiles.put(profile, subProfs);
								}

								subProfs.add(element.getAsString());
							}
						});
					}

					if(profileJson.has("triggers"))
					{
						JsonArray triggers = profileJson.getAsJsonArray("triggers");
						triggers.forEach(element -> {
							JsonObject triggerJson = element.getAsJsonObject();
							if(!triggerJson.has("type"))
							{
								CCubesCore.logger.log(Level.ERROR, "Unable to parse profile \"" + profileID + "\" Triggers. Missing \"type\" json entry.");
								return;
							}
							String type = triggerJson.get("type").getAsString().toLowerCase();

							switch(type)
							{
								case "difficulty":
									if(!triggerJson.has("type"))
									{
										CCubesCore.logger.log(Level.ERROR, "Unable to parse profile \"" + profileID + "\" Triggers. Missing \"difficulty\" json entry for the diffuculty trigger type.");
										return;
									}
									profile.addTriggers(new DifficultyTrigger(profile, EnumDifficulty.valueOf(triggerJson.get("difficulty").getAsString().toUpperCase())));
									break;
								case "gamestage":
									if(!triggerJson.has("stage"))
									{
										CCubesCore.logger.log(Level.ERROR, "Unable to parse profile \"" + profileID + "\" Triggers. Missing \"stage\" json entry for the gamestage trigger type.");
										return;
									}
									profile.addTriggers(new GameStageTrigger(profile, triggerJson.get("stage").getAsString()));
									break;
								case "dimension":
									if(!triggerJson.has("dim_id"))
									{
										CCubesCore.logger.log(Level.ERROR, "Unable to parse profile \"" + profileID + "\" Triggers. Missing \"dim_id\" json entry for the dimension trigger type.");
										return;
									}
									profile.addTriggers(new DimensionChangeTrigger(profile, triggerJson.get("dim_id").getAsInt()));
									break;
								default:
									CCubesCore.logger.log(Level.ERROR, "Profile trigger type \"" + type + "\" is not currently supported.");
									break;
							}
						});
					}
					
					if(profileJson.has("reward_properties"))
					{
						JsonArray properties = profileJson.getAsJsonArray("reward_properties");
						properties.forEach(element -> {
							JsonObject rewardPropJson = element.getAsJsonObject();
							if(!rewardPropJson.has("reward_name"))
							{
								CCubesCore.logger.log(Level.ERROR, "Unable to parse reward property json \"" + element.toString() + "\". Missing \"reward_name\" entry.");
								return;
							}
							String rewardName = rewardPropJson.get("reward_name").getAsString();

							if(rewardPropJson.has("chance_value"))
								profile.addRewardChanceChange(rewardName, rewardPropJson.get("chance_value").getAsInt());
						});
					}
				}

				CCubesCore.logger.log(Level.INFO, "Loaded profile file " + f.getName());
			}
		}

		for(BasicProfile prof : unfoundSubProfiles.keySet())
		{
			for(String subProfID : unfoundSubProfiles.get(prof))
			{
				IProfile subProf = ProfileManager.getProfileFromID(subProfID);
				if(subProf != null)
					prof.addSubProfile(subProf);
				else
					CCubesCore.logger.log(Level.ERROR, "Unable to find subprofile \"" + subProfID + "\" for the profile \"" + prof.getID() + "\".");
			}
		}
	}

}
