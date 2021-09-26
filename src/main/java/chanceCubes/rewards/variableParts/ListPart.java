package chanceCubes.rewards.variableParts;

import chanceCubes.util.RewardsUtil;

public class ListPart<T> implements IPart
{
	private final T[] list;
	
	public ListPart(T[] list)
	{
		this.list = list;
	}
	@Override
	public String getValue()
	{
		return String.valueOf(list[RewardsUtil.rand.nextInt(list.length)]);
	}

}
