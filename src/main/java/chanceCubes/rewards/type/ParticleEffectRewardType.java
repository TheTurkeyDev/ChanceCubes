package chanceCubes.rewards.type;

import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketParticle;
import chanceCubes.rewards.rewardparts.ParticlePart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

public class ParticleEffectRewardType extends BaseRewardType<ParticlePart>
{
	public ParticleEffectRewardType(ParticlePart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(ParticlePart part, World world, int x, int y, int z, EntityPlayer player)
	{
		CCubesPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> new TargetPoint(x, y, z, 50, world.getDimension().getType())), new PacketParticle(part, x + Math.random(), y + Math.random(), z + Math.random(), 0, 0, 0));
	}
}
