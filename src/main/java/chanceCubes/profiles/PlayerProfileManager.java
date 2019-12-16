package chanceCubes.profiles;

import chanceCubes.rewards.IChanceCubeReward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerProfileManager
{
	private String playerUUID;
	private List<IProfile> enabledProfiles = new ArrayList<>();
	private List<IProfile> disabledProfiles = new ArrayList<>();

	private JsonObject playerProfilejson = new JsonObject();

	public PlayerProfileManager(String playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public void enableProfile(IProfile profile)
	{
		if(!enabledProfiles.contains(profile))
		{
			disabledProfiles.remove(profile);

			enabledProfiles.add(profile);
			profile.onEnable(this, playerUUID);
		}

		playerProfilejson.addProperty(profile.getID(), true);
		GlobalProfileManager.updateProfileSaveFile(playerUUID, playerProfilejson);
	}

	public void disableProfile(IProfile profile)
	{
		if(!disabledProfiles.contains(profile))
		{
			enabledProfiles.remove(profile);
			disabledProfiles.add(profile);
			profile.onDisable(this, playerUUID);
		}

		playerProfilejson.addProperty(profile.getID(), false);
		GlobalProfileManager.updateProfileSaveFile(playerUUID, playerProfilejson);
	}

	public Map<String, Object> getRewardSpawnSettings(IChanceCubeReward reward)
	{
		Map<String, Object> settings = new HashMap<>();

		for(IProfile prof : enabledProfiles)
		{
			Map<String, Object> rewardSettings = prof.getRewardSettings().get(reward.getName());
			if(rewardSettings != null && !rewardSettings.isEmpty())
				settings.putAll(prof.getRewardSettings().get(reward.getName()));
		}

		return settings;
	}

	public List<IProfile> getAllProfiles()
	{
		List<IProfile> toReturn = new ArrayList<>();
		toReturn.addAll(enabledProfiles);
		toReturn.addAll(disabledProfiles);
		return toReturn;
	}

	public List<String> getEnabledProfileNames()
	{
		List<String> toReturn = new ArrayList<>();
		for(IProfile prof : enabledProfiles)
			toReturn.add(prof.getName());
		return toReturn;
	}

	public List<String> getDisabledProfileNames()
	{
		List<String> toReturn = new ArrayList<>();
		for(IProfile prof : disabledProfiles)
			toReturn.add(prof.getName());
		return toReturn;
	}

	public boolean isProfileEnabled(IProfile prof)
	{
		return enabledProfiles.contains(prof);
	}

	public boolean isRewardenabled(String reward)
	{
		List<IProfile> topLevelProfiles = new ArrayList<>();
		for(IProfile profile : enabledProfiles)
		{
			boolean isTopLevel = true;
			for(int i = topLevelProfiles.size() - 1; i >= 0; i--)
			{
				IProfile topProfile = topLevelProfiles.get(i);
				if(profile instanceof BasicProfile && topProfile instanceof BasicProfile)
				{
					if(((BasicProfile) topProfile).getSubProfiles().contains(profile))
						isTopLevel = false;
					else if(((BasicProfile) profile).getSubProfiles().contains(topProfile))
						topLevelProfiles.remove(i);
				}
			}

			if(isTopLevel)
				topLevelProfiles.add(profile);
		}

		for(IProfile profile : topLevelProfiles)
			if(!profile.isRewardEnabled(reward))
				return false;
		return true;
	}

	public void loadFromJson(JsonObject json)
	{
		this.playerProfilejson = json;
		for(Map.Entry<String, JsonElement> profileEntry : json.entrySet())
		{
			IProfile profile = GlobalProfileManager.getProfileFromID(profileEntry.getKey());
			if(profile == null || !profileEntry.getValue().isJsonPrimitive())
			{
				//TODO: Remove it?
				continue;
			}

			if(profileEntry.getValue().getAsBoolean())
				this.enableProfile(profile);
			else
				disabledProfiles.add(profile);
		}
	}

	public void loadFromDefaults(Map<IProfile, Boolean> profileDefaults)
	{
		for(Map.Entry<IProfile, Boolean> profileEntry : profileDefaults.entrySet())
		{
			if(profileEntry.getValue())
				this.enableProfile(profileEntry.getKey());
			else
				this.disabledProfiles.add(profileEntry.getKey());
		}
	}
}
