package chanceCubes.rewards.rewardparts;

import chanceCubes.mcwrapper.ItemWrapper;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemPart extends BasePart
{
	private final NBTVar itemNBT;

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
		CompoundTag nbt = new CompoundTag();
		nbt.putString("id", ItemWrapper.getItemIdStr(stack));
		nbt.putByte("Count", (byte) stack.getCount());
		nbt.putShort("Damage", (short) stack.getDamageValue());
		nbt.put("tag", stack.getTag() == null ? new CompoundTag() : stack.getTag());
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
		return ItemStack.of(this.itemNBT.getNBTValue());
	}
}