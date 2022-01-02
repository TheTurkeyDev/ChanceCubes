package chanceCubes.network;

import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCubeScan
{
	public BlockPos pos;

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

	public static void handle(PacketCubeScan msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			BlockEntity te = ctx.get().getSender().level.getBlockEntity(msg.pos);
			if(te instanceof TileChanceCube)
				((TileChanceCube) te).setScanned(true);
			else if(te instanceof TileChanceD20)
				((TileChanceD20) te).setScanned(true);
		});
		ctx.get().setPacketHandled(true);
	}

}