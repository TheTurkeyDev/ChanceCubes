package chanceCubes.rewards.defaultRewards;

import chanceCubes.rewards.type.IRewardType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasicReward extends BaseCustomReward
{
	private IRewardType[] rewards;

	public BasicReward(String name, int chance, IRewardType... rewards)
	{
		super(name, chance);
		this.rewards = rewards;
	}

	@Override
	public void trigger(World world, BlockPos position, EntityPlayer player)
	{
		if(!world.isRemote && rewards != null)
			for(IRewardType reward : rewards)
				reward.trigger(world, position.getX(), position.getY(), position.getZ(), player);
	}
}