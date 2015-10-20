package chanceCubes.rewards.rewardparts;

public class ExpirencePart
{
	private int amount;
	private int orbs = 1;

	private int delay = 0;

	public ExpirencePart(int amount)
	{
		this.amount = amount;
	}
	
	public ExpirencePart(int amount, int delay)
	{
		this.amount = amount;
		this.delay = delay;
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
