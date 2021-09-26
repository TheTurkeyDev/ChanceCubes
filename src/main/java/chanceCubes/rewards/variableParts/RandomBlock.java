package chanceCubes.rewards.variableParts;

import chanceCubes.util.RewardsUtil;

public class RandomBlock implements IPart
{
	@Override
	public String getValue()
	{
		return RewardsUtil.getRandomBlock().getRegistryName().toString();
	}
}
