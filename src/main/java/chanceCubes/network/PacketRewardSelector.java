package chanceCubes.network;

import chanceCubes.items.CCubesItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

	public static void handle(PacketRewardSelector msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ItemStack stack = ctx.get().getSender().getInventory().getSelected();
			if(!stack.isEmpty() && (stack.getItem().equals(CCubesItems.REWARD_SELECTOR_PENDANT.get()) || stack.getItem().equals(CCubesItems.SINGLE_USE_REWARD_SELECTOR_PENDANT.get())))
			{
				CompoundTag nbt = stack.getTag();
				if(nbt == null)
					nbt = new CompoundTag();
				nbt.putString("Reward", msg.reward);
				stack.setTag(nbt);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}