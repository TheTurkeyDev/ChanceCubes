package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.EffectPart;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class EffectRewardType extends BaseRewardType<EffectPart>
{

	public EffectRewardType(EffectPart... effects)
	{
		super(effects);
	}

	@Override
	protected void trigger(EffectPart part, ServerLevel level, int x, int y, int z, Player player)
	{
		int radius = part.getRadius().getIntValue();

		BlockPos pos = new BlockPos(x, y, z);
		for(int j2 = 0; j2 < level.players().size(); ++j2)
		{
			Player tempPlayer = level.players().get(j2);
			double distTo = tempPlayer.getOnPos().distSqr(pos);

			if(distTo < radius)
				tempPlayer.addEffect(part.getEffect());
		}
	}

}
