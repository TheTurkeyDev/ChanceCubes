package chanceCubes.hookins;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.hookins.mods.ExtraUtilsModHook;
import chanceCubes.hookins.mods.ThermalExpansionModHook;
import chanceCubes.hookins.mods.ThermalFoundationModHook;
import cpw.mods.fml.common.Loader;

public class ModHookUtil
{
	public static void loadCustomModRewards()
	{
		if(Loader.isModLoaded("ExtraUtilities"))
		{
			new ExtraUtilsModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod Extra Utilities");
		}
		if(Loader.isModLoaded("ThermalFoundation"))
		{
			new ThermalFoundationModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod Thermal Foundation");
		}
		if(Loader.isModLoaded("ThermalExpansion"))
		{
			new ThermalExpansionModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod Thermal Expansion");
		}
	}
}