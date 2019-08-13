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
		if(val == null)
			val = new NBTTagCompound();
		super.addPart(new StringPart(val.toString()));
	}

	public NBTVar(String val)
	{
		this.addPart(new StringPart(val));
	}

	public NBTTagCompound getNBTValue()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		String val = super.getValue();
		if(val.isEmpty())
			return nbt;
		try
		{
			nbt = JsonToNBT.getTagFromJson(val);
		} catch(NBTException e)
		{
			e.printStackTrace();
		}
		return nbt;
	}
}
