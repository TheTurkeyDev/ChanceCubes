package chanceCubes.network;

import io.netty.buffer.ByteBuf;
import chanceCubes.CCubesCore;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketParticle implements IMessage
{

	public String particle;

	public double x;
	public double y;
	public double z;

	public double vX;
	public double vY;
	public double vZ;

	public PacketParticle()
	{
	}

	public PacketParticle(String particle, double x, double y, double z, double vX, double vY, double vZ)
	{
		this.particle = particle;
		this.x = x;
		this.y = y;
		this.z = z;
		this.vX = vX;
		this.vY = vY;
		this.vZ = vZ;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.particle);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(vX);
		buf.writeDouble(vY);
		buf.writeDouble(vZ);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.particle = ByteBufUtils.readUTF8String(buf);
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.vX = buf.readDouble();
		this.vY = buf.readDouble();
		this.vZ = buf.readDouble();
	}

	public static final class Handler implements IMessageHandler<PacketParticle, IMessage>
	{
		@Override
		public IMessage onMessage(PacketParticle message, MessageContext ctx)
		{
			CCubesCore.proxy.getClientPlayer().worldObj.spawnParticle(message.particle, message.x, message.y, message.z, message.vX, message.vY, message.vZ);

			return null;

		}
	}

}
