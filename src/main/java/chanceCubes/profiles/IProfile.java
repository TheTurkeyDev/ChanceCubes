package chanceCubes.profiles;

import java.util.Map;
import java.util.Set;

import chanceCubes.profiles.triggers.ITrigger;

public interface IProfile
{
	String getID();

	String getName();

	String getDesc();

	String getDescLong();

	void onEnable();

	void onDisable();

	Set<ITrigger<?>> getTriggers();

	void setTriggerState(ITrigger<?> trigger, boolean completed);

	Map<String, Map<String, Object>> getRewardSettings();
}
