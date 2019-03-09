package chanceCubes.rewards.defaultRewards;

import chanceCubes.rewards.IChanceCubeReward;

public abstract class BaseCustomReward implements IChanceCubeReward
{
	private int chance;

	@Override
	public int getChanceValue()
	{
		return chance;
	}

	@Override
	public void setChanceValue(int chance)
	{
		this.chance = chance;
	}
}
