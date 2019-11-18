package chanceCubes.profiles;

import chanceCubes.profiles.triggers.ITrigger;

import java.util.List;
import java.util.Map;

public interface IProfile
{
	String getID();

	String getName();

	String getDesc();

	String getDescLong();

	void onEnable(PlayerProfileManager playerProfileManager, String playerUUID);

	void onDisable(PlayerProfileManager playerProfileManager, String playerUUID);

	boolean isRewardEnabled(String reward);

	List<ITrigger<?>> getTriggers();

	void setTriggerState(ITrigger<?> trigger, String playerUUID, boolean completed);

	Map<String, Map<String, Object>> getRewardSettings();
}
