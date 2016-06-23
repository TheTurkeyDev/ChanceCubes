package chanceCubes.rewards.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ItemRewardType extends BaseRewardType<ItemPart>
{
	public ItemRewardType(ItemPart... items)
	{
		super(items);
	}

	@Override
	public void trigger(final ItemPart part, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		if(part.getDelay() != 0)
		{
			Task task = new Task("ItemStack Reward Delay", part.getDelay())
			{
				@Override
				public void callback()
				{
					spawnStack(part, world, x, y, z, player);
				}
			};
			Scheduler.scheduleTask(task);
		}
		else
		{
			spawnStack(part, world, x, y, z, player);
		}
	}

	public void spawnStack(ItemPart part, World world, int x, int y, int z, EntityPlayer player)
	{
		Entity itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, part.getItemStack().copy());
		world.spawnEntityInWorld(itemEnt);
	}
}
