package chanceCubes.network;

import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.ItemChanceCube;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketCreativePendant
{
	private int chancevalue;

	public PacketCreativePendant(int chance)
	{
		this.chancevalue = chance;
	}

	public static void encode(PacketCreativePendant msg, PacketBuffer buf)
	{
		buf.writeInt(msg.chancevalue);
	}

	public static PacketCreativePendant decode(PacketBuffer buf)
	{
		return new PacketCreativePendant(buf.readInt());
	}

	public static void handle(PacketCreativePendant msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			Container c;
			try
			{
				c = ctx.get().getSender().openContainer;
			} catch(Exception NullPointerException)
			{
				CCubesCore.logger.log(Level.ERROR, "Chance Cubes has failed to set the chance of a cube due to a packet failure! Please Inform Turkey of this!");
				return;
			}

			if(c instanceof CreativePendantContainer)
			{
				CreativePendantContainer container = (CreativePendantContainer) c;
				ItemStack ccubes = container.getChanceCubesInPendant();
				if(!ccubes.isEmpty() && ccubes.getItem() instanceof ItemChanceCube)
					((ItemChanceCube) ccubes.getItem()).setChance(ccubes, msg.chancevalue);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}