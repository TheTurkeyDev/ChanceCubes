package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;

public abstract class BasePart
{
	protected IntVar delay = new IntVar(0);

	public int getDelay()
	{
		return delay.getIntValue();
	}

	public void setDelay(int delay)
	{
		this.setDelay(new IntVar(delay));
	}

	public void setDelay(IntVar delay)
	{
		this.delay = delay;
	}
}
