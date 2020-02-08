package chanceCubes.parsers;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.variableParts.ListPart;
import chanceCubes.rewards.variableParts.RandomBlock;
import chanceCubes.rewards.variableParts.RandomItem;
import chanceCubes.rewards.variableParts.StringPart;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import chanceCubes.rewards.variableTypes.StringVar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.Level;

import java.util.Arrays;

public class ParserUtil
{
	public static IntVar getInt(JsonObject json, String key, int defaultVal)
	{
		String in = "";
		if(json.has(key))
			in = json.get(key).getAsString();

		return getInt(in, defaultVal);
	}

	public static IntVar getInt(String input, int defaultVal)
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
				var.addPart(new ListPart<>(Arrays.stream(part.replaceAll(" ", "").substring(1, part.lastIndexOf(']')).split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new)));
			}
			else if(IntVar.isInteger(part))
			{
				var.addPart(new StringPart(part));
			}
			else
			{
				CCubesCore.logger.log(Level.ERROR, "An integer was expected, but " + part + " was recieved for the input " + input);
				CCubesCore.logger.log(Level.ERROR, "If " + part + " was not what you entered than this may be an issue with the mod and please report to the mod author!");
			}
		}

		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public static FloatVar getFloat(JsonObject json, String key, float defaultVal)
	{
		String in = "";
		if(json.has(key))
			in = json.get(key).getAsString();

		return getFloat(in, defaultVal);
	}

	public static FloatVar getFloat(String input, float defaultVal)
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
				var.addPart(new ListPart<>(Arrays.stream(part.replaceAll(" ", "").substring(1, part.lastIndexOf(']')).split(",")).map(Float::parseFloat).toArray(Float[]::new)));
			}
			if(FloatVar.isFloat(part))
			{
				var.addPart(new StringPart(part));
			}
			else
			{
				CCubesCore.logger.log(Level.ERROR, "An float was expected, but " + part + " was received for the input " + input);
				CCubesCore.logger.log(Level.ERROR, "If " + part + " was not what you entered than this may be an issue with the mod and please report to the mod author!");
			}

		}
		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public static BoolVar getBoolean(JsonObject json, String key, boolean defaultVal)
	{
		String in = "";
		if(json.has(key))
			in = json.get(key).getAsString();

		return getBoolean(in, defaultVal);
	}

	public static BoolVar getBoolean(String input, boolean defaultVal)
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
				var.addPart(new ListPart<>(Arrays.stream(part.replaceAll(" ", "").substring(1, part.lastIndexOf(']')).split(",")).map(Boolean::parseBoolean).toArray(Boolean[]::new)));
			else
				var.addPart(new StringPart(part));

		}
		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public static StringVar getString(JsonObject json, String key, String defaultVal)
	{
		String in = "";
		if(json.has(key))
			in = json.get(key).getAsString();

		return getString(in, defaultVal);
	}

	public static StringVar getString(String input, String defaultVal)
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
				var.addPart(new ListPart<>(part.replaceAll(" ", "").substring(1, part.lastIndexOf(']') - 1).split(",")));
			else if(part.startsWith("ITEM"))
				var.addPart(new RandomItem());
			else if(part.startsWith("BLOCK"))
				var.addPart(new RandomBlock());
			else
				var.addPart(new StringPart(part));
		}
		if(var.isEmpty())
			var.addPart(new StringPart(defaultVal));

		return var;
	}

	public static NBTVar getNBT(JsonObject json, String key)
	{
		String in = "";
		if(json.has(key))
		{
			JsonElement value = json.get(key);
			if(value.isJsonPrimitive())
			{
				in = value.getAsString();
				in = removedKeyQuotes(in);
			}
			else
			{
				in = json.getAsJsonObject(key).toString();
			}
		}

		return getNBT(in);
	}

	public static NBTVar getNBT(String input)
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
				var.addPart(new ListPart<>(part.replaceAll(" ", "").substring(1, part.lastIndexOf(']')).split(",")));
			else if(part.startsWith("ITEM"))
				var.addPart(new RandomItem());
			else if(part.startsWith("BLOCK"))
				var.addPart(new RandomBlock());
			else
				var.addPart(new StringPart(part));
		}
		if(var.isEmpty())
			var.addPart(new StringPart(""));

		return var;
	}

	public static String removedKeyQuotes(String raw)
	{
		StringBuilder sb = new StringBuilder(raw);
		int index = 0;
		while((index = sb.indexOf("\"", index)) != -1)
		{
			int secondQuote = sb.indexOf("\"", index + 1);
			if(secondQuote == -1)
				break;
			if(sb.charAt(secondQuote + 1) == ':')
			{
				sb.deleteCharAt(index);
				sb.delete(secondQuote - 1, secondQuote);
				index = secondQuote;
			}
			else
			{
				index++;
			}
		}
		return sb.toString();
	}
}
