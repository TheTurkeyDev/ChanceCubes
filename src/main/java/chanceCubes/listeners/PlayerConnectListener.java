package chanceCubes.listeners;

import chanceCubes.CCubesCore;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class PlayerConnectListener
{
	@SubscribeEvent
	public void onPlayerLogin(final PlayerLoggedInEvent event)
	{
		if(event.player.world.isRemote)
			return;

		ProfileManager.updateProfilesForWorld(event.player.world.getWorldInfo().getWorldName());

		new Thread(() -> CustomUserReward.getCustomUserReward(event.player.getUniqueID())).start();
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		if(event.player.world.isRemote)
			return;

		ChanceCubeRegistry.INSTANCE.unregisterReward(CCubesCore.MODID + ":CR_" + event.player.getCommandSenderEntity().getName());
	}
}