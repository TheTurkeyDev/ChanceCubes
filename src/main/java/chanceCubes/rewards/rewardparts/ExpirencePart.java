package chanceCubes.rewards.rewardparts;

public class ExpirencePart
{
	private int amount;
	private int orbs;

	private int delay = 0;

	public ExpirencePart(int amount)
	{
		this.amount = amount;
	}

	public int getAmount()
	{
		return amount;
	}

	public int getDelay()
	{
		return delay;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}
	
	public int getNumberofOrbs()
	{
		return orbs;
	}

	public ExpirencePart setNumberofOrbs(int orbs)
	{
		this.orbs = orbs;
		return this;
	}
}
