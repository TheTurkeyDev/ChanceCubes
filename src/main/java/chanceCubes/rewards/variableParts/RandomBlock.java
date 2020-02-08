package chanceCubes.rewards.variableParts;

import chanceCubes.util.RewardsUtil;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class RandomBlock implements IPart
{
	@Override
	public String getValue()
	{
		return RewardsUtil.getRandomBlock().getRegistryName().toString();
	}
}
