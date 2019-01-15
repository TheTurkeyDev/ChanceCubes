package chanceCubes.rewards.variableTypes;

public class StringVar extends CustomVar<String>
{
	private String value = "";

	public StringVar(String val)
	{
		super(VarType.Single);
		this.value = val;
	}

	public StringVar(String[] val)
	{
		super(VarType.List, val);
	}

	@Override
	public String getValue()
	{
		if(this.isRandom == VarType.List)
			return this.getRandomListVal("");
		return value;
	}

	public static StringVar parseRandom(String input, String defaultVal)
	{
		if(input.charAt(3) == '[' && input.indexOf(']', 3) != -1)
		{
			return new StringVar(input.substring(4, input.lastIndexOf(']')).split(","));
		}
		return new StringVar(defaultVal);
	}
}
