package chanceCubes.rewards.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRewardType implements IRewardType
{
	private ItemStack[] items;
	
	public ItemRewardType(ItemStack... items)
	{
		this.items = items;
	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			if(items != null)
				for(ItemStack stack: items)
				{
					Entity itemEnt = new EntityItem(world, x, y, z, stack.copy());
					world.spawnEntityInWorld(itemEnt);
				}
		}
	}

}
