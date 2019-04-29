package chanceCubes.profiles.triggers;

public interface ITrigger<T>
{
	public void onTrigger(T[] args);
	
	public String getTriggerDesc();
}
