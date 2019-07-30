package chanceCubes.rewards.variableTypes;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import chanceCubes.rewards.variableParts.StringPart;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;

public class NBTVar extends CustomVar
{
	public NBTVar()
	{

	}

	public NBTVar(CompoundNBT val)
	{
		if(val == null)
			val = new CompoundNBT();
		super.addPart(new StringPart(val.toString()));
	}

	public NBTVar(String val)
	{
		this.addPart(new StringPart(val));
	}

	public CompoundNBT getNBTValue()
	{
		CompoundNBT nbt = new CompoundNBT();
		String val = super.getValue();
		if(val.isEmpty())
			return nbt;
		try
		{
			nbt = JsonToNBT.getTagFromJson(val);
		} catch(CommandSyntaxException e)
		{
			e.printStackTrace();
		}
		return nbt;
	}
}
