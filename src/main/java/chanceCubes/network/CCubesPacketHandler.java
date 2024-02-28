package chanceCubes.network;

import chanceCubes.CCubesCore;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class CCubesPacketHandler
{
//	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(CCubesCore.MODID, "packets")).clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true).networkProtocolVersion(() -> NetworkConstants.NETVERSION).simpleChannel();

	public static void setupPackets(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(CCubesCore.MODID);

		registrar.play(PacketTriggerD20.ID, PacketTriggerD20::new, handler -> handler
				.client(PacketTriggerD20::handle));

		registrar.play(PacketCreativePendant.ID, PacketCreativePendant::new, handler -> handler
				.server(PacketCreativePendant::handle));
		registrar.play(PacketRewardSelector.ID, PacketRewardSelector::new, handler -> handler
				.server(PacketRewardSelector::handle));
		registrar.play(PacketCubeScan.ID, PacketCubeScan::new, handler -> handler
				.server(PacketCubeScan::handle));
	}
}
