package chanceCubes.rewards.variableTypes;

import chanceCubes.rewards.variableParts.ListPart;
import chanceCubes.rewards.variableParts.StringPart;

public class StringVar extends CustomVar
{

	public StringVar()
	{

	}

	public StringVar(String val)
	{
		super.addPart(new StringPart(val));
	}

	public StringVar(String[] val)
	{
		super.addPart(new ListPart<>(val));
	}
}
