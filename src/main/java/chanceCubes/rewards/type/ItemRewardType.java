package chanceCubes.rewards.type;

import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemRewardType extends BaseRewardType<ItemPart>
{
	public ItemRewardType(ItemPart... items)
	{
		super(items);
	}

	@Override
	public void trigger(final ItemPart part, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("ItemStack Reward Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				EntityItem itemEnt = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, part.getItemStack().copy());
				itemEnt.setPickupDelay(10);
				world.spawnEntity(itemEnt);
			}
		});
	}
}
