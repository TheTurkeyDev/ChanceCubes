package chanceCubes.rewards.profiles.triggerHooks;

import chanceCubes.rewards.profiles.IProfile;
import chanceCubes.rewards.profiles.ProfileManager;
import chanceCubes.rewards.profiles.triggers.GameStageTrigger;
import chanceCubes.rewards.profiles.triggers.ITrigger;
import net.darkhax.gamestages.event.GameStageEvent.Added;
import net.darkhax.gamestages.event.GameStageEvent.Removed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameStageTriggerHooks
{
	@SubscribeEvent
	public void onStageAdd(Added event)
	{
		for(IProfile prof : ProfileManager.getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof GameStageTrigger)
				{
					GameStageTrigger trigger = (GameStageTrigger) module;
					trigger.onTrigger(new String[] { event.getStageName(), "A" });
				}
			}
		}
	}

	@SubscribeEvent
	public void onStageRemove(Removed event)
	{
		for(IProfile prof : ProfileManager.getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof GameStageTrigger)
				{
					GameStageTrigger trigger = (GameStageTrigger) module;
					trigger.onTrigger(new String[] { event.getStageName(), "R" });
				}
			}
		}
	}
}
