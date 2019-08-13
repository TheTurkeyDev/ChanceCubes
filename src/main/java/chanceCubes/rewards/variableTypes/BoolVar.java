package chanceCubes.rewards.variableTypes;

import chanceCubes.rewards.variableParts.IPart;
import chanceCubes.rewards.variableParts.ListPart;
import chanceCubes.rewards.variableParts.RandomPart;
import chanceCubes.rewards.variableParts.StringPart;

public class BoolVar extends CustomVar
{
	public BoolVar()
	{

	}

	public BoolVar(boolean val)
	{
		this.addPart(new StringPart(val));
	}

	public BoolVar(Boolean[] val)
	{
		this.addPart(new ListPart<>(val));
	}

	public BoolVar(IPart part)
	{
		this.addPart(part);
	}

	public Boolean getBoolValue()
	{
		return Boolean.parseBoolean(super.getValue());
	}

	public static RandomPart<Boolean> parseRandom(String input)
	{
		return new RandomPart<>(false, true);
	}
}
