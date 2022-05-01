package chanceCubes.rewards.variableParts;

import chanceCubes.util.RewardsUtil;

public record ListPart<T>(T[] list) implements IPart
{
	@Override
	public String getValue()
	{
		return String.valueOf(list[RewardsUtil.rand.nextInt(list.length)]);
	}

}
