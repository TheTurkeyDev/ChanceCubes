package chanceCubes.network;

import chanceCubes.CCubesCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PacketRewardSelector(String reward) implements CustomPacketPayload
{
	public static final StreamCodec<ByteBuf, PacketRewardSelector> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, PacketRewardSelector::reward, PacketRewardSelector::new
	);
	public static final Type<PacketRewardSelector> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(CCubesCore.MODID, "reward_selector"));

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
}