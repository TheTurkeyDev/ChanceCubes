package chanceCubes.network;

import net.minecraft.network.FriendlyByteBuf;

public class PacketRewardSelector
{
	private final String reward;

	public PacketRewardSelector(String reward)
	{
		this.reward = reward;
	}

	public static void encode(PacketRewardSelector msg, FriendlyByteBuf buf)
	{
		buf.writeUtf(msg.reward);
	}

	public static PacketRewardSelector decode(FriendlyByteBuf buf)
	{
		return new PacketRewardSelector(buf.readUtf());
	}

	public String getReward()
	{
		return reward;
	}
}