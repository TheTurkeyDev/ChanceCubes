package chanceCubes.network;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.rewardparts.ParticlePart;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.network.ForgeMessage;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketParticle extends ForgeMessage
{

	public int particleID = -1;
	public String particleName = "";

	public double x;
	public double y;
	public double z;

	public double vX;
	public double vY;
	public double vZ;

	public PacketParticle()
	{
	}

	public PacketParticle(ParticlePart part, double x, double y, double z, double vX, double vY, double vZ)
	{
		this.particleName = part.getParticleName();
		this.particleID = part.getParticleID();
		this.x = x;
		this.y = y;
		this.z = z;
		this.vX = vX;
		this.vY = vY;
		this.vZ = vZ;
	}

	public PacketParticle(String particle, double x, double y, double z, double vX, double vY, double vZ)
	{
		this.particleName = particle;
		this.x = x;
		this.y = y;
		this.z = z;
		this.vX = vX;
		this.vY = vY;
		this.vZ = vZ;
	}

	public PacketParticle(int particle, double x, double y, double z, double vX, double vY, double vZ)
	{
		this.particleID = particle;
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
		buf.writeInt(this.particleID);
		ByteBufUtils.writeUTF8String(buf, this.particleName);
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
		this.particleID = buf.readInt();
		this.particleName = ByteBufUtils.readUTF8String(buf);
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.vX = buf.readDouble();
		this.vY = buf.readDouble();
		this.vZ = buf.readDouble();
	}

//	public static final class Handler implements IMessageHandler<PacketParticle, IMessage>
//	{
//		@Override
//		public IMessage onMessage(PacketParticle message, MessageContext ctx)
//		{
//			EnumParticleTypes particle = message.particleID == -1 ? EnumParticleTypes.getByName(message.particleName) : EnumParticleTypes.getParticleFromId(message.particleID);
//			CCubesCore.proxy.getClientPlayer().world.spawnParticle(particle, message.x, message.y, message.z, message.vX, message.vY, message.vZ);
//			return null;
//		}
//	}
}