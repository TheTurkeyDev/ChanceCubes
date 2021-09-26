package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ChestChanceItem;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class ChestRewardType extends BaseRewardType<ChestChanceItem>
{
	private ChestBlockEntity chest;

	private int delay = 0;

	public ChestRewardType(ChestChanceItem... items)
	{
		super(items);
	}

	@Override
	public void trigger(final ServerLevel level, final int x, final int y, final int z, final Player player)
	{
		Scheduler.scheduleTask(new Task("Chest Reward Delay", delay)
		{
			@Override
			public void callback()
			{
				RewardsUtil.placeBlock(Blocks.CHEST.defaultBlockState(), level, new BlockPos(x, y, z));
				chest = (ChestBlockEntity) level.getBlockEntity(new BlockPos(x, y, z));

				for(ChestChanceItem item : rewards)
					trigger(item, level, x, y, z, player);
			}
		});

	}

	@Override
	protected void trigger(ChestChanceItem item, ServerLevel level, int x, int y, int z, Player player)
	{
		boolean addToChest = RewardsUtil.rand.nextInt(100) < item.getChance();
		if(addToChest)
		{
			int slot = RewardsUtil.rand.nextInt(chest.getContainerSize());
			chest.setItem(slot, item.getRandomItemStack());
		}
	}

	public ChestRewardType setDelay(int delay)
	{
		this.delay = delay;
		return this;
	}
}