package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ParticlePart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleEffectRewardType extends BaseRewardType<ParticlePart>
{
	public ParticleEffectRewardType(ParticlePart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(ParticlePart part, World world, int x, int y, int z, PlayerEntity player)
	{
		ParticleType<?> particle = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(part.getParticleName()));
		((ServerWorld) world).spawnParticle((IParticleData) particle, x + Math.random(), y + Math.random(), z + Math.random(), 1, 0d, 1d, 0d, 0);
	}
}
