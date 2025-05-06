package chanceCubes.network;

import chanceCubes.CCubesCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PacketCubeScan(BlockPos pos) implements CustomPacketPayload
{
	public static final StreamCodec<ByteBuf, PacketCubeScan> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PacketCubeScan::pos, PacketCubeScan::new
	);
	public static final Type<PacketCubeScan> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(CCubesCore.MODID, "cube_scan"));

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
}