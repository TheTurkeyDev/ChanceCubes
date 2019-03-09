package chanceCubes.rewards.defaultRewards;

import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.type.IRewardType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasicReward implements IChanceCubeReward
{
	private String name;
	private int chance;
	private IRewardType[] rewards;

	public BasicReward(String name, int chance, IRewardType... rewards)
	{
		this.name = name;
		this.chance = chance;
		this.rewards = rewards;
	}

	@Override
	public void trigger(World world, BlockPos position, EntityPlayer player)
	{
		if(!world.isRemote && rewards != null)
			for(IRewardType reward : rewards)
				reward.trigger(world, position.getX(), position.getY(), position.getZ(), player);
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