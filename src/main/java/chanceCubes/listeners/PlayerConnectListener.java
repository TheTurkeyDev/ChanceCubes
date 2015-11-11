package chanceCubes.listeners;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.CustomUserReward;

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
		
		ChanceCubeRegistry.INSTANCE.unregisterReward(CCubesCore.MODID + ":Custom_Reward_For_" + event.player.getCommandSenderEntity().getName());
	}
}