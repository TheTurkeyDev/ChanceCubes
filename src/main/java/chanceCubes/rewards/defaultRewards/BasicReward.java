package chanceCubes.rewards.defaultRewards;

import chanceCubes.rewards.rewardtype.IRewardType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class BasicReward extends BaseCustomReward
{
	private IRewardType[] rewards;

	public BasicReward(String name, int chance, IRewardType... rewards)
	{
		super(name, chance);
		this.rewards = rewards;
	}

	@Override
	public void trigger(World world, BlockPos position, PlayerEntity player, Map<String, Object> settings)
	{
		if(!world.isRemote && rewards != null)
			for(IRewardType reward : rewards)
				reward.trigger(world, position.getX(), position.getY(), position.getZ(), player);
	}
}