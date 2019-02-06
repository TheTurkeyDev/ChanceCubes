package chanceCubes.rewards.variableTypes;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.variableParts.ListPart;
import chanceCubes.rewards.variableParts.RandomPart;
import chanceCubes.rewards.variableParts.StringPart;

public class FloatVar extends CustomVar
{
	public FloatVar()
	{
		
	}

	public FloatVar(float val)
	{
		super.addPart(new StringPart(val));
	}

	public FloatVar(Float[] val)
	{
		this.addPart(new ListPart<Float>(val));
	}

	public float getFloatValue()
	{
		String val = super.getValue();
		if(isFloat(val))
			return Float.parseFloat(val);
		else
			CCubesCore.logger.log(Level.ERROR, "An Error occurred while processing a Reward value! " + val + " is not a float!");
		return 0;
	}

	public static RandomPart<Float> parseRandom(String input)
	{
		input = input.replaceAll(" ", "");
		if(input.charAt(3) == '(' && input.indexOf(')', 3) != -1)
		{
			String[] randParams = input.substring(4, input.lastIndexOf(')')).split(",");
			if(randParams.length == 1 && isFloat(randParams[0]))
				return new RandomPart<Float>(0f, Float.parseFloat(randParams[0]));
			else if(randParams.length == 2 && isFloat(randParams[0]) && isFloat(randParams[1]))
				return new RandomPart<Float>(Float.parseFloat(randParams[0]), Float.parseFloat(randParams[1]));
			//TODO: Maybe add step random?
		}
		return new RandomPart<Float>(0f, 1f);
	}

	public static boolean isFloat(String input)
	{
		//TODO: Check that it is within the range of an integer?
		return input.matches("[-+]?[0-9]*\\.?[0-9]+");
	}

}
