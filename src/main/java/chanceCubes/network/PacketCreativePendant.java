package chanceCubes.network;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.ItemChanceCube;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketCreativePendant implements IMessage
{

	private String playerName;
	private int chancevalue;

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
			Container c = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(message.playerName).openContainer;

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
