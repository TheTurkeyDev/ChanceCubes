package chanceCubes.profiles.triggers;

public interface ITrigger<T>
{
	void onTrigger(String playerUUID, T[] args);

	String getTriggerDesc();
}
