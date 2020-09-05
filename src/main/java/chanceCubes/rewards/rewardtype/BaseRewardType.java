package chanceCubes.rewards.rewardtype;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public abstract class BaseRewardType<T> implements IRewardType
{
	protected T[] rewards;

	@SafeVarargs
	protected BaseRewardType(T... rewards)
	{
		this.rewards = rewards;
	}

	@Override
	public void trigger(final ServerWorld world, final int x, final int y, final int z, final PlayerEntity player)
	{
		for(T t : rewards)
			trigger(t, world, x, y, z, player);
	}

	protected abstract void trigger(T obj, ServerWorld world, int x, int y, int z, PlayerEntity player);

	public T[] getRewardParts()
	{
		return this.rewards.clone();
	}
}
