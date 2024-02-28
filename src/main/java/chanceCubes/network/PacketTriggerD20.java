package chanceCubes.network;

import chanceCubes.CCubesCore;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record PacketTriggerD20(BlockPos pos) implements CustomPacketPayload
{
	public static final ResourceLocation ID = new ResourceLocation(CCubesCore.MODID, "trigger_d20");

	public PacketTriggerD20(FriendlyByteBuf buf)
	{
		this(buf.readBlockPos());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(pos);
	}

	@Override
	public ResourceLocation id()
	{
		return ID;
	}

	public static void handle(final PacketTriggerD20 msg, final PlayPayloadContext context)
	{
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent()) {
						Player player = context.player().get();
						Level level = player.level();
						BlockEntity ico;

//						if ((ico = level.getBlockEntity(msg.pos)) != null)
//							if (ico instanceof TileChanceD20 && level.players().size() > 0)
//								((TileChanceD20) ico).startBreaking(level.players().get(0));
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("chancecubes.networking.trigger_d20.failed", e.getMessage()));
					return null;
				});
	}
}