package chanceCubes.rewards.variableTypes;

import chanceCubes.rewards.variableParts.StringPart;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class NBTVar extends CustomVar
{
	public NBTVar()
	{

	}

	public NBTVar(NBTTagCompound val)
	{
		super.addPart(new StringPart(val.toString()));
	}

	public NBTVar(String val)
	{
		this.addPart(new StringPart(val.toString()));
	}

	public NBTTagCompound getNBTValue()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		try
		{
			nbt = JsonToNBT.getTagFromJson(super.getValue());
		} catch(NBTException e)
		{
			e.printStackTrace();
		}
		return nbt;
	}
}
