package chanceCubes.util;

import java.lang.reflect.Field;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import chanceCubes.CCubesCore;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class RenderUtil
{

	private static Field timerField = initTimer();

	private static Field initTimer()
	{
		Field f = null;
		try
		{
			f = ReflectionHelper.findField(Minecraft.class, "field_71428_T", "timer", "Q");
			f.setAccessible(true);
		} catch(Exception e)
		{
			CCubesCore.logger.error("Failed to initialize timer reflection.");
			e.printStackTrace();
		}
		return f;
	}

	@Nullable
	public static Timer getTimer()
	{
		if(timerField == null)
			return null;

		try
		{
			return (Timer) timerField.get(Minecraft.getMinecraft());
		} catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static long getCoordinateRandom(int x, int y, int z)
	{
		// MC 1.8 code...
		long l = (x * 3129871) ^ z * 116129781L ^ y;
		l = l * l * 42317861L + l * 11L;
		return l;
	}
}
