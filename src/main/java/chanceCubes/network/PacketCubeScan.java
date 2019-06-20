package chanceCubes.network;

import java.util.function.Supplier;

import chanceCubes.tileentities.TileChanceCube;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketCubeScan
{
	public BlockPos pos;

	public PacketCubeScan(BlockPos pos)
	{
		this.pos = pos;
	}

	public static void encode(PacketCubeScan msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.pos);
	}

	public static PacketCubeScan decode(PacketBuffer buf)
	{
		return new PacketCubeScan(buf.readBlockPos());
	}

	public static void handle(PacketCubeScan msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			TileEntity te = ctx.get().getSender().world.getTileEntity(msg.pos);
			if(te instanceof TileChanceCube)
				((TileChanceCube) te).setScanned(true);
//			else if(te instanceof TileChanceD20)
//				((TileChanceD20) te).setScanned(true);
		});
		ctx.get().setPacketHandled(true);
	}

}