package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import net.minecraft.nbt.CompoundNBT;

public class EntityPart extends BasePart
{
	private NBTVar nbtData;

	private boolean removedBlocks = true;

	public EntityPart(CompoundNBT nbtData)
	{
		this(nbtData, 0);
	}

	public EntityPart(CompoundNBT nbtData, int delay)
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

	public CompoundNBT getNBT()
	{
		return nbtData.getNBTValue();
	}

	public boolean shouldRemovedBlocks()
	{
		return removedBlocks;
	}

	public EntityPart setRemovedBlocks(boolean removedBlocks)
	{
		this.removedBlocks = removedBlocks;
		return this;
	}
}
