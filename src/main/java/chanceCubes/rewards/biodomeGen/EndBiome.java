package chanceCubes.rewards.biodomeGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.rewards.giantRewards.BioDomeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EndBiome implements IBioDomeBiome
{
	private Random rand = new Random();

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
			List<OffsetBlock> treeblocks = this.addTower(x, y, z, (delay / BioDomeReward.delayShorten));
			blocks.addAll(treeblocks);
		}
	}

	public List<OffsetBlock> addTower(int x, int y, int z, int delay)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();

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
	public void spawnEntities(BlockPos center, World world)
	{
		for(int i = 0; i < rand.nextInt(10) + 5; i++)
		{
			EndermanEntity enderman = EntityType.ENDERMAN.create(world);
			enderman.setLocationAndAngles(center.getX() + (rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (rand.nextInt(31) - 15), 0, 0);
			world.addEntity(enderman);
		}
	}
}