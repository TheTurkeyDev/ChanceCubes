package chanceCubes.rewards.rewardtype;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public abstract class BaseRewardType<T> implements IRewardType
{
	protected T[] rewards;

	@SafeVarargs
	protected BaseRewardType(T... rewards)
	{
		this.rewards = rewards;
	}

	@Override
	public void trigger(final ServerLevel level, final int x, final int y, final int z, final Player player)
	{
		for(T t : rewards)
			trigger(t, level, x, y, z, player);
	}

	protected abstract void trigger(T obj, ServerLevel world, int x, int y, int z, Player player);

	public T[] getRewardParts()
	{
		return this.rewards.clone();
	}
}
