package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class PlayerConnectListener
{
	boolean hasChecked = false;
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if(event.player.worldObj.isRemote)
			return;
		
		new CustomUserReward(event.player);
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedOutEvent event)
	{
		if(event.player.worldObj.isRemote)
			return;
		
		ChanceCubeRegistry.INSTANCE.unregisterReward(CCubesCore.MODID + ":Custom_Reward_For_" + event.player.getCommandSenderName());
	}
}