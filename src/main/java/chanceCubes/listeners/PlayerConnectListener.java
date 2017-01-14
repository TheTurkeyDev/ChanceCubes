package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class PlayerConnectListener
{
	boolean hasChecked = false;
	
	@SubscribeEvent
	public void onPlayerLogin(final PlayerLoggedInEvent event)
	{
		if(event.player.worldObj.isRemote)
			return;
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new CustomUserReward(event.player);
			}

		}).start();
		
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedOutEvent event)
	{
		if(event.player.worldObj.isRemote)
			return;
		
		ChanceCubeRegistry.INSTANCE.unregisterReward(CCubesCore.MODID + ":Custom_Reward_For_" + event.player.getCommandSenderEntity().getName());
	}
}