package chanceCubes.network;

import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

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
			Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
			if(world.isPresent())
			{
				TileEntity ico;

				if((ico = world.get().getTileEntity(msg.pos)) != null)
					if(ico instanceof TileChanceD20 && world.get().getPlayers().size() > 0)
						((TileChanceD20) ico).startBreaking(world.get().getPlayers().get(0));
			}
		});
		ctx.get().setPacketHandled(true);
	}
}