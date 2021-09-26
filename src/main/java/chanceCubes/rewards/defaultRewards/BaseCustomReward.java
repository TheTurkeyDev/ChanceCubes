package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCustomReward implements IChanceCubeReward
{
	private final String name;
	private final int chance;

	public BaseCustomReward(String name, int chance)
	{
		this.name = name;
		this.chance = chance;
	}

	@Override
	public int getChanceValue()
	{
		return this.chance;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public boolean getSettingAsBoolean(JsonObject settings, String key, boolean defaultVal)
	{
		if(settings.has(key))
		{
			try
			{
				return settings.get(key).getAsBoolean();
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key).toString() + " to a boolean!");
				return defaultVal;
			}
		}
		return defaultVal;
	}

	public int getSettingAsInt(JsonObject settings, String key, int defaultVal, int min, int max)
	{
		return Math.max(Math.min(getSettingAsInt(settings, key, defaultVal), max), min);
	}

	public int getSettingAsInt(JsonObject settings, String key, int defaultVal)
	{
		return getSettingAsNumber(settings, key, defaultVal).intValue();
	}

	public double getSettingAsDouble(JsonObject settings, String key, double defaultVal, double min, double max)
	{
		return Math.max(Math.min(getSettingAsDouble(settings, key, defaultVal), max), min);
	}

	public double getSettingAsDouble(JsonObject settings, String key, double defaultVal)
	{
		return getSettingAsNumber(settings, key, defaultVal).intValue();
	}

	public Number getSettingAsNumber(JsonObject settings, String key, Number defaultVal)
	{
		if(settings.has(key))
		{
			try
			{
				return settings.get(key).getAsNumber();
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key).toString() + " to a number!");
				return defaultVal;
			}
		}
		return defaultVal;
	}

	public String getSettingAsString(JsonObject settings, String key, String defaultVal)
	{
		if(settings.has(key))
		{
			try
			{
				return settings.get(key).getAsString();
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key).toString() + " to a string!");
				return defaultVal;
			}
		}
		return defaultVal;
	}

	public String[] getSettingAsStringList(JsonObject settings, String key, String[] defaultVal)
	{
		if(settings.has(key))
		{
			try
			{
				JsonArray arr = settings.get(key).getAsJsonArray();
				String[] toReturn = new String[arr.size()];
				for(int i = 0; i < arr.size(); i++)
					toReturn[i] = arr.get(i).getAsString();
				return toReturn;
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key).toString() + " to a string list!");
				return defaultVal;
			}
		}
		return defaultVal;
	}

	public ItemStack[] getSettingAsItemStackList(JsonObject settings, String key, ItemStack[] defaultVal)
	{
		if(settings.has(key))
		{
			JsonArray stackObjects;
			try
			{
				stackObjects = settings.get(key).getAsJsonArray();
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key).toString() + " to a Json array!");
				return defaultVal;
			}

			List<ItemStack> stacks = new ArrayList<>();
			for(JsonElement obj : stackObjects)
			{
				ItemStack stack;
				try
				{
					stack = ItemStack.of(JsonToNBT.getTagFromJson(obj.toString()));
				} catch(Exception e)
				{
					CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + obj.toString() + " to an item stack!");
					continue;
				}
				stacks.add(stack);
			}

			return stacks.toArray(new ItemStack[0]);
		}
		return defaultVal;
	}
}
