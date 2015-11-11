package chanceCubes.rewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import chanceCubes.rewards.type.IRewardType;

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
	public String getName()
	{
		return this.name;
	}
}