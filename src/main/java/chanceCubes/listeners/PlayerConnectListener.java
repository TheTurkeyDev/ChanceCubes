package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;

public class PlayerConnectListener
{
	@SubscribeEvent
	public void onPlayerLogin(final PlayerLoggedInEvent event)
	{
		if(event.getPlayer().world.isRemote)
			return;

		if(event.getEntity().world.getWorldInfo().isHardcore())
			ProfileManager.enableProfile("hardcore");

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

		if(event.getEntity().world.getWorldInfo().isHardcore())
			ProfileManager.disableProfile("hardcore");

		ChanceCubeRegistry.INSTANCE.unregisterReward(CCubesCore.MODID + ":CR_" + event.getPlayer().getCommandSource().getName());
	}
}