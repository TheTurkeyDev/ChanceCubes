package chanceCubes.rewards.type;

import chanceCubes.rewards.rewardparts.EffectPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EffectRewardType extends BaseRewardType<EffectPart>
{

	public EffectRewardType(EffectPart... effects)
	{
		super(effects);
	}

	@Override
	protected void trigger(EffectPart part, World world, int x, int y, int z, EntityPlayer player)
	{
		int radius = part.getRadius().getIntValue();

		for(int j2 = 0; j2 < world.playerEntities.size(); ++j2)
		{
			EntityPlayer tempPlayer = world.playerEntities.get(j2);
			double distTo = tempPlayer.getDistance(x, y, z);

			if(distTo < radius)
				tempPlayer.addPotionEffect(part.getEffect());
		}
	}

}
