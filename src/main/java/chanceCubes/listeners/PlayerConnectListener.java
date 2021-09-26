package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerConnectListener
{
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if(event.getPlayer().level.isClientSide())
			return;

		new Thread(() -> CustomUserReward.getCustomUserReward(event.getPlayer().getUUID())).start();
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		if(event.getPlayer().level.isClientSide())
			return;

		String rewardName = CCubesCore.MODID + ":CR_" + event.getPlayer().getName();
		if(GlobalCCRewardRegistry.DEFAULT.isRewardEnabled(rewardName))
			GlobalCCRewardRegistry.DEFAULT.unregisterReward(rewardName);

		String playerUUID = event.getPlayer().getStringUUID();
		GlobalCCRewardRegistry.DEFAULT.removePlayerRewards(playerUUID);
		GlobalCCRewardRegistry.GIANT.removePlayerRewards(playerUUID);
	}
}