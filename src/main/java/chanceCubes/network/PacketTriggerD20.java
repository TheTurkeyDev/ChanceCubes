package chanceCubes.network;

import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class PacketTriggerD20
{
	public BlockPos pos;

	public PacketTriggerD20(BlockPos pos)
	{
		this.pos = pos;
	}

	public static void encode(PacketTriggerD20 msg, FriendlyByteBuf buf)
	{
		buf.writeBlockPos(msg.pos);
	}

	public static PacketTriggerD20 decode(FriendlyByteBuf buf)
	{
		return new PacketTriggerD20(buf.readBlockPos());
	}

	public static void handle(PacketTriggerD20 msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			Optional<Level> levelOpt = LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
			if(levelOpt.isPresent())
			{
				BlockEntity ico;

				Level level = levelOpt.get();
				if((ico = level.getBlockEntity(msg.pos)) != null)
					if(ico instanceof TileChanceD20 && level.players().size() > 0)
						((TileChanceD20) ico).startBreaking(level.players().get(0));
			}
		});
		ctx.get().setPacketHandled(true);
	}
}