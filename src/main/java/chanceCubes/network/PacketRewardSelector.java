package chanceCubes.network;

import java.util.function.Supplier;

import chanceCubes.items.CCubesItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRewardSelector
{
	private String reward;

	public PacketRewardSelector(String reward)
	{
		this.reward = reward;
	}

	public static void encode(PacketRewardSelector msg, PacketBuffer buf)
	{
		buf.writeString(msg.reward);
	}

	public static PacketRewardSelector decode(PacketBuffer buf)
	{
		return new PacketRewardSelector(buf.readString(32767));
	}

	public static void handle(PacketRewardSelector msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			ItemStack stack = ctx.get().getSender().inventory.getCurrentItem();
			if(!stack.isEmpty() && (stack.getItem().equals(CCubesItems.rewardSelectorPendant) || stack.getItem().equals(CCubesItems.singleUseRewardSelectorPendant)))
			{
				CompoundNBT nbt = stack.getTag();
				if(nbt == null)
					nbt = new CompoundNBT();
				nbt.putString("Reward", msg.reward);
				stack.setTag(nbt);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
