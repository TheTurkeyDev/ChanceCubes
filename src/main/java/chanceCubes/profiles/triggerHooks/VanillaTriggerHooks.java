package chanceCubes.profiles.triggerHooks;

import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.profiles.triggers.DifficultyTrigger;
import chanceCubes.profiles.triggers.DimensionChangeTrigger;
import chanceCubes.profiles.triggers.ITrigger;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class VanillaTriggerHooks
{
	//TODO: Crafting trigger
	
	@SubscribeEvent
	public void onDifficultyChange(DifficultyChangeEvent event)
	{
		for(IProfile prof : ProfileManager.getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof DifficultyTrigger)
				{
					DifficultyTrigger trigger = (DifficultyTrigger) module;
					trigger.onTrigger(new EnumDifficulty[] { event.getDifficulty(), event.getOldDifficulty() });
				}
			}
		}
	}

	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent event)
	{
		for(IProfile prof : ProfileManager.getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof DimensionChangeTrigger)
				{
					DimensionChangeTrigger trigger = (DimensionChangeTrigger) module;
					trigger.onTrigger(new Integer[] { event.toDim, event.fromDim });
				}
			}
		}
	}
}
