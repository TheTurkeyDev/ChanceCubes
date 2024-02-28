package chanceCubes.network;

import net.minecraft.network.FriendlyByteBuf;

public class PacketCreativePendant
{
	private final int chanceValue;

	public PacketCreativePendant(int chance)
	{
		this.chanceValue = chance;
	}

	public static void encode(PacketCreativePendant msg, FriendlyByteBuf buf)
	{
		buf.writeInt(msg.chanceValue);
	}

	public static PacketCreativePendant decode(FriendlyByteBuf buf)
	{
		return new PacketCreativePendant(buf.readInt());
	}

	public int getChanceValue()
	{
		return this.chanceValue;
	}
}