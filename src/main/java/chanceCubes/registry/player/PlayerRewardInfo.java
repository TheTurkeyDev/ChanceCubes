package chanceCubes.registry.player;

import chanceCubes.rewards.IChanceCubeReward;

import java.util.ArrayList;
import java.util.List;

public class PlayerRewardInfo
{
	public final IChanceCubeReward reward;
	private int chanceValue;
	private final List<Integer> chanceChangesCache;

	public PlayerRewardInfo(IChanceCubeReward reward)
	{
		this.reward = reward;
		this.chanceValue = reward.getChanceValue();
		chanceChangesCache = new ArrayList<>();
		chanceChangesCache.add(chanceValue);
	}

	public int getChanceValue()
	{
		return this.chanceValue;
	}


	public void setRewardChanceValue(int chance)
	{
		chanceChangesCache.add(chance);
		chanceValue = chance;
	}

	public void resetRewardChanceValue(int chanceFrom)
	{
		chanceChangesCache.remove((Integer) chanceFrom);
		if(chanceChangesCache.size() == 0)
		{
			// Again, something dun messed up
			return;
		}
		chanceValue = chanceChangesCache.get(chanceChangesCache.size() - 1);
	}
}
