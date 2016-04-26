package chanceCubes.network;

import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class PacketCubeScan implements IMessage
{
	public int x;
	public int y;
	public int z;
	
	public PacketCubeScan()
	{
	}

	public PacketCubeScan(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();

	}

	public static final class Handler implements IMessageHandler<PacketCubeScan, IMessage>
	{
		@Override
		public IMessage onMessage(PacketCubeScan message, MessageContext ctx)
		{
			TileEntity te = MinecraftServer.getServer().getEntityWorld().getTileEntity(message.x, message.y, message.z);
			if(te instanceof TileChanceCube)
				((TileChanceCube)te).setScanned(true);
			else if(te instanceof TileChanceD20)
				((TileChanceD20)te).setScanned(true);
			
			return null;
		}
	}

}
