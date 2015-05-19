package chanceCubes.rewards.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRewardType extends MultipleRewardType<ItemStack>
{
    public ItemRewardType(ItemStack... items)
    {
        super(items);
    }

    @Override
    public void trigger(ItemStack stack, World world, int x, int y, int z, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            Entity itemEnt = new EntityItem(world, x, y, z, stack.copy());
            world.spawnEntityInWorld(itemEnt);
        }
    }
}
