package chanceCubes.rewards.variableTypes;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.variableParts.ListPart;
import chanceCubes.rewards.variableParts.RandomPart;
import chanceCubes.rewards.variableParts.StringPart;

public class IntVar extends CustomVar
{
	public IntVar()
	{
		
	}

	public IntVar(int val)
	{
		super.addPart(new StringPart(val));
	}

	public IntVar(Integer[] val)
	{
		this.addPart(new ListPart<Integer>(val));
	}

	public int getIntValue()
	{
		String val = super.getValue();
		if(isInteger(val))
			return Integer.parseInt(val);
		else
			CCubesCore.logger.log(Level.ERROR, "An Error occurred while processing a Reward value! " + val + " is not an integer!");
		return 0;
	}

	public static RandomPart<Integer> parseRandom(String input)
	{
		input = input.replaceAll(" ", "");
		if(input.charAt(3) == '(' && input.indexOf(')', 3) != -1)
		{
			String[] randParams = input.substring(4, input.lastIndexOf(')')).split(",");
			if(randParams.length == 1 && isInteger(randParams[0]))
				return new RandomPart<Integer>(0, Integer.parseInt(randParams[0]));
			else if(randParams.length == 2 && isInteger(randParams[0]) && isInteger(randParams[1]))
				return new RandomPart<Integer>(Integer.parseInt(randParams[0]), Integer.parseInt(randParams[1]));
			//TODO: Maybe add step random?
		}
		return new RandomPart<Integer>(0,1);
	}

	public static boolean isInteger(String input)
	{
		//TODO: Check that it is within the range of an integer?
		return input.matches("[-+]?[0-9]*");
	}
}
