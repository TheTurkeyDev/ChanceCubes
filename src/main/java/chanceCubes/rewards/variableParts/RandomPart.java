package chanceCubes.rewards.variableParts;

import chanceCubes.util.RewardsUtil;

public class RandomPart<T> implements IPart
{
	T upper;
	T lower;

	public RandomPart(T lower, T upper)
	{
		this.upper = upper;
		this.lower = lower;
	}

	@Override
	public String getValue()
	{
		if(upper instanceof Boolean)
			return String.valueOf(RewardsUtil.rand.nextBoolean());
		else if(upper instanceof Integer)
			return String.valueOf(RewardsUtil.rand.nextInt((int) upper - (int) lower) + (int) lower);
		else if(upper instanceof Float)
			return String.valueOf((float) lower + ((float) upper - (float) lower) * RewardsUtil.rand.nextFloat());
		else
			return String.valueOf(RewardsUtil.rand.nextInt());
	}

}
