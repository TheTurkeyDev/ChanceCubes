package chanceCubes.profiles;

import chanceCubes.CCubesCore;
import chanceCubes.profiles.triggers.ITrigger;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BasicProfile implements IProfile
{
	private boolean enabled;
	private String id;
	private String name;
	private String desc;
	private boolean anyTrigger = true;
	private StringBuilder descFull = new StringBuilder();
	private Map<ITrigger<?>, Boolean> triggers = new HashMap<>();
	private List<String> rewardsToEnable = new ArrayList<>();
	private List<String> rewardsToDisable = new ArrayList<>();
	private List<IProfile> subProfiles = new ArrayList<>();
	private Map<String, Integer> chanceChanges = new HashMap<>();
	private Map<String, Map<String, Object>> rewardSettings = new HashMap<>();

	public BasicProfile(String id, String name, String desc)
	{
		this.id = id;
		this.name = name;
		this.desc = desc;
	}

	public void setAnyTrigger(boolean anyTrigger)
	{
		this.anyTrigger = anyTrigger;
	}

	public BasicProfile addEnabledRewards(String... rewards)
	{
		this.rewardsToEnable.addAll(Arrays.asList(rewards));
		return this;
	}

	public BasicProfile addDisabledRewards(String... rewards)
	{
		this.rewardsToDisable.addAll(Arrays.asList(rewards));
		return this;
	}

	public BasicProfile addTriggers(ITrigger<?>... triggers)
	{
		for(ITrigger<?> trigger : triggers)
			this.triggers.put(trigger, false);
		return this;
	}

	public BasicProfile addSubProfile(IProfile... profiles)
	{
		this.subProfiles.addAll(Arrays.asList(profiles));
		return this;
	}

	public BasicProfile addRewardChanceChange(String rewardName, int newChance)
	{
		this.chanceChanges.put(rewardName, newChance);
		return this;
	}

	public BasicProfile addSettingsToReward(String reward, String key, Object value)
	{
		Map<String, Object> settings = rewardSettings.computeIfAbsent(reward, k -> new HashMap<>());
		settings.put(key, value);
		return this;
	}

	@Override
	public void onEnable()
	{
		enabled = true;
		for(IProfile prof : this.subProfiles)
			ProfileManager.enableProfile(prof);
		for(String s : this.rewardsToDisable)
			if(!ChanceCubeRegistry.INSTANCE.disableReward(s))
				if(!GiantCubeRegistry.INSTANCE.disableReward(s))
					CCubesCore.logger.log(Level.ERROR, name + " failed to disable reward " + s);
		for(String s : this.rewardsToEnable)
			if(!ChanceCubeRegistry.INSTANCE.enableReward(s))
				if(!GiantCubeRegistry.INSTANCE.enableReward(s))
					CCubesCore.logger.log(Level.ERROR, name + " failed to enable reward " + s);
		for(Entry<String, Integer> rewardInfo : this.chanceChanges.entrySet())
			ProfileManager.setRewardChanceValue(rewardInfo.getKey(), rewardInfo.getValue());
	}

	@Override
	public void onDisable()
	{
		enabled = false;
		for(IProfile prof : this.subProfiles)
			ProfileManager.disableProfile(prof);
		for(String s : this.rewardsToDisable)
			if(!ChanceCubeRegistry.INSTANCE.enableReward(s))
				if(!GiantCubeRegistry.INSTANCE.enableReward(s))
					CCubesCore.logger.log(Level.ERROR, name + " failed to enable reward " + s);
		for(String s : this.rewardsToEnable)
			if(!ChanceCubeRegistry.INSTANCE.disableReward(s))
				if(!GiantCubeRegistry.INSTANCE.disableReward(s))
					CCubesCore.logger.log(Level.ERROR, name + " failed to disable reward " + s);
		for(Entry<String, Integer> rewardInfo : this.chanceChanges.entrySet())
			ProfileManager.resetRewardChanceValue(rewardInfo.getKey(), rewardInfo.getValue());
	}

	public Map<String, Map<String, Object>> getRewardSettings()
	{
		return rewardSettings;
	}

	@Override
	public String getID()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getDesc()
	{
		return desc;
	}

	public List<String> getRewardsToEnable()
	{
		return this.rewardsToEnable;
	}

	public List<String> getRewardsToDisable()
	{
		return this.rewardsToDisable;
	}

	public List<String> getChanceValueChanges()
	{
		List<String> toReturn = new ArrayList<>();
		for(Entry<String, Integer> change : this.chanceChanges.entrySet())
			toReturn.add(change.getKey() + " -> " + change.getValue());
		return toReturn;
	}

	public List<IProfile> getSubProfiles()
	{
		return this.subProfiles;
	}

	@Override
	public String getDescLong()
	{
		if(descFull.length() == 0 && !desc.isEmpty())
		{
			descFull.append(desc);
			descFull.append("\n");
			descFull.append("=== Rewards Enabled ===");
			descFull.append("\n");
			if(this.rewardsToEnable.size() == 0)
				descFull.append("None\n");
			for(String s : this.rewardsToEnable)
			{
				descFull.append(s);
				descFull.append("\n");
			}
			descFull.append("=== Rewards Disabled ===");
			descFull.append("\n");
			if(this.rewardsToDisable.size() == 0)
				descFull.append("None\n");
			for(String s : this.rewardsToDisable)
			{
				descFull.append(s);
				descFull.append("\n");
			}
			descFull.append("=== Triggers ===");
			descFull.append("\n");
			if(this.triggers.size() == 0)
				descFull.append("None\n");
			for(ITrigger<?> t : this.triggers.keySet())
			{
				descFull.append(t.getTriggerDesc());
				descFull.append("\n");
			}
			descFull.append("=== Reward Chance Value Changes ===");
			descFull.append("\n");
			if(this.chanceChanges.size() == 0)
				descFull.append("None\n");
			for(Entry<String, Integer> change : this.chanceChanges.entrySet())
			{
				descFull.append(change.getKey()).append(" -> ").append(change.getValue());
				descFull.append("\n");
			}
			descFull.append("=== Sub Profiles ===");
			descFull.append("\n");
			if(this.subProfiles.size() == 0)
				descFull.append("None\n");
			for(IProfile subProf : this.subProfiles)
			{
				descFull.append(subProf.getName()).append(" (").append(subProf.getID()).append(")");
				descFull.append("\n");
			}
		}
		return descFull.toString();
	}

	@Override
	public Set<ITrigger<?>> getTriggers()
	{
		return triggers.keySet();
	}

	public void setTriggerState(ITrigger<?> trigger, boolean completed)
	{
		this.triggers.replace(trigger, completed);
		for(Boolean triggerCompeleted : triggers.values())
		{
			//Bit awkward looking code, but idk what to do right now
			if(this.enabled && !triggerCompeleted && !anyTrigger)
			{
				ProfileManager.disableProfile(this);
				return;
			}
			else if(this.enabled && !triggerCompeleted && anyTrigger)
			{
				continue;
			}
			else if(!this.enabled && triggerCompeleted && anyTrigger)
			{
				ProfileManager.enableProfile(this);
				return;
			}
			else if(!this.enabled && triggerCompeleted && !anyTrigger)
			{
				continue;
			}
			else
			{
				return;
			}
		}

		if(enabled)
			ProfileManager.disableProfile(this);
		else
			ProfileManager.enableProfile(this);
	}
}

/*						AnyTrigger			!AnyTrigger
Last Enabled				N/C					E
First enabled				E					N/C
Last disabled				D					N/C
First Disabled				N/C					D
 */