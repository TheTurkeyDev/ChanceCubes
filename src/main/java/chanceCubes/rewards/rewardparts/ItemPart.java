package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemPart extends BasePart
{
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
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", stack.getItem().getRegistryName().toString());
		nbt.setByte("Count", (byte) stack.getCount());
		nbt.setShort("Damage", (short) stack.getItemDamage());
		nbt.setTag("tag", stack.getTagCompound());
		this.itemNBT = new NBTVar(nbt);
		this.setDelay(delay);
	}

	public ItemPart(String nbt)
	{
		this(new NBTVar(nbt), new IntVar(0));
	}

	public ItemPart(NBTVar nbt)
	{
		this(nbt, new IntVar(0));
	}

	public ItemPart(NBTVar nbt, IntVar delay)
	{
		this.itemNBT = nbt;
		this.setDelay(delay);
	}

	public ItemStack getItemStack()
	{
		return new ItemStack(this.itemNBT.getNBTValue());
	}
}
