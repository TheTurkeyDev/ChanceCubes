package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.EffectPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class EffectRewardType extends BaseRewardType<EffectPart>
{

	public EffectRewardType(EffectPart... effects)
	{
		super(effects);
	}

	@Override
	protected void trigger(EffectPart part, ServerWorld world, int x, int y, int z, PlayerEntity player)
	{
		int radius = part.getRadius().getIntValue();

		for(int j2 = 0; j2 < world.getPlayers().size(); ++j2)
		{
			PlayerEntity tempPlayer = world.getPlayers().get(j2);
			double distTo = tempPlayer.getDistanceSq(x, y, z);

			if(distTo < radius)
				tempPlayer.addPotionEffect(part.getEffect());
		}
	}

}
