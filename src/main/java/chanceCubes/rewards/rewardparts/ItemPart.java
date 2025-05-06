package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class ItemPart extends BasePart
{
	private final NBTVar itemNBT;

	public ItemPart(ItemStack stack, HolderLookup.Provider provider)
	{
		this(stack, 0, provider);
	}

	public ItemPart(ItemStack stack, int delay, HolderLookup.Provider provider)
	{
		this(stack, new IntVar(delay), provider);
	}

	public ItemPart(ItemStack stack, IntVar delay, HolderLookup.Provider provider)
	{
		Tag tag = ItemStack.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), stack)
				.getOrThrow(string -> new IllegalStateException("Failed to encode item stack: " + string));
		CompoundTag nbt = (CompoundTag) tag;
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

	public ItemStack getItemStack(HolderLookup.Provider provider)
	{
		return ItemStack.parseOptional(provider, this.itemNBT.getNBTValue());
	}
}