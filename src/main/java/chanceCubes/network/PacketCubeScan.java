package chanceCubes.network;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileChanceCube;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record PacketCubeScan(BlockPos pos) implements CustomPacketPayload
{
	public static final ResourceLocation ID = new ResourceLocation(CCubesCore.MODID, "cube_scan");

	public PacketCubeScan(FriendlyByteBuf buf)
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

	public static void handle(final PacketCubeScan msg, final PlayPayloadContext context)
	{
		context.workHandler().submitAsync(() -> {
					if (context.player().isPresent()) {
						Player player = context.player().get();
						BlockEntity te = player.level().getBlockEntity(msg.pos);
						if(te instanceof TileChanceCube)
							((TileChanceCube) te).setScanned(true);
//						else if (te instanceof TileChanceD20)
//							((TileChanceD20) te).setScanned(true);
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("chancecubes.networking.cube_scan.failed", msg.pos, e.getMessage()));
					return null;
				});
	}

}