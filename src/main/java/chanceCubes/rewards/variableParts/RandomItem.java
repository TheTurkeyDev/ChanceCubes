package chanceCubes.rewards.variableParts;

import chanceCubes.util.RewardsUtil;

public class RandomItem implements IPart
{
	@Override
	public String getValue()
	{
		return RewardsUtil.getRandomItem().getRegistryName().toString();
	}
}
