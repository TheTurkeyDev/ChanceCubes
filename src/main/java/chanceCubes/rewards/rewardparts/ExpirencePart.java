package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;

public class ExpirencePart
{
	private IntVar amount = new IntVar(0);
	private IntVar orbs = new IntVar(1);

	private IntVar delay = new IntVar(0);

	public ExpirencePart(int amount)
	{
		this(new IntVar(amount));
	}

	public ExpirencePart(IntVar amount)
	{
		this.amount = amount;
	}

	public ExpirencePart(int amount, int delay)
	{
		this(new IntVar(amount), new IntVar(delay));
	}

	public ExpirencePart(IntVar amount, IntVar delay)
	{
		this.amount = amount;
		this.delay = delay;
	}

	public int getAmount()
	{
		return amount.getValue();
	}

	public int getDelay()
	{
		return delay.getValue();
	}

	public void setDelay(int delay)
	{
		this.setDelay(new IntVar(delay));
	}

	public void setDelay(IntVar delay)
	{
		this.delay = delay;
	}

	public int getNumberofOrbs()
	{
		return orbs.getValue();
	}

	public ExpirencePart setNumberofOrbs(int orbs)
	{
		return this.setNumberofOrbs(new IntVar(orbs));
	}

	public ExpirencePart setNumberofOrbs(IntVar orbs)
	{
		this.orbs = orbs;
		return this;
	}
}
