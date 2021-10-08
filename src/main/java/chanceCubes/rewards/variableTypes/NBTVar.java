package chanceCubes.rewards.variableTypes;

import chanceCubes.rewards.variableParts.StringPart;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;

public class NBTVar extends CustomVar
{
	public NBTVar()
	{

	}

	public NBTVar(CompoundTag val)
	{
		if(val == null)
			val = new CompoundTag();
		super.addPart(new StringPart(val.toString()));
	}

	public NBTVar(String val)
	{
		this.addPart(new StringPart(val));
	}

	public CompoundTag getNBTValue()
	{
		CompoundTag nbt = new CompoundTag();
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
