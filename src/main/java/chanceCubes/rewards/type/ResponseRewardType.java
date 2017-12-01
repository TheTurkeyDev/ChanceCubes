package chanceCubes.rewards.type;

import chanceCubes.rewards.rewardparts.ResponsePart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ResponseRewardType extends BaseRewardType<ResponsePart>
{

	public ResponseRewardType(ResponsePart... responses)
	{
		super(responses);
	}

	@Override
	protected void trigger(ResponsePart obj, World world, int x, int y, int z, EntityPlayer player)
	{

	}

}