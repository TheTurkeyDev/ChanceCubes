package chanceCubes.profiles;

import java.util.List;
import java.util.Map;

import chanceCubes.profiles.triggers.ITrigger;

public interface IProfile
{
	String getID();

	String getName();

	String getDesc();

	String getDescLong();

	void onEnable();

	void onDisable();

	List<ITrigger<?>> getTriggers();

	Map<String, Map<String, Object>> getRewardSettings();
}
