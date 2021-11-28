package chanceCubes.rewards.variableTypes;

import chanceCubes.mcwrapper.JsonWrapper;
import chanceCubes.rewards.variableParts.StringPart;
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
		String val = super.getValue();
		if(val.isEmpty())
			return new CompoundTag();

		return JsonWrapper.getNBTFromJson(val);
	}
}
