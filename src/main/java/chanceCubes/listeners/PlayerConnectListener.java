package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class PlayerConnectListener
{
	@SubscribeEvent
	public void onPlayerLogin(final PlayerLoggedInEvent event)
	{
		if(event.getPlayer().world.isRemote)
			return;

		new Thread(() ->
		{
			CustomUserReward.getCustomUserReward(event.getPlayer().getUniqueID());
		}).start();
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		if(event.getPlayer().world.isRemote)
			return;

		ChanceCubeRegistry.INSTANCE.unregisterReward(CCubesCore.MODID + ":CR_" + event.getPlayer().getCommandSource().getName());
	}
}