package chanceCubes.profiles;

import chanceCubes.profiles.triggers.ITrigger;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
