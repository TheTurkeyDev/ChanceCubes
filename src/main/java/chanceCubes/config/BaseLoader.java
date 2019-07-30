package chanceCubes.config;

import java.io.File;
import java.util.Arrays;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.variableParts.ListPart;
import chanceCubes.rewards.variableParts.StringPart;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import chanceCubes.rewards.variableTypes.StringVar;

public class BaseLoader
{
	protected String currentParsingReward = "";
	protected String currentParsingPart = "";
	protected int lineNumber = 0;

	protected File folder;

	public IntVar getInt(String input, int defaultVal)
	{
		IntVar var = new IntVar();
		String[] parts = input.split("%%");
		for(String part : parts)
		{
			if(part.isEmpty())
				continue;

			if(part.startsWith("RND"))
			{
				var.addPart(IntVar.parseRandom(part));
			}
			else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
			{
				var.addPart(new ListPart<Integer>(Arrays.stream(part.replaceAll("\\s", "").substring(1, part.lastIndexOf(']')).split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new)));
			}
			else if(IntVar.isInteger(part))
			{
				var.addPart(new StringPart(part));
			}
			else
			{
				CCubesCore.logger.log(Level.ERROR, "An integer was expected, but " + part + " was recieved for the " + this.currentParsingPart + " reward part in the \"" + this.currentParsingReward + "\" reward.");
				CCubesCore.logger.log(Level.ERROR, "If " + part + " was not what you entered than this may be an issue with the mod and please report to the mod author!");
			}
		}

		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public FloatVar getFloat(String input, float defaultVal)
	{
		FloatVar var = new FloatVar();

		String[] parts = input.split("%%");
		for(String part : parts)
		{
			if(part.isEmpty())
				continue;

			if(part.startsWith("RND"))
			{
				var.addPart(FloatVar.parseRandom(part));
			}
			else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
			{
				var.addPart(new ListPart<Float>(Arrays.stream(part.replaceAll("\\s", "").substring(1, part.lastIndexOf(']')).split(",")).map(Float::parseFloat).toArray(Float[]::new)));
			}
			if(FloatVar.isFloat(part))
			{
				var.addPart(new StringPart(part));
			}
			else
			{
				CCubesCore.logger.log(Level.ERROR, "An float was expected, but " + part + " was recieved for the " + this.currentParsingPart + " reward part in the \"" + this.currentParsingReward + "\" reward.");
				CCubesCore.logger.log(Level.ERROR, "If " + part + " was not what you entered than this may be an issue with the mod and please report to the mod author!");
			}

		}
		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public BoolVar getBoolean(String input, boolean defaultVal)
	{
		BoolVar var = new BoolVar();
		String[] parts = input.split("%%");
		for(String part : parts)
		{
			if(part.isEmpty())
				continue;

			if(part.startsWith("RND"))
				var.addPart(BoolVar.parseRandom(part));
			else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
				var.addPart(new ListPart<Boolean>(Arrays.stream(part.replaceAll("\\s", "").substring(1, part.lastIndexOf(']')).split(",")).map(Boolean::parseBoolean).toArray(Boolean[]::new)));
			else
				var.addPart(new StringPart(part));

		}
		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public StringVar getString(String input, String defaultVal)
	{
		StringVar var = new StringVar();
		String[] parts = input.split("%%");
		for(String part : parts)
		{
			if(part.isEmpty())
				continue;

			if(part.startsWith("RND"))
				var.addPart(IntVar.parseRandom(part));
			else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
				var.addPart(new ListPart<String>(part.replaceAll("\\s", "").substring(1, part.lastIndexOf(']') - 1).split(",")));
			else
				var.addPart(new StringPart(part));
		}
		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public NBTVar getNBT(String input)
	{
		NBTVar var = new NBTVar();
		String[] parts = input.split("%%");
		for(String part : parts)
		{
			if(part.isEmpty())
				continue;

			if(part.startsWith("RND"))
				var.addPart(IntVar.parseRandom(part));
			else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
				var.addPart(new ListPart<String>(part.replaceAll("\\s", "").substring(1, part.lastIndexOf(']')).split(",")));
			else
				var.addPart(new StringPart(part));
		}
		if(var.isEmpty())
			var.addPart(new StringPart(""));

		return var;
	}
}
