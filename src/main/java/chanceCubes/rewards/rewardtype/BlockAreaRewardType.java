package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.BlockAreaPart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class BlockAreaRewardType extends BaseRewardType<BlockAreaPart>
{
	public BlockAreaRewardType(BlockAreaPart... parts)
	{
		super(parts);
	}

	@Override
	protected void trigger(BlockAreaPart blockPart, ServerLevel level, int x, int y, int z, Player player)
	{
		blockPart.placeBlocks(level, player, x, y, z);
	}
}
