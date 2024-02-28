package chanceCubes.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class PacketCubeScan
{
	public final BlockPos pos;

	public PacketCubeScan(BlockPos pos)
	{
		this.pos = pos;
	}

	public static void encode(PacketCubeScan msg, FriendlyByteBuf buf)
	{
		buf.writeBlockPos(msg.pos);
	}

	public static PacketCubeScan decode(FriendlyByteBuf buf)
	{
		return new PacketCubeScan(buf.readBlockPos());
	}

}