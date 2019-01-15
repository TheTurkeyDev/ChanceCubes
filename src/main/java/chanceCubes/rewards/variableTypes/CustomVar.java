package chanceCubes.rewards.variableTypes;

import chanceCubes.util.RewardsUtil;

public abstract class CustomVar<T>
{
	protected VarType isRandom;

	protected T[] list;

	public CustomVar(VarType random)
	{
		this.isRandom = random;
	}

	public CustomVar(VarType random, T[] list)
	{
		this(random);
		this.list = list;
	}

	public abstract T getValue();

	protected T getRandomListVal(T defaultVal)
	{
		if(list == null || list.length == 0)
			return defaultVal;
		return list[RewardsUtil.rand.nextInt(list.length)];
	}

	public VarType isRandom()
	{
		return isRandom;
	}

	public enum VarType
	{
		Random, List, Single;
	}
}
