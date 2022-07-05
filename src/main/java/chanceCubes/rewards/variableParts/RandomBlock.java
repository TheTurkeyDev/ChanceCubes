package chanceCubes.rewards.variableParts;

import chanceCubes.mcwrapper.BlockWrapper;
import chanceCubes.util.RewardsUtil;

public class RandomBlock implements IPart
{
	@Override
	public String getValue()
	{
		return BlockWrapper.getBlockIdStr(RewardsUtil.getRandomBlock());
	}
}
