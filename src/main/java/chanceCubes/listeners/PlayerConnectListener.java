package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.profiles.GlobalProfileManager;
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
		if(event.getPlayer().world.isRemote)
			return;

		GlobalProfileManager.loadPlayerProfile(event.getPlayer().getUniqueID().toString());
		new Thread(() -> CustomUserReward.getCustomUserReward(event.getPlayer().getUniqueID())).start();
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		if(event.getPlayer().world.isRemote)
			return;

		String rewardName = CCubesCore.MODID + ":CR_" + event.getPlayer().getName();
		if(GlobalCCRewardRegistry.DEFAULT.isRewardEnabled(rewardName))
			GlobalCCRewardRegistry.DEFAULT.unregisterReward(rewardName);

		String playerUUID = event.getPlayer().getUniqueID().toString();
		GlobalCCRewardRegistry.DEFAULT.removePlayerRewards(playerUUID);
		GlobalCCRewardRegistry.GIANT.removePlayerRewards(playerUUID);
		GlobalProfileManager.removePlayerProfile(playerUUID);
	}
}