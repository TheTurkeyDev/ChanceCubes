package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class BaseRewardType<T> implements IRewardType
{
	protected T[] rewards;

	@SafeVarargs
	protected BaseRewardType(T... rewards)
	{
		this.rewards = rewards;
	}

	@Override
	public void trigger(final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		for (T t : rewards)
			trigger(t, world, x, y, z, player);
	}

	protected abstract void trigger(T obj, World world, int x, int y, int z, EntityPlayer player);
	
	public T[] getRewardParts()
	{
		return this.rewards.clone();
	}
}
