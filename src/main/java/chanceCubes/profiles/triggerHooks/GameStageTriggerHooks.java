package chanceCubes.profiles.triggerHooks;

public class GameStageTriggerHooks
{
//	@SubscribeEvent
//	public void onStageAdd(Added event)
//	{
//		PlayerEntity player = event.getEntityPlayer();
//		for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player).getAllProfiles())
//		{
//			for(ITrigger<?> module : prof.getTriggers())
//			{
//				if(module instanceof GameStageTrigger)
//				{
//					GameStageTrigger trigger = (GameStageTrigger) module;
//					trigger.onTrigger(player.getUniqueID().toString(), new String[]{event.getStageName(), "A"});
//				}
//			}
//		}
//	}
//
//	@SubscribeEvent
//	public void onStageRemove(Removed event)
//	{
//		PlayerEntity player = event.getEntityPlayer();
//		for(IProfile prof : GlobalProfileManager.getPlayerProfileManager(player).getAllProfiles())
//		{
//			for(ITrigger<?> module : prof.getTriggers())
//			{
//				if(module instanceof GameStageTrigger)
//				{
//					GameStageTrigger trigger = (GameStageTrigger) module;
//					trigger.onTrigger(player.getUniqueID().toString(), new String[]{event.getStageName(), "R"});
//				}
//			}
//		}
//	}
}
