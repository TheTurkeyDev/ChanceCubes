package chanceCubes.profiles;

import java.util.List;
import java.util.Map;

import chanceCubes.profiles.triggers.ITrigger;

public interface IProfile
{
	public String getID();

	public String getName();

	public String getDesc();

	public String getDescLong();

	public void onEnable();

	public void onDisable();

	public List<ITrigger<?>> getTriggers();

	public Map<String, Map<String, Object>> getRewardSettings();
}
