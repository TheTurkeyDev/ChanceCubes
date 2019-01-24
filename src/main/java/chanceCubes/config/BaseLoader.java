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
import chanceCubes.rewards.variableTypes.StringVar;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

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
		int index = -1;
		for(String part : parts)
		{
			index++;
			if(part.isEmpty())
				continue;

			if(index % 2 == 0)
			{
				if(IntVar.isInteger(part))
				{
					var.addPart(new StringPart(part));
				}
				else
				{
					CCubesCore.logger.log(Level.ERROR, "An integer was expected, but " + part + " was recieved for the " + this.currentParsingPart + " reward part in the \"" + this.currentParsingReward + "\" reward.");
					CCubesCore.logger.log(Level.ERROR, "If " + part + " was not what you entered than this may be an issue with the mod and please report to the mod author!");
				}
			}
			else
			{
				part = part.replaceAll(" ", "");
				if(part.startsWith("RND"))
					var.addPart(IntVar.parseRandom(part));
				else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
					var.addPart(new ListPart<Integer>(Arrays.stream(part.substring(1, part.lastIndexOf(']')).split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new)));
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
		int index = -1;
		for(String part : parts)
		{
			index++;
			if(part.isEmpty())
				continue;

			if(index % 2 == 0)
			{
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
			else
			{
				part = part.replaceAll(" ", "");
				if(part.startsWith("RND"))
					var.addPart(FloatVar.parseRandom(part));
				else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
					var.addPart(new ListPart<Float>(Arrays.stream(part.substring(1, part.lastIndexOf(']')).split(",")).map(Float::parseFloat).toArray(Float[]::new)));
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
		int index = -1;
		for(String part : parts)
		{
			index++;
			if(part.isEmpty())
				continue;

			if(index % 2 == 0)
			{
				var.addPart(new StringPart(part));
			}
			else
			{
				part = part.replaceAll(" ", "");
				if(part.startsWith("RND"))
					var.addPart(BoolVar.parseRandom(part));
				else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
					var.addPart(new ListPart<Boolean>(Arrays.stream(part.substring(1, part.lastIndexOf(']')).split(",")).map(Boolean::parseBoolean).toArray(Boolean[]::new)));
			}

		}
		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public StringVar getString(String input, String defaultVal)
	{
		StringVar var = new StringVar();
		String[] parts = input.split("%%");
		int index = -1;
		for(String part : parts)
		{
			index++;
			if(part.isEmpty())
				continue;

			if(index % 2 == 0)
			{
				var.addPart(new StringPart(part));
			}
			else
			{
				part = part.replaceAll(" ", "");
				if(part.startsWith("RND"))
					var.addPart(IntVar.parseRandom(part));
				else if(part.charAt(0) == '[' && part.indexOf(']') != -1)
					var.addPart(new ListPart<String>(part.substring(1, part.lastIndexOf(']')).split(",")));
			}
		}
		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public NBTTagCompound getNBT(String input)
	{
		try
		{
			NBTBase nbtbase = JsonToNBT.getTagFromJson(input);

			if(nbtbase instanceof NBTTagCompound)
				return (NBTTagCompound) nbtbase;

		} catch(NBTException e1)
		{
			CCubesCore.logger.log(Level.ERROR, e1.getMessage());
		}

		CCubesCore.logger.log(Level.ERROR, "Failed to convert the JSON to NBT for reward \"" + this.currentParsingReward + "\": " + input);
		return new NBTTagCompound();
	}
}
