package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;

public class ExpirencePart extends BasePart
{
	private IntVar amount;
	private IntVar orbs = new IntVar(1);

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
		this.setDelay(delay);
	}

	public int getAmount()
	{
		return amount.getIntValue();
	}

	public int getNumberofOrbs()
	{
		return orbs.getIntValue();
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
