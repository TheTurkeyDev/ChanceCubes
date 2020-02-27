package chanceCubes.profiles.triggerHooks;

import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.triggers.GameStageTrigger;
import chanceCubes.profiles.triggers.ITrigger;
import net.darkhax.gamestages.event.GameStageEvent.Added;
import net.darkhax.gamestages.event.GameStageEvent.Removed;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameStageTriggerHooks
{
	@SubscribeEvent
	public void onStageAdd(Added event)
	{
		PlayerEntity player = event.getEntityPlayer();
		for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player).getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof GameStageTrigger)
				{
					GameStageTrigger trigger = (GameStageTrigger) module;
					trigger.onTrigger(player.getUniqueID().toString(), new String[]{event.getStageName(), "A"});
				}
			}
		}
	}

	@SubscribeEvent
	public void onStageRemove(Removed event)
	{
		PlayerEntity player = event.getEntityPlayer();
		for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player).getAllProfiles())
		{
			for(ITrigger<?> module : prof.getTriggers())
			{
				if(module instanceof GameStageTrigger)
				{
					GameStageTrigger trigger = (GameStageTrigger) module;
					trigger.onTrigger(player.getUniqueID().toString(), new String[]{event.getStageName(), "R"});
				}
			}
		}
	}
}
