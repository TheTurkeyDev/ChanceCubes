package chanceCubes.items;

import net.minecraft.item.ItemStack;

public interface IChancePendant
{
	public int getChanceIncrease();
	
	public void damage(ItemStack stack);
}
