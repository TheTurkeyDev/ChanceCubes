package chanceCubes.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemChancePendant extends BaseChanceCubesItem
{
	private final int chanceIncrease;

	public ItemChancePendant(int tier, int chanceBonus)
	{
		super((new Item.Properties()).durability(32), "chance_pendant_tier" + tier);
		chanceIncrease = chanceBonus;
		super.addLore("Increases the chance of Chance Cubes by:");
		super.addLore("      +" + chanceIncrease + " when the block is broken");
		super.addLore("Only needs to be in the players inventory to work");
		super.addLore("Note, this is NOT a percentage increase.");
	}

	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		return true;
	}

	public int getChanceIncrease()
	{
		return chanceIncrease;
	}

	public void damage(ItemStack stack)
	{
		stack.setDamageValue(stack.getDamageValue() + 1);
	}
}