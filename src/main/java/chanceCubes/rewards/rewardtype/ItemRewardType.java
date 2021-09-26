package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class ItemRewardType extends BaseRewardType<ItemPart>
{
	public ItemRewardType(ItemPart... items)
	{
		super(items);
	}

	@Override
	public void trigger(final ItemPart part, final ServerLevel level, final int x, final int y, final int z, final Player player)
	{
		Scheduler.scheduleTask(new Task("ItemStack Reward Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				ItemEntity itemEnt = new ItemEntity(level, x + 0.5, y + 0.5, z + 0.5, part.getItemStack().copy());
				itemEnt.setPickUpDelay(10);
				level.addFreshEntity(itemEnt);
			}
		});
	}
}
