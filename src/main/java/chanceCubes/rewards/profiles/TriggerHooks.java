package chanceCubes.rewards.profiles;

import chanceCubes.rewards.profiles.modules.DiffucultyTrigger;
import chanceCubes.rewards.profiles.modules.ITrigger;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerHooks
{
	@SubscribeEvent
	public void onDifficultyChange(DifficultyChangeEvent event)
	{
		for(IProfile prof : ProfileManager.getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof DiffucultyTrigger)
				{
					DiffucultyTrigger trigger = (DiffucultyTrigger) module;
					trigger.onTrigger(new EnumDifficulty[] { event.getDifficulty(), event.getOldDifficulty() });
				}
			}
		}

	}
}
