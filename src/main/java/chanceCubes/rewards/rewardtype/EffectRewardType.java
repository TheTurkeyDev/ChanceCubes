package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.EffectPart;
import net.minecraft.dispenser.Position;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EffectRewardType extends BaseRewardType<EffectPart>
{

	public EffectRewardType(EffectPart... effects)
	{
		super(effects);
	}

	@Override
	protected void trigger(EffectPart part, World world, int x, int y, int z, PlayerEntity player)
	{
		int radius = part.getRadius().getIntValue();

		for(int j2 = 0; j2 < world.getPlayers().size(); ++j2)
		{
			PlayerEntity tempPlayer = world.getPlayers().get(j2);

			if(tempPlayer.getPosition().withinDistance(new Position(x, y, z), radius))
				tempPlayer.addPotionEffect(part.getEffect());
		}
	}

}
