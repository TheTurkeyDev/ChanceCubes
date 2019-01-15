package chanceCubes.rewards.variableTypes;

import java.util.Arrays;

import chanceCubes.util.RewardsUtil;

public class BoolVar extends CustomVar<Boolean>
{
	private Boolean val;

	public BoolVar()
	{
		super(VarType.Random);
	}

	public BoolVar(Boolean val)
	{
		super(VarType.Single);
		this.val = val;
	}

	public BoolVar(Boolean[] val)
	{
		super(VarType.List, val);
	}

	@Override
	public Boolean getValue()
	{
		if(this.isRandom == VarType.Random)
			return RewardsUtil.rand.nextBoolean();
		else if(this.isRandom == VarType.List)
			return this.getRandomListVal(true);
		return val;
	}

	public static BoolVar parseRandom(String input, Boolean defaultVal)
	{
		if(input.charAt(3) == '(' && input.indexOf(')', 3) != -1)
		{
			return new BoolVar();
		}
		else if(input.charAt(3) == '[' && input.indexOf(']', 3) != -1)
		{
			return new BoolVar(Arrays.stream(input.substring(4, input.lastIndexOf(']')).split(",")).map(Boolean::parseBoolean).toArray(Boolean[]::new));
		}
		return new BoolVar(defaultVal);
	}

	public static boolean isInteger(String input)
	{
		//TODO: Check that it is within the range of an integer?
		return input.matches("[0-9]*");
	}

}
