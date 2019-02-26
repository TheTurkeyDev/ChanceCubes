package chanceCubes.network;

import java.util.function.Supplier;

import chanceCubes.rewards.rewardparts.ParticlePart;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketParticle
{
	public String particleName = "";

	public double x;
	public double y;
	public double z;

	public double vX;
	public double vY;
	public double vZ;

	public PacketParticle(ParticlePart part, double x, double y, double z, double vX, double vY, double vZ)
	{
		this.particleName = part.getParticleName();
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

	public static void encode(PacketParticle msg, PacketBuffer buf)
	{
		buf.writeString(msg.particleName);
		buf.writeDouble(msg.x);
		buf.writeDouble(msg.y);
		buf.writeDouble(msg.z);
		buf.writeDouble(msg.vX);
		buf.writeDouble(msg.vY);
		buf.writeDouble(msg.vZ);
	}

	public static PacketParticle decode(PacketBuffer buf)
	{
		return new PacketParticle(buf.readString(32767), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public static void handle(PacketParticle msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			Minecraft.getInstance().player.world.spawnParticle(getRegisteredParticleTypes(msg.particleName), msg.x, msg.y, msg.z, msg.vX, msg.vY, msg.vZ);
		});
		ctx.get().setPacketHandled(true);
	}

	//TODO: Remove when ForgeRegistries adds particles
	@SuppressWarnings("unchecked")
	private static <T extends ParticleType<?>> T getRegisteredParticleTypes(String p_197589_0_)
	{
		T t = (T) IRegistry.field_212632_u.func_212608_b(new ResourceLocation(p_197589_0_));
		if(t == null)
		{
			throw new IllegalStateException("Invalid or unknown particle type: " + p_197589_0_);
		}
		else
		{
			return t;
		}
	}

}