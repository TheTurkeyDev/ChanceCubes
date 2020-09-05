package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class ItemRewardType extends BaseRewardType<ItemPart>
{
	public ItemRewardType(ItemPart... items)
	{
		super(items);
	}

	@Override
	public void trigger(final ItemPart part, final ServerWorld world, final int x, final int y, final int z, final PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("ItemStack Reward Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				ItemEntity itemEnt = new ItemEntity(world, x + 0.5, y + 0.5, z + 0.5, part.getItemStack().copy());
				itemEnt.setPickupDelay(10);
				world.addEntity(itemEnt);
			}
		});
	}
}
