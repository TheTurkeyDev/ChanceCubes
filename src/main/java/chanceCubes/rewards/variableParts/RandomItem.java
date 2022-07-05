package chanceCubes.rewards.variableParts;

import chanceCubes.mcwrapper.ItemWrapper;
import chanceCubes.util.RewardsUtil;

public class RandomItem implements IPart
{
	@Override
	public String getValue()
	{
		return ItemWrapper.getItemIdStr(RewardsUtil.getRandomItem());
	}
}
