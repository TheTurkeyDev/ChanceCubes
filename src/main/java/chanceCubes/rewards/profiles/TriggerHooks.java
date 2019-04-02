package chanceCubes.rewards.profiles;

import chanceCubes.rewards.profiles.triggers.DifficultyTrigger;
import chanceCubes.rewards.profiles.triggers.ITrigger;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TriggerHooks
{
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
}