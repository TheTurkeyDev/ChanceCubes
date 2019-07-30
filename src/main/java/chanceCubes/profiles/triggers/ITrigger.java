package chanceCubes.profiles.triggers;

public interface ITrigger<T>
{
	void onTrigger(T[] args);

	String getTriggerDesc();
}
