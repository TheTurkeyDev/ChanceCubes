package chanceCubes.config.script;

import chanceCubes.util.RewardsUtil;

public class ScriptParser
{
	public static String parseRandom(String input, String defaultVal)
	{
		if(input.charAt(3) == '(' && input.indexOf(')', 3) != -1)
		{
			String[] randParams = input.substring(4, input.lastIndexOf(')')).split(",");
			if(randParams.length == 1 && isInteger(randParams[0]))
			{
				return String.valueOf(RewardsUtil.rand.nextInt(Integer.parseInt(randParams[0])));
			}
			else if(randParams.length == 2 && isInteger(randParams[0]) && isInteger(randParams[1]))
			{
				return String.valueOf(RewardsUtil.rand.nextInt(Integer.parseInt(randParams[1])) + Integer.parseInt(randParams[0]));
			}
			//TODO: Maybe add step random?
		}
		else if(input.charAt(3) == '[' && input.indexOf(']', 3) != -1)
		{
			String[] randParams = input.substring(4, input.lastIndexOf(']')).split(",");
			return randParams[RewardsUtil.rand.nextInt(randParams.length)];
		}
		return defaultVal;
	}

	public static boolean isInteger(String input)
	{
		//TODO: Check that it is within the range of an integer?
		return input.matches("[0-9]*");
	}
}
