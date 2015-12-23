package chanceCubes.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.ItemChanceCube;

public class PacketCreativePendant implements IMessage
{

	private String playerName;
	private int chancevalue;

	public PacketCreativePendant()
	{
	}

	public PacketCreativePendant(String player, int chance)
	{
		this.playerName = player;
		this.chancevalue = chance;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, playerName);
		buf.writeInt(chancevalue);

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.playerName = ByteBufUtils.readUTF8String(buf);
		this.chancevalue = buf.readInt();

	}

	public static final class Handler implements IMessageHandler<PacketCreativePendant, IMessage>
	{
		@Override
		public IMessage onMessage(PacketCreativePendant message, MessageContext ctx)
		{
			Container c;
			try
			{
				c = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(message.playerName).openContainer;
			} catch(Exception NullPointerException)
			{
				CCubesCore.logger.log(Level.ERROR, "Chance Cubes has failed to set the chance of a cube due to a packet failure! Please Inform Turkey of this!");
				return null;
			}

			if(c instanceof CreativePendantContainer)
			{
				CreativePendantContainer container = (CreativePendantContainer) c;
				ItemStack ccubes = container.getChanceCubesInPendant();
				if(ccubes != null)
				{
					if(ccubes.getItem() instanceof ItemChanceCube)
					{
						((ItemChanceCube) ccubes.getItem()).setChance(ccubes, message.chancevalue);
					}
				}
			}
			return null;
		}
	}

}
