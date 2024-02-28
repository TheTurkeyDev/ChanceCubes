package chanceCubes.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class PacketTriggerD20
{
	public final BlockPos pos;

	public PacketTriggerD20(BlockPos pos)
	{
		this.pos = pos;
	}

	public static void encode(PacketTriggerD20 msg, FriendlyByteBuf buf)
	{
		buf.writeBlockPos(msg.pos);
	}

	public static PacketTriggerD20 decode(FriendlyByteBuf buf)
	{
		return new PacketTriggerD20(buf.readBlockPos());
	}


}