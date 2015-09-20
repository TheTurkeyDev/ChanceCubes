package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.ChestChanceItem;

public class ChestRewardType extends BaseRewardType<ChestChanceItem>
{
	private TileEntityChest chest;
	
	public ChestRewardType(ChestChanceItem... items)
	{
		super(items);
	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		world.setBlock(x, y, z, Blocks.chest);
		chest = (TileEntityChest) world.getTileEntity(x, y, z);
		
		for (ChestChanceItem item : rewards)
			trigger(item, world, x, y, z, player);
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
}