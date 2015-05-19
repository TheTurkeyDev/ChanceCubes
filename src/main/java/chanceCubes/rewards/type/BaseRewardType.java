package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class BaseRewardType<T> implements IRewardType
{
    protected T[] rewards;

    protected BaseRewardType(T... rewards)
    {
        this.rewards = rewards;
    }

    @Override
    public void trigger(World world, int x, int y, int z, EntityPlayer player)
    {
        for (T t : rewards)
        {
            trigger(t, world, x, y, z, player);
        }
    }

    protected abstract void trigger(T obj, World world, int x, int y, int z, EntityPlayer player);
}
