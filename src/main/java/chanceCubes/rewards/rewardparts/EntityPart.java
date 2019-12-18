package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import net.minecraft.nbt.NBTTagCompound;

public class EntityPart extends BasePart
{
	private NBTVar nbtData;

	private BoolVar removedBlocks = new BoolVar(true);
	private IntVar copies = new IntVar(0);

	public EntityPart(NBTTagCompound nbtData)
	{
		this(nbtData, 0);
	}

	public EntityPart(NBTTagCompound nbtData, int delay)
	{
		this(new NBTVar(nbtData), new IntVar(delay));
	}

	public EntityPart(String nbtRaw)
	{
		this(new NBTVar(nbtRaw), new IntVar(0));
	}

	public EntityPart(NBTVar nbt, IntVar delay)
	{
		this.nbtData = nbt;
		this.setDelay(delay);
	}

	public NBTTagCompound getNBT()
	{
		return nbtData.getNBTValue();
	}

	public boolean shouldRemovedBlocks()
	{
		return removedBlocks.getBoolValue();
	}

	public EntityPart setRemovedBlocks(BoolVar removedBlocks)
	{
		this.removedBlocks = removedBlocks;
		return this;
	}

	public IntVar getCopies()
	{
		return copies;
	}

	public void setCopies(IntVar copies)
	{
		this.copies = copies;
	}
}
