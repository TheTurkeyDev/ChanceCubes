package chanceCubes.network;

import chanceCubes.CCubesCore;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class CCubesNetwork
{
	public static void setupPackets(final RegisterPayloadHandlersEvent event)
	{
		final PayloadRegistrar registrar = event.registrar(CCubesCore.MODID);

		registrar.playToClient(PacketTriggerD20.ID, PacketTriggerD20.STREAM_CODEC, CCubesClientPayloadHandler.getInstance()::handleTrigger);

		registrar.playToServer(PacketCreativePendant.ID, PacketCreativePendant.STREAM_CODEC, CCubesServerPayloadHandler.getInstance()::handleCreativePendant);
		registrar.playToServer(PacketRewardSelector.ID, PacketRewardSelector.STREAM_CODEC, CCubesServerPayloadHandler.getInstance()::handleRewardSelector);
		registrar.playToServer(PacketCubeScan.ID, PacketCubeScan.STREAM_CODEC, CCubesServerPayloadHandler.getInstance()::handleCubeScan);
	}
}
