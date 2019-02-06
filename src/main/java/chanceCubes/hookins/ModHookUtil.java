package chanceCubes.hookins;

import net.minecraftforge.fml.common.ModContainer;
import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.hookins.mods.*;
import net.minecraftforge.fml.common.Loader;

import java.util.Map;

public class ModHookUtil
{
	public static void loadCustomModRewards()
	{
		if(!CCubesSettings.enableHardCodedRewards)
			return;
		if(Loader.isModLoaded("extrautils2"))
		{
			new ExtraUtilsModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod " + getModNameByID("extrautils2"));
		}
		if(Loader.isModLoaded("thermalfoundation"))
		{
			new ThermalFoundationModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod " + getModNameByID("thermalfoundation"));
		}
		if(Loader.isModLoaded("thermalexpansion"))
		{
			new ThermalExpansionModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod " + getModNameByID("thermalexpansion"));
		}
	}

	public static String getModNameByID(String modid)
	{
		Map<String, ModContainer> indexedModList = Loader.instance().getIndexedModList();
		ModContainer modContainer = indexedModList.get(modid);
		if(modContainer != null)
		{
			return modContainer.getName();
		}
		return modid;
	}
}
