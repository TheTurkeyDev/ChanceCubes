package chanceCubes.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemChancePendant extends BaseChanceCubesItem
{
	private int chanceIncrease;

	public ItemChancePendant(int tier, int chancebonus)
	{
		super((new Item.Properties()).maxStackSize(1).defaultMaxDamage(32), "chance_pendant_tier" + tier);
		super.showDurabilityBar(new ItemStack(this));
		chanceIncrease = chancebonus;
		super.addLore("Increases the chance of Chance Cubes by:");
		super.addLore("      +" + chanceIncrease + " when the block is broken");
		super.addLore("Only needs to be in the players inventory to work");
		super.addLore("Note, this is NOT a percentage increase.");
	}

	public int getChanceIncrease()
	{
		return chanceIncrease;
	}

	public void damage(ItemStack stack)
	{
		stack.setDamage(stack.getDamage() + 1);
	}
}