package chanceCubes.rewards.rewardparts;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class EntityPart extends BasePart
{
	private NBTTagCompound nbtData;

	private boolean removedBlocks = true;

	public EntityPart(NBTTagCompound nbtData)
	{
		this(nbtData, 0);
	}

	public EntityPart(NBTTagCompound nbtData, int delay)
	{
		this.nbtData = nbtData;
		this.setDelay(delay);
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
