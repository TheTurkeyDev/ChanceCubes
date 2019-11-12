package chanceCubes.profiles.triggerHooks;

import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.profiles.IProfile;
import chanceCubes.profiles.triggers.GameStageTrigger;
import chanceCubes.profiles.triggers.ITrigger;
import net.darkhax.gamestages.event.GameStageEvent.Added;
import net.darkhax.gamestages.event.GameStageEvent.Removed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameStageTriggerHooks
{
	@SubscribeEvent
	public void onStageAdd(Added event)
	{
		EntityPlayer player = event.getEntityPlayer();
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
		EntityPlayer player = event.getEntityPlayer();
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
