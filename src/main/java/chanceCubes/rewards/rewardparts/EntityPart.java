package chanceCubes.rewards.rewardparts;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class EntityPart
{
	private NBTTagCompound nbtData;

	private int delay = 0;

	public EntityPart(NBTTagCompound nbtData)
	{
		this.nbtData = nbtData;
	}

	public EntityPart(String nbtRaw)
	{
		try
		{
			this.nbtData = (NBTTagCompound) JsonToNBT.func_180713_a(nbtRaw);
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
		return delay;
	}

	public EntityPart setDelay(int delay)
	{
		this.delay = delay;
		return this;
	}
}
