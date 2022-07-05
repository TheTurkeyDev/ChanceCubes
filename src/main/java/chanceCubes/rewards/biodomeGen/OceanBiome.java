package chanceCubes.rewards.biodomeGen;

import chanceCubes.mcwrapper.ComponentWrapper;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Random;

public class OceanBiome extends BaseBiome
{
	public OceanBiome(String name)
	{
		super(name);
	}

	@Override
	public void spawnEntities(BlockPos pos, ServerLevel level)
	{
		for(int i = 0; i < RewardsUtil.rand.nextInt(10) + 5; i++)
		{
			Squid squid = EntityType.SQUID.create(level);
			squid.moveTo(pos.getX() + (RewardsUtil.rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (RewardsUtil.rand.nextInt(31) - 15), 0, 0);
			squid.setCustomName(ComponentWrapper.string("Mango"));
			level.addFreshEntity(squid);
		}
	}

	@Override
	public Block getFloorBlock()
	{
		return Blocks.CLAY;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y == 0 || dist >= 0)
			return;
		blocks.add(new OffsetBlock(x, y, z, Blocks.WATER, false, delay / BioDomeGen.delayShorten));
	}
}