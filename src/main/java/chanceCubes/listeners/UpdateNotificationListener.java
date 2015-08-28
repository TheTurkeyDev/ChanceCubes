package chanceCubes.listeners;

import chanceCubes.rewards.CustomUserReward;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class UpdateNotificationListener
{
	boolean hasChecked = false;
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if(event.player.worldObj.isRemote)
			return;
		
		new CustomUserReward(event.player);
	}
}