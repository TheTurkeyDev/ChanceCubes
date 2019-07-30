package chanceCubes.profiles.triggerHooks;

import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.profiles.triggers.GameStageTrigger;
import chanceCubes.profiles.triggers.ITrigger;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameStageTriggerHooks
{
	@SubscribeEvent
	public void onStageAdd(GameStageEvent.Added event)
	{
		for(IProfile prof : ProfileManager.getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof GameStageTrigger)
				{
					GameStageTrigger trigger = (GameStageTrigger) module;
					trigger.onTrigger(new String[]{event.getStageName(), "A"});
				}
			}
		}
	}

	@SubscribeEvent
	public void onStageRemove(GameStageEvent.Removed event)
	{
		for(IProfile prof : ProfileManager.getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof GameStageTrigger)
				{
					GameStageTrigger trigger = (GameStageTrigger) module;
					trigger.onTrigger(new String[]{event.getStageName(), "R"});
				}
			}
		}
	}
}
