package chanceCubes.hookins;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.hookins.mods.ExtraUtilsModHook;
import net.minecraftforge.fml.common.Loader;

public class ModHookUtil
{
	public static void loadCustomModRewards()
	{
		if(Loader.isModLoaded("ExtraUtilities"))
		{
			new ExtraUtilsModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod ExtraUtilities");
		}
	}
}
