package chanceCubes.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;

public class ItemChancePendantT1 extends Item implements IChancePendant
{
	public ItemChancePendantT1()
	{
		this.setUnlocalizedName("chancePendantTier1");
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
		this.setMaxDamage(CCubesSettings.pendantUses);
	}

	public int getChanceIncrease()
	{
		return 10;
	}

	@Override
	public void damage(ItemStack stack)
	{
		stack.setItemDamage(stack.getItemDamage() - 1);
	}
}