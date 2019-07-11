package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.ParticlePart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ParticleEffectRewardType extends BaseRewardType<ParticlePart>
{
	public ParticleEffectRewardType(ParticlePart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(ParticlePart part, World world, int x, int y, int z, EntityPlayer player)
	{
		ParticleType<? extends IParticleData> particle = IRegistry.field_212632_u.func_212608_b(new ResourceLocation(part.getParticleName()));
		((WorldServer)world).spawnParticle((IParticleData) particle, x + Math.random(), y + Math.random(), z + Math.random(), 1, 0d, 1d, 0d, 0);
	}
}
