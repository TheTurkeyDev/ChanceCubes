package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ParticlePart;
import chanceCubes.util.RewardsUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class ParticleEffectRewardType extends BaseRewardType<ParticlePart>
{
	public ParticleEffectRewardType(ParticlePart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(ParticlePart part, ServerLevel level, int x, int y, int z, Player player)
	{
		ParticleOptions particle = (ParticleOptions) RewardsUtil.getParticleSafe(new ResourceLocation(part.getParticleName()));
		level.sendParticles(particle, x + Math.random(), y + Math.random(), z + Math.random(), 1, 0d, 1d, 0.0D, 0.15F);
	}
}
