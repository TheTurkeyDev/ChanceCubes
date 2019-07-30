package chanceCubes.network;

import java.util.function.Supplier;

import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTriggerD20
{

	public BlockPos pos;

	public PacketTriggerD20(BlockPos pos)
	{
		this.pos = pos;
	}

	public static void encode(PacketTriggerD20 msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.pos);
	}

	public static PacketTriggerD20 decode(PacketBuffer buf)
	{
		return new PacketTriggerD20(buf.readBlockPos());
	}

	public static void handle(PacketTriggerD20 msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			TileEntity ico;

			if((ico = Minecraft.getInstance().player.world.getTileEntity(msg.pos)) != null)
				if(ico instanceof TileChanceD20)
					((TileChanceD20) ico).startBreaking(Minecraft.getInstance().player);
		});
		ctx.get().setPacketHandled(true);
	}
}
