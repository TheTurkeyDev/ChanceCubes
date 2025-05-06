package chanceCubes.network;

import chanceCubes.CCubesCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PacketTriggerD20(BlockPos pos) implements CustomPacketPayload
{
	public static final StreamCodec<ByteBuf, PacketTriggerD20> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PacketTriggerD20::pos, PacketTriggerD20::new
	);
	public static final Type<PacketTriggerD20> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(CCubesCore.MODID, "trigger_d20"));

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
}