package chanceCubes.network;

import chanceCubes.CCubesCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PacketCreativePendant(int chanceValue) implements CustomPacketPayload
{
	public static final StreamCodec<ByteBuf, PacketCreativePendant> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, PacketCreativePendant::chanceValue, PacketCreativePendant::new
	);
	public static final Type<PacketCreativePendant> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(CCubesCore.MODID, "creative_pendant"));

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
}