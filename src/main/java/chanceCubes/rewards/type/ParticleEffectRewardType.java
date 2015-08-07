package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ParticleEffectRewardType extends BaseRewardType<String>
{
	public ParticleEffectRewardType(String... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(String effect, World world, int x, int y, int z, EntityPlayer player)
	{
		world.spawnParticle(effect, x, y + 1, z, 0.0D, 0.0D, 0.0D);
	}
}
