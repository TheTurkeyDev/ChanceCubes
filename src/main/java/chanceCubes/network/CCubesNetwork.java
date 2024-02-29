package chanceCubes.network;

import chanceCubes.CCubesCore;
import io.netty.util.AttributeKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;

public class CCubesNetwork
{
	public static final ResourceLocation HANDSHAKE_NAME = new ResourceLocation(CCubesCore.MODID, "packets");
	public static final AttributeKey<CCubesPacketHandler> CONTEXT = AttributeKey.newInstance(HANDSHAKE_NAME.toString());
	public static final SimpleChannel CHANNEL = ChannelBuilder
			.named(HANDSHAKE_NAME)
			.optional()
			.networkProtocolVersion(0)
			.attribute(CONTEXT, CCubesPacketHandler::new)
			.simpleChannel()

			.messageBuilder(PacketCreativePendant.class, NetworkDirection.PLAY_TO_SERVER)
			.decoder(PacketCreativePendant::decode)
			.encoder(PacketCreativePendant::encode)
			.consumerNetworkThread(CONTEXT, CCubesPacketHandler::handleCreativePendant)
			.add()

			.messageBuilder(PacketRewardSelector.class, NetworkDirection.PLAY_TO_SERVER)
			.decoder(PacketRewardSelector::decode)
			.encoder(PacketRewardSelector::encode)
			.consumerNetworkThread(CONTEXT, CCubesPacketHandler::handleRewardSelector)
			.add()

			.messageBuilder(PacketTriggerD20.class, NetworkDirection.PLAY_TO_CLIENT)
			.decoder(PacketTriggerD20::decode)
			.encoder(PacketTriggerD20::encode)
			.consumerNetworkThread(CONTEXT, CCubesPacketHandler::handleTriggerD20)
			.add()

			.messageBuilder(PacketCubeScan.class, NetworkDirection.PLAY_TO_SERVER)
			.decoder(PacketCubeScan::decode)
			.encoder(PacketCubeScan::encode)
			.consumerNetworkThread(CONTEXT, CCubesPacketHandler::handleCubeScan)
			.add();

	public static void init() {
		for(var channel : new Channel[]{CHANNEL})
			CCubesCore.logger.debug("Registering Network {} v{}", channel.getName(), channel.getProtocolVersion());
	}
}
