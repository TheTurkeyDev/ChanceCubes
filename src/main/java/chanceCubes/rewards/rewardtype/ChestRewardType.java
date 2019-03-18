package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ChestChanceItem;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChestRewardType extends BaseRewardType<ChestChanceItem>
{
	private TileEntityChest chest;

	private int delay = 0;

	public ChestRewardType(ChestChanceItem... items)
	{
		super(items);
	}

	@Override
	public void trigger(final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Chest Reward Delay", delay)
		{
			@Override
			public void callback()
			{
				RewardsUtil.placeBlock(Blocks.CHEST.getDefaultState(), world, new BlockPos(x, y, z));
				chest = (TileEntityChest) world.getTileEntity(new BlockPos(x, y, z));

				for(ChestChanceItem item : rewards)
					trigger(item, world, x, y, z, player);
			}
		});

	}

	@Override
	protected void trigger(ChestChanceItem item, World world, int x, int y, int z, EntityPlayer player)
	{
		boolean addToChest = world.rand.nextInt(100) < item.getChance();
		if(addToChest)
		{
			int slot = world.rand.nextInt(chest.getSizeInventory());
			chest.setInventorySlotContents(slot, item.getRandomItemStack());
		}
	}

	public ChestRewardType setDelay(int delay)
	{
		this.delay = delay;
		return this;
	}
}