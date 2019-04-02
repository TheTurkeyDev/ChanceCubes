package chanceCubes.rewards.defaultRewards;

import chanceCubes.rewards.IChanceCubeReward;

public abstract class BaseCustomReward implements IChanceCubeReward
{
	private String name;
	private int chance;
	
	public BaseCustomReward(String name, int chance)
	{
		this.name = name;
		this.chance = chance;
	}

	@Override
	public int getChanceValue()
	{
		return this.chance;
	}

	@Override
	public void setChanceValue(int chance)
	{
		this.chance = chance;
	}

	@Override
	public String getName()
	{
		return this.name;
	}
}