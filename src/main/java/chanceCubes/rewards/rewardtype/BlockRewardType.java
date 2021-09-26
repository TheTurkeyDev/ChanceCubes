package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class BlockRewardType extends BaseRewardType<OffsetBlock>
{

	public BlockRewardType(OffsetBlock... blocks)
	{
		super(blocks);
	}

	@Override
	protected void trigger(OffsetBlock block, ServerLevel level, int x, int y, int z, Player player)
	{
		if(block == null)
			return;
		if(block.isRelativeToPlayer() && !RewardsUtil.isBlockUnbreakable(level, new BlockPos((int) Math.floor(player.getX()), (int) Math.floor(player.getY()), (int) Math.floor(player.getZ()))))
			block.spawnInWorld(level, (int) Math.floor(player.getX()), (int) Math.floor(player.getY()), (int) Math.floor(player.getZ()));
		else if(!RewardsUtil.isBlockUnbreakable(level, new BlockPos(x, y + 3, z)))
			block.spawnInWorld(level, x, y, z);
	}
}