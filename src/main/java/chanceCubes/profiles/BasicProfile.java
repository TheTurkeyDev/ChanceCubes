package chanceCubes.profiles;

import chanceCubes.profiles.triggers.ITrigger;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.registry.player.PlayerCCRewardRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BasicProfile implements IProfile
{
	private Map<String, Map<ITrigger<?>, Boolean>> triggerStates = new HashMap<>();
	private String id;
	private String name;
	private String desc;
	private boolean anyTrigger = true;
	private StringBuilder descFull = new StringBuilder();
	private List<ITrigger<?>> triggers = new ArrayList<>();
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
		this.triggers.addAll(Arrays.asList(triggers));
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
	public void onEnable(PlayerProfileManager playerProfileManager, String playerUUID)
	{
		PlayerCCRewardRegistry defaultPlayerRewards = GlobalCCRewardRegistry.DEFAULT.getPlayerRewardRegistry(playerUUID);
		PlayerCCRewardRegistry giantPlayerRewards = GlobalCCRewardRegistry.GIANT.getPlayerRewardRegistry(playerUUID);

		for(IProfile prof : this.subProfiles)
			if(!playerProfileManager.isProfileEnabled(prof))
				prof.onEnable(playerProfileManager, playerUUID);

		for(String s : this.rewardsToDisable)
		{
			if(GlobalCCRewardRegistry.DEFAULT.isValidRewardName(s))
				defaultPlayerRewards.disableReward(s);
			else if(GlobalCCRewardRegistry.GIANT.isValidRewardName(s))
				giantPlayerRewards.disableReward(s);
		}

		for(String s : this.rewardsToEnable)
		{
			if(GlobalCCRewardRegistry.DEFAULT.isValidRewardName(s))
				defaultPlayerRewards.enableReward(s);
			else if(GlobalCCRewardRegistry.GIANT.isValidRewardName(s))
				giantPlayerRewards.enableReward(s);
		}

		for(Entry<String, Integer> rewardInfo : this.chanceChanges.entrySet())
		{
			if(GlobalCCRewardRegistry.DEFAULT.isValidRewardName(rewardInfo.getKey()))
				defaultPlayerRewards.setRewardChanceValue(rewardInfo.getKey(), rewardInfo.getValue());
			else if(GlobalCCRewardRegistry.GIANT.isValidRewardName(rewardInfo.getKey()))
				giantPlayerRewards.setRewardChanceValue(rewardInfo.getKey(), rewardInfo.getValue());
		}
	}

	@Override
	public void onDisable(PlayerProfileManager playerProfileManager, String playerUUID)
	{
		if(playerProfileManager.isProfileEnabled(this))
			return;

		PlayerCCRewardRegistry defaultPlayerRewards = GlobalCCRewardRegistry.DEFAULT.getPlayerRewardRegistry(playerUUID);
		PlayerCCRewardRegistry giantPlayerRewards = GlobalCCRewardRegistry.GIANT.getPlayerRewardRegistry(playerUUID);
		for(IProfile prof : this.subProfiles)
			prof.onDisable(playerProfileManager, playerUUID);

		for(String s : this.rewardsToDisable)
		{
			if(GlobalCCRewardRegistry.DEFAULT.isValidRewardName(s))
				defaultPlayerRewards.enableReward(s);
			else if(GlobalCCRewardRegistry.GIANT.isValidRewardName(s))
				giantPlayerRewards.enableReward(s);
		}

		for(String s : this.rewardsToEnable)
		{
			if(GlobalCCRewardRegistry.DEFAULT.isValidRewardName(s))
				defaultPlayerRewards.disableReward(s);
			else if(GlobalCCRewardRegistry.GIANT.isValidRewardName(s))
				giantPlayerRewards.disableReward(s);
		}

		for(Entry<String, Integer> rewardInfo : this.chanceChanges.entrySet())
		{
			if(GlobalCCRewardRegistry.DEFAULT.isValidRewardName(rewardInfo.getKey()))
				defaultPlayerRewards.setRewardChanceValue(rewardInfo.getKey(), rewardInfo.getValue());
			else if(GlobalCCRewardRegistry.GIANT.isValidRewardName(rewardInfo.getKey()))
				giantPlayerRewards.setRewardChanceValue(rewardInfo.getKey(), rewardInfo.getValue());
		}
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
			for(ITrigger<?> t : this.triggers)
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
	public boolean isRewardEnabled(String reward)
	{
		if(this.rewardsToEnable.contains(reward))
			return true;
		else if(this.rewardsToDisable.contains(reward))
			return false;
		for(IProfile subProf : this.subProfiles)
			if(!subProf.isRewardEnabled(reward))
				return false;
		return true;
	}

	@Override
	public List<ITrigger<?>> getTriggers()
	{
		return triggers;
	}

	public Map<ITrigger<?>, Boolean> getDefaultTriggerMap()
	{
		Map<ITrigger<?>, Boolean> triggerMap = new HashMap<>();
		for(ITrigger<?> trig : getTriggers())
			triggerMap.put(trig, false);
		return triggerMap;
	}

	public void setTriggerState(ITrigger<?> trigger, String playerUUID, boolean completed)
	{
		PlayerProfileManager playerProfileManager = GlobalProfileManager.getPlayerProfileManager(playerUUID);
		boolean enabled = playerProfileManager.isProfileEnabled(this);
		Map<ITrigger<?>, Boolean> playerTriggerStates = triggerStates.computeIfAbsent(playerUUID, k -> getDefaultTriggerMap());
		playerTriggerStates.replace(trigger, completed);

		if(!enabled && anyTrigger && completed)
		{
			playerProfileManager.enableProfile(this);
		}
		else if(enabled && !anyTrigger && !completed)
		{
			playerProfileManager.disableProfile(this);
		}
		else
		{
			boolean shouldDisable = true;
			boolean shouldEnable = true;
			for(Boolean triggerCompeleted : playerTriggerStates.values())
			{
				if(enabled && anyTrigger && triggerCompeleted)
				{
					shouldDisable = false;
					break;
				}
				else if(!enabled && !anyTrigger && !triggerCompeleted)
				{
					shouldEnable = false;
					break;
				}
			}

			if(shouldEnable)
				playerProfileManager.enableProfile(this);
			if(shouldDisable)
				playerProfileManager.disableProfile(this);
		}
	}
}