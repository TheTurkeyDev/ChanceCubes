package chanceCubes.network;

import chanceCubes.CCubesCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CCubesPacketHandler
{
	private static int id = 0;
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(CCubesCore.MODID, "packets")).clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true).networkProtocolVersion(() -> NetworkConstants.NETVERSION).simpleChannel();


	public static void init()
	{
		CHANNEL.registerMessage(id++, PacketCreativePendant.class, PacketCreativePendant::encode, PacketCreativePendant::decode, PacketCreativePendant::handle);
		CHANNEL.registerMessage(id++, PacketRewardSelector.class, PacketRewardSelector::encode, PacketRewardSelector::decode, PacketRewardSelector::handle);
		CHANNEL.registerMessage(id++, PacketTriggerD20.class, PacketTriggerD20::encode, PacketTriggerD20::decode, PacketTriggerD20::handle);
		CHANNEL.registerMessage(id++, PacketCubeScan.class, PacketCubeScan::encode, PacketCubeScan::decode, PacketCubeScan::handle);
	}

	public static void sendToPlayer(Object msg, ServerPlayer player)
	{
		CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}
}
