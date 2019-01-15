package chanceCubes.rewards.variableTypes;

import java.util.Arrays;

import chanceCubes.util.RewardsUtil;

public class FloatVar extends CustomVar<Float>
{
	private float lower = 0;
	private float upper = 1;

	public FloatVar()
	{
		this(0);
	}

	public FloatVar(float lower, float upper)
	{
		super(VarType.Random);
		this.lower = lower;
		this.upper = upper;
	}

	public FloatVar(float val)
	{
		super(VarType.Single);
		this.upper = val;
	}

	public FloatVar(Float[] val)
	{
		super(VarType.List, val);
	}

	@Override
	public Float getValue()
	{
		if(this.isRandom == VarType.Random)
			return lower + RewardsUtil.rand.nextFloat() * (upper - lower);
		else if(this.isRandom == VarType.List)
			return this.getRandomListVal(0f);
		return upper;
	}

	public static FloatVar parseRandom(String input, float defaultVal)
	{
		if(input.charAt(3) == '(' && input.indexOf(')', 3) != -1)
		{
			String[] randParams = input.substring(4, input.lastIndexOf(')')).split(",");
			if(randParams.length == 1 && isFloat(randParams[0]))
			{
				return new FloatVar(0, Integer.parseInt(randParams[0]));
			}
			else if(randParams.length == 2 && isFloat(randParams[0]) && isFloat(randParams[1]))
			{
				return new FloatVar(Integer.parseInt(randParams[0]), Integer.parseInt(randParams[1]));
			}
			//TODO: Maybe add step random?
		}
		else if(input.charAt(3) == '[' && input.indexOf(']', 3) != -1)
		{
			return new FloatVar(Arrays.stream(input.substring(4, input.lastIndexOf(']')).split(",")).map(Float::parseFloat).toArray(Float[]::new));
		}
		return new FloatVar(defaultVal);
	}

	public static boolean isFloat(String input)
	{
		//TODO: Check that it is within the range of an integer?
		return input.matches("[-+]?[0-9]*\\.?[0-9]+");
	}

}
