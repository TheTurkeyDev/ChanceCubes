package chanceCubes.rewards.defaultRewards;

import chanceCubes.rewards.rewardtype.IRewardType;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class BasicReward extends BaseCustomReward
{
	private final IRewardType[] rewards;

	public BasicReward(String name, int chance, IRewardType... rewards)
	{
		super(name, chance);
		this.rewards = rewards;
	}

	@Override
	public void trigger(ServerLevel level, BlockPos position, Player player, JsonObject settings)
	{
		if(rewards != null)
			for(IRewardType reward : rewards)
				reward.trigger(level, position.getX(), position.getY(), position.getZ(), player);
	}
}