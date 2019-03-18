package chanceCubes.rewards.rewardtype;

import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketParticle;
import chanceCubes.rewards.rewardparts.ParticlePart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ParticleEffectRewardType extends BaseRewardType<ParticlePart>
{
	public ParticleEffectRewardType(ParticlePart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(ParticlePart part, World world, int x, int y, int z, EntityPlayer player)
	{
		CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketParticle(part, x + Math.random(), y + Math.random(), z + Math.random(), 0, 0, 0), new TargetPoint(world.provider.getDimension(), x, y, z, 50));
	}
}
