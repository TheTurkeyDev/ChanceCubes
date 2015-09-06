package chanceCubes.hookins.mods;

public abstract class BaseModHook
{
	public String modId;

	private boolean enabled = false;

	public BaseModHook(String modId)
	{
		this.modId = modId;
		this.enabled = true;
	}

	public abstract void loadRewards();

	public boolean isEnabled()
	{
		return this.enabled;
	}
}
