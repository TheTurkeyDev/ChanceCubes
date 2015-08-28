package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.util.OffsetBlock;

public class BlockRewardType extends BaseRewardType<OffsetBlock>
{

	public BlockRewardType( OffsetBlock... blocks)
	{
		super(blocks);
	}

	@Override
	protected void trigger(OffsetBlock block, World world, int x, int y, int z, EntityPlayer player)
	{
		if(block.isRelativeToPlayer())
			block.spawnInWorld(world, (int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
		else
			block.spawnInWorld(world, x, y, z);
	}
}