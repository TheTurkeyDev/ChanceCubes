package chanceCubes.rewards.profiles.triggers;

public interface ITrigger<T>
{
	public void onTrigger(T[] args);
}