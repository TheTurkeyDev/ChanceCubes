package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

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
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("id", stack.getItem().getRegistryName().toString());
		nbt.putByte("Count", (byte) stack.getCount());
		nbt.putShort("Damage", (short) stack.getDamage());
		nbt.put("tag", stack.getTag() == null ? new CompoundNBT() : stack.getTag());
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
		return ItemStack.read(this.itemNBT.getNBTValue());
	}
}
