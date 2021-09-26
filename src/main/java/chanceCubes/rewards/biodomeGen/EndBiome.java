package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EndBiome extends BaseBiome
{

	public EndBiome(String name)
	{
		super(name);
	}

	@Override
	public Block getFloorBlock()
	{
		return Blocks.END_STONE;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y != 0)
			return;
		if(dist < -5 && rand.nextInt(200) == 0)
		{
			List<OffsetBlock> treeblocks = this.addTower(x, y, z, (delay / BioDomeGen.delayShorten));
			blocks.addAll(treeblocks);
		}
	}

	public List<OffsetBlock> addTower(int x, int y, int z, int delay)
	{
		List<OffsetBlock> blocks = new ArrayList<>();

		for(int yy = 0; yy < 10; yy++)
		{
			for(int xx = -1; xx < 2; xx++)
			{
				for(int zz = -1; zz < 2; zz++)
				{
					blocks.add(new OffsetBlock(x + xx, y + yy, z + zz, Blocks.OBSIDIAN, false, delay));
					delay++;
				}
			}
		}
		blocks.add(new OffsetBlock(x, y + 10, z, Blocks.BEDROCK, false, delay));
		return blocks;
	}

	@Override
	public void spawnEntities(BlockPos center, ServerLevel level)
	{
		for(int i = 0; i < RewardsUtil.rand.nextInt(10) + 5; i++)
		{
			EnderMan enderman = EntityType.ENDERMAN.create(level);
			enderman.moveTo(center.getX() + (RewardsUtil.rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (RewardsUtil.rand.nextInt(31) - 15), 0, 0);
			level.addFreshEntity(enderman);
		}
	}
}