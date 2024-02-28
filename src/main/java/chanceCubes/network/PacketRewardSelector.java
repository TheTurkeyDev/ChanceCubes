package chanceCubes.network;

import chanceCubes.CCubesCore;
import chanceCubes.items.CCubesItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record PacketRewardSelector(String reward) implements CustomPacketPayload
{
	public static final ResourceLocation ID = new ResourceLocation(CCubesCore.MODID, "reward_selector");

	public PacketRewardSelector(FriendlyByteBuf buf)
	{
		this(buf.readUtf());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUtf(reward);
	}

	@Override
	public ResourceLocation id()
	{
		return ID;
	}

	public static void handle(final PacketRewardSelector msg, final PlayPayloadContext context)
	{
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent()) {
						Player player = context.player().get();
						ItemStack stack = player.getInventory().getSelected();
						if(!stack.isEmpty() && (stack.getItem().equals(CCubesItems.REWARD_SELECTOR_PENDANT.get()) || stack.getItem().equals(CCubesItems.SINGLE_USE_REWARD_SELECTOR_PENDANT.get())))
						{
							CompoundTag nbt = stack.getTag();
							if(nbt == null)
								nbt = new CompoundTag();
							nbt.putString("Reward", msg.reward);
							stack.setTag(nbt);
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("chancecubes.networking.reward_selector.failed", e.getMessage()));
					return null;
				});
	}
}