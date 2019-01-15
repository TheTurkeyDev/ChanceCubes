package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.IntVar;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class EntityPart
{
	private NBTTagCompound nbtData;

	private IntVar delay = new IntVar(0);

	private boolean removedBlocks = true;

	public EntityPart(NBTTagCompound nbtData)
	{
		this.nbtData = nbtData;
	}

	public EntityPart(String nbtRaw)
	{
		try
		{
			this.nbtData = (NBTTagCompound) JsonToNBT.getTagFromJson(nbtRaw);
		} catch(NBTException e)
		{
			e.printStackTrace();
		}
	}

	public NBTTagCompound getNBT()
	{
		return nbtData;
	}

	public int getDelay()
	{
		return delay.getValue();
	}

	public EntityPart setDelay(int delay)
	{
		return this.setDelay(new IntVar(delay));
	}

	public EntityPart setDelay(IntVar delay)
	{
		this.delay = delay;
		return this;
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
