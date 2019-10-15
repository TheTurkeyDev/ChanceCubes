package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonObject;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;

public abstract class BaseCustomReward implements IChanceCubeReward
{
	private String name;
	private int chance;

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
	public void setChanceValue(int chance)
	{
		this.chance = chance;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public boolean getSettingAsBoolean(Map<String, Object> settings, String key, boolean defaultVal)
	{
		if(settings.containsKey(key))
		{
			try
			{
				return (boolean) settings.get(key);
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key) + " to a boolean!");
				return defaultVal;
			}
		}
		return defaultVal;
	}

	public int getSettingAsInt(Map<String, Object> settings, String key, int defaultVal, int min, int max)
	{
		return Math.max(Math.min(getSettingAsInt(settings, key, defaultVal), max), min);
	}

	public int getSettingAsInt(Map<String, Object> settings, String key, int defaultVal)
	{
		if(settings.containsKey(key))
			return getSettingAsNumber(settings, key, defaultVal).intValue();
		return defaultVal;
	}

	public double getSettingAsDouble(Map<String, Object> settings, String key, double defaultVal, double min, double max)
	{
		return Math.max(Math.min(getSettingAsDouble(settings, key, defaultVal), max), min);
	}

	public double getSettingAsDouble(Map<String, Object> settings, String key, double defaultVal)
	{
		if(settings.containsKey(key))
			return getSettingAsNumber(settings, key, defaultVal).intValue();
		return defaultVal;
	}

	public Number getSettingAsNumber(Map<String, Object> settings, String key, Number defaultVal)
	{
		if(settings.containsKey(key))
		{
			try
			{
				return (Number) settings.get(key);
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key) + " to a number!");
				return defaultVal;
			}
		}
		return defaultVal;
	}

	public String getSettingAsString(Map<String, Object> settings, String key, String defaultVal)
	{
		if(settings.containsKey(key))
		{
			try
			{
				return (String) settings.get(key);
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key) + " to a string!");
				return defaultVal;
			}
		}
		return defaultVal;
	}

	public String[] getSettingAsStringList(Map<String, Object> settings, String key, String[] defaultVal)
	{
		if(settings.containsKey(key))
		{
			try
			{
				return (String[]) settings.get(key);
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key) + " to a string list!");
				return defaultVal;
			}
		}
		return defaultVal;
	}

	public ItemStack[] getSettingAsItemStackList(Map<String, Object> settings, String key, ItemStack[] defaultVal)
	{
		if(settings.containsKey(key))
		{
			JsonObject[] stackObjects;
			try
			{
				stackObjects = (JsonObject[]) settings.get(key);
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, key + " setting failed! Failed to convert " + settings.get(key) + " to a JSON list!");
				return defaultVal;
			}

			List<ItemStack> stacks = new ArrayList<>();
			for(JsonObject obj : stackObjects)
			{
				ItemStack stack;
				try
				{
					stack = new ItemStack(JsonToNBT.getTagFromJson(obj.toString()));
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
