package chanceCubes.network;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.ItemChanceCube;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record PacketCreativePendant(int chanceValue) implements CustomPacketPayload
{
	public static final ResourceLocation ID = new ResourceLocation(CCubesCore.MODID, "creative_pendant");

	public PacketCreativePendant(FriendlyByteBuf buf)
	{
		this(buf.readInt());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(chanceValue);
	}

	@Override
	public ResourceLocation id()
	{
		return ID;
	}

	public static void handle(final PacketCreativePendant msg, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent()) {
						Player player = context.player().get();
						if(player == null)
							return;
						try
						{
							AbstractContainerMenu c = player.containerMenu;
							if(c instanceof CreativePendantContainer container)
							{
								ItemStack cCubes = container.getChanceCubesInPendant();
								if(!cCubes.isEmpty() && cCubes.getItem() instanceof ItemChanceCube)
									((ItemChanceCube) cCubes.getItem()).setChance(cCubes, msg.chanceValue());
							}
						} catch(Exception e)
						{
							CCubesCore.logger.log(org.apache.logging.log4j.Level.ERROR, "Chance Cubes has failed to set the chance of a cube due to a packet failure! Please Inform Turkey of this!");
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("chancecubes.networking.creative_pendant.failed", e.getMessage()));
					return null;
				});
	}

}