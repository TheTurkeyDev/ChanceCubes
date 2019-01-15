package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import net.minecraft.item.ItemStack;

public class ItemPart
{
	private ItemStack stack;

	private IntVar delay = new IntVar(0);

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
		this.delay = delay;
	}

	public ItemStack getItemStack()
	{
		return stack;
	}

	public int getDelay()
	{
		return delay.getValue();
	}

	public ItemPart setDelay(int delay)
	{
		return this.setDelay(new IntVar(delay));
	}

	public ItemPart setDelay(IntVar delay)
	{
		this.delay = delay;
		return this;
	}
}
