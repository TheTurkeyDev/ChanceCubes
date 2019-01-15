package chanceCubes.rewards.variableTypes;

import java.util.Arrays;

import chanceCubes.util.RewardsUtil;

public class IntVar extends CustomVar<Integer>
{
	private int lower = 0;
	private int upper = 1;

	public IntVar()
	{
		this(0);
	}

	public IntVar(int lower, int upper)
	{
		super(VarType.Random);
		this.lower = lower;
		this.upper = upper;
	}

	public IntVar(int val)
	{
		super(VarType.Single);
		this.upper = val;
	}

	public IntVar(Integer[] val)
	{
		super(VarType.List, val);
	}

	@Override
	public Integer getValue()
	{
		if(this.isRandom == VarType.Random)
			return RewardsUtil.rand.nextInt(upper - lower) + lower;
		else if(this.isRandom == VarType.List)
			return this.getRandomListVal(0);
		return upper;
	}

	public static IntVar parseRandom(String input, int defaultVal)
	{
		if(input.charAt(3) == '(' && input.indexOf(')', 3) != -1)
		{
			String[] randParams = input.substring(4, input.lastIndexOf(')')).split(",");
			if(randParams.length == 1 && isInteger(randParams[0]))
			{
				return new IntVar(0, Integer.parseInt(randParams[0]));
			}
			else if(randParams.length == 2 && isInteger(randParams[0]) && isInteger(randParams[1]))
			{
				return new IntVar(Integer.parseInt(randParams[0]), Integer.parseInt(randParams[1]));
			}
			//TODO: Maybe add step random?
		}
		else if(input.charAt(3) == '[' && input.indexOf(']', 3) != -1)
		{
			return new IntVar(Arrays.stream(input.substring(4, input.lastIndexOf(']')).split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new));
		}
		return new IntVar(defaultVal);
	}

	public static boolean isInteger(String input)
	{
		//TODO: Check that it is within the range of an integer?
		return input.matches("[0-9]*");
	}

}
