package chanceCubes.profiles;

import java.util.List;

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
}
