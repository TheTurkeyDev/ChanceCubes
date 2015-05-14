package chanceCubes.rewards;

import net.minecraft.world.World;
import chanceCubes.rewards.type.IRewardType;

public class BasicReward implements IChanceCubeReward {

	private String name;
	private int luck;
	private IRewardType[] rewards;
	
	public BasicReward(String name, int luck, IRewardType... rewards)
	{
		this.name = name;
		this.luck = luck;
		this.rewards = rewards;
	}
	
	@Override
	public void trigger(World world, int x, int y, int z) {
		if(!world.isRemote)
		{
			if(rewards != null)
				for(IRewardType reward: rewards)
				{
					reward.trigger(world, x, y, z);
				}
		}
		
	}

	@Override
	public int getLuckValue() {
		return this.luck;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
