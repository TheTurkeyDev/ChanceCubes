package chanceCubes.rewards.type;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ParticleEffectRewardType extends BaseRewardType<String>
{

	public ParticleEffectRewardType(String... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(String command, World world, int x, int y, int z, EntityPlayer player)
	{
		Random r = new Random();
		if (!world.isRemote)
		{
			for(String effect: this.rewards)
				world.spawnParticle(effect, x, y, z, r.nextInt(3) - 2, r.nextInt(3) - 2, r.nextInt(3) - 2);
		}
	}
}
