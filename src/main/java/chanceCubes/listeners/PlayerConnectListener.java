package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class PlayerConnectListener
{
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if(event.getEntity().level().isClientSide())
			return;

		new Thread(() -> CustomUserReward.getCustomUserReward(event.getEntity().getUUID())).start();
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		if(event.getEntity().level().isClientSide())
			return;

		String rewardName = CCubesCore.MODID + ":CR_" + event.getEntity().getName();
		if(GlobalCCRewardRegistry.DEFAULT.isRewardEnabled(rewardName))
			GlobalCCRewardRegistry.DEFAULT.unregisterReward(rewardName);

		String playerUUID = event.getEntity().getStringUUID();
		GlobalCCRewardRegistry.DEFAULT.removePlayerRewards(playerUUID);
		GlobalCCRewardRegistry.GIANT.removePlayerRewards(playerUUID);
	}
}