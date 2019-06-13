package chanceCubes.hookins;

import java.util.Map;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.hookins.mods.ExtraUtilsModHook;
import chanceCubes.hookins.mods.ThermalExpansionModHook;
import chanceCubes.hookins.mods.ThermalFoundationModHook;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

public class ModHookUtil
{
	public static void loadCustomModRewards()
	{
		if(!CCubesSettings.enableHardCodedRewards.get())
			return;
		if(ModList.get().isLoaded("extrautils2"))
		{
			new ExtraUtilsModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod " + getModNameByID("extrautils2"));
		}
		if(ModList.get().isLoaded("thermalfoundation"))
		{
			new ThermalFoundationModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod " + getModNameByID("thermalfoundation"));
		}
		if(ModList.get().isLoaded("thermalexpansion"))
		{
			new ThermalExpansionModHook();
			CCubesCore.logger.log(Level.INFO, "Loaded custom rewards for the mod " + getModNameByID("thermalexpansion"));
		}
	}

	public static String getModNameByID(String modid)
	{
		ModContainer modCon = ModList.get().getModContainerById(modid).orElse(null);
		if(modCon != null)
			return modCon.getModInfo().getDisplayName();
		return modid;
	}
}
