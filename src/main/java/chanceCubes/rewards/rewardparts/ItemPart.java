package chanceCubes.rewards.rewardparts;

import net.minecraft.item.ItemStack;

public class ItemPart
{
	public static String[] elements = new String[] { "experienceAmount:I", "delay:I", "numberOfOrbs:I" };

	private ItemStack stack;

	private int delay = 0;

	public ItemPart(ItemStack stack)
	{
		this.stack = stack;
	}

	public ItemPart(ItemStack stack, int delay)
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
		return delay;
	}

	public ItemPart setDelay(int delay)
	{
		this.delay = delay;
		return this;
	}
}
