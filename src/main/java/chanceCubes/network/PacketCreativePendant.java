package chanceCubes.network;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.ItemChanceCube;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import org.apache.logging.log4j.Level;

import java.util.function.Supplier;

public class PacketCreativePendant
{
	private final int chanceValue;

	public PacketCreativePendant(int chance)
	{
		this.chanceValue = chance;
	}

	public static void encode(PacketCreativePendant msg, FriendlyByteBuf buf)
	{
		buf.writeInt(msg.chanceValue);
	}

	public static PacketCreativePendant decode(FriendlyByteBuf buf)
	{
		return new PacketCreativePendant(buf.readInt());
	}

	public static void handle(PacketCreativePendant msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			Player player = ctx.get().getSender();
			if(player == null)
				return;

			try
			{
				AbstractContainerMenu c = player.containerMenu;
				if(c instanceof CreativePendantContainer container)
				{
					ItemStack cCubes = container.getChanceCubesInPendant();
					if(!cCubes.isEmpty() && cCubes.getItem() instanceof ItemChanceCube)
						((ItemChanceCube) cCubes.getItem()).setChance(cCubes, msg.chanceValue);
				}
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, "Chance Cubes has failed to set the chance of a cube due to a packet failure! Please Inform Turkey of this!");
			}
		});
		ctx.get().setPacketHandled(true);
	}

}