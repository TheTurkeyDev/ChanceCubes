package chanceCubes.profiles.triggerHooks;

import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.profiles.triggers.DifficultyTrigger;
import chanceCubes.profiles.triggers.DimensionChangeTrigger;
import chanceCubes.profiles.triggers.ITrigger;
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
					trigger.onTrigger(new Difficulty[] { event.getDifficulty(), event.getOldDifficulty() });
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
					trigger.onTrigger(new Integer[] { event.getTo().getId(), event.getFrom().getId() });
				}
			}
		}
	}
}
