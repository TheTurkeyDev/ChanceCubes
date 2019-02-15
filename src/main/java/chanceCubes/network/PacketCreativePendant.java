package chanceCubes.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.network.ForgeMessage;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketCreativePendant extends ForgeMessage
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

	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, playerName);
		buf.writeInt(chancevalue);

	}

	public void fromBytes(ByteBuf buf)
	{
		this.playerName = ByteBufUtils.readUTF8String(buf);
		this.chancevalue = buf.readInt();

	}

//	public static final class Handler implements IMessageHandler<PacketCreativePendant, IMessage>
//	{
//		@Override
//		public IMessage onMessage(PacketCreativePendant message, MessageContext ctx)
//		{
//			Container c;
//			try
//			{
//				c = ctx.getServerHandler().player.openContainer;
//			} catch(Exception NullPointerException)
//			{
//				CCubesCore.logger.log(Level.ERROR, "Chance Cubes has failed to set the chance of a cube due to a packet failure! Please Inform Turkey of this!");
//				return null;
//			}
//
//			if(c instanceof CreativePendantContainer)
//			{
//				CreativePendantContainer container = (CreativePendantContainer) c;
//				ItemStack ccubes = container.getChanceCubesInPendant();
//				if(!ccubes.isEmpty() && ccubes.getItem() instanceof ItemChanceCube)
//					((ItemChanceCube) ccubes.getItem()).setChance(ccubes, message.chancevalue);
//			}
//			return null;
//		}
//	}

}
