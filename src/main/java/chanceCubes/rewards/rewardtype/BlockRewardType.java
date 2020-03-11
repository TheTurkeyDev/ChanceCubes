package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRewardType extends BaseRewardType<OffsetBlock>
{

	public BlockRewardType(OffsetBlock... blocks)
	{
		super(blocks);
	}

	@Override
	protected void trigger(OffsetBlock block, World world, int x, int y, int z, PlayerEntity player)
	{
		if(block == null)
			return;
		if(block.isRelativeToPlayer() && !RewardsUtil.isBlockUnbreakable(world, new BlockPos((int) Math.floor(player.getPosX()), (int) Math.floor(player.getPosY()), (int) Math.floor(player.getPosZ()))))
			block.spawnInWorld(world, (int) Math.floor(player.getPosX()), (int) Math.floor(player.getPosY()), (int) Math.floor(player.getPosZ()));
		else if(!RewardsUtil.isBlockUnbreakable(world, new BlockPos(x, y + 3, z)))
			block.spawnInWorld(world, x, y, z);
	}
}