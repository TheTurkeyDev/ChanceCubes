package chanceCubes.hookins;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.hookins.mods.ExtraUtilsModHook;
import net.minecraftforge.fml.common.Loader;

public class ModHookUtil
{
	public static void loadCustomModRewards()
	{
		if(!CCubesSettings.enableHardCodedRewards)
			return;

		if(Loader.isModLoaded("extrautils2"))
		{
			new ExtraUtilsModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod ExtraUtilities");
		}
	}
}
