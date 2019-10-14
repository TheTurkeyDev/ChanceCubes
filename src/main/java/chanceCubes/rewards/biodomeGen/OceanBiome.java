package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class OceanBiome extends BaseBiome
{
	public OceanBiome(String name)
	{
		super(name);
	}

	@Override
	public void spawnEntities(BlockPos pos, World world)
	{
		for(int i = 0; i < RewardsUtil.rand.nextInt(10) + 5; i++)
		{
			EntitySquid squid = new EntitySquid(world);
			squid.setLocationAndAngles(pos.getX() + (RewardsUtil.rand.nextInt(31) - 15), pos.getY() + 1, pos.getZ() + (RewardsUtil.rand.nextInt(31) - 15), 0, 0);
			squid.setCustomNameTag("Mango");
			world.spawnEntity(squid);
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