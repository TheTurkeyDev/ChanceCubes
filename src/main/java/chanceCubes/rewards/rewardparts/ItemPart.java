package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import chanceCubes.rewards.variableTypes.StringVar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemPart extends BasePart
{
	private StringVar item;
	private NBTVar itemNBT;

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
		this(new StringVar(stack.getItem().getRegistryName().toString()), new NBTVar(), delay);
	}

	public ItemPart(String item, String nbt)
	{
		this(new StringVar(item), new NBTVar(nbt), new IntVar(0));
	}

	public ItemPart(StringVar item, NBTVar nbt, IntVar delay)
	{
		this.item = item;
		this.itemNBT = nbt;
		this.setDelay(delay);
	}

	public ItemStack getItemStack()
	{
		NBTTagCompound nbt = itemNBT.getNBTValue();
		nbt.setString("id", item.getValue());
		return new ItemStack(nbt);
	}
}
