package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import net.minecraft.item.ItemStack;

public class ItemPart extends BasePart
{
	private ItemStack stack;

	public ItemPart(ItemStack stack)
	{
		this(stack, 0);
	}

	public ItemPart(ItemStack stack, int delay)
	{
		this(stack, new IntVar(delay));
	}

	public ItemPart(ItemStack stack, IntVar delay)
	{
		this.stack = stack;
		this.setDelay(delay);
	}

	public ItemStack getItemStack()
	{
		return stack;
	}
}
