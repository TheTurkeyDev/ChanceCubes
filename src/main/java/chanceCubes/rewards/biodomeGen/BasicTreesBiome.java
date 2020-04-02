package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicTreesBiome extends BaseBiome
{

	public BasicTreesBiome(String name)
	{
		super(name);
	}

	@Override
	public Block getFloorBlock()
	{
		return Blocks.GRASS_BLOCK;
	}

	@Override
	public void getRandomGenBlock(float dist, Random rand, int x, int y, int z, List<OffsetBlock> blocks, int delay)
	{
		if(y != 0)
			return;
		if(dist < 0 && rand.nextInt(5) == 0)
		{
			OffsetBlock osb = new OffsetBlock(x, y + 1, z, Blocks.GRASS, false, (delay / BioDomeGen.delayShorten));
			osb.setBlockState(Blocks.GRASS.getDefaultState());
			blocks.add(osb);
		}
		else if(dist < -5 && rand.nextInt(100) == 0)
		{
			List<OffsetBlock> treeblocks = this.addTree(x, y, z, (delay / BioDomeGen.delayShorten));
			blocks.addAll(treeblocks);
		}
	}

	public List<OffsetBlock> addTree(int x, int y, int z, int delay)
	{
		List<OffsetBlock> blocks = new ArrayList<>();

		for(int yy = 1; yy < 6; yy++)
		{
			blocks.add(new OffsetBlock(x, y + yy, z, Blocks.OAK_LOG, false, delay));
			delay++;
		}

		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				for(int yy = 0; yy < 2; yy++)
				{
					if((xx != 0 || zz != 0))
					{
						blocks.add(new OffsetBlock(x + xx, y + 4 + yy, z + zz, Blocks.OAK_LEAVES, false, delay));
						delay++;
					}
				}
			}
		}

		blocks.add(new OffsetBlock(x + 1, y + 6, z, Blocks.OAK_LEAVES, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x - 1, y + 6, z, Blocks.OAK_LEAVES, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z + 1, Blocks.OAK_LEAVES, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z - 1, Blocks.OAK_LEAVES, false, delay));
		delay++;
		blocks.add(new OffsetBlock(x, y + 6, z, Blocks.OAK_LEAVES, false, delay));

		return blocks;
	}

	@Override
	public void spawnEntities(BlockPos center, World world)
	{
		for(int i = 0; i < RewardsUtil.rand.nextInt(10) + 5; i++)
		{
			int ri = RewardsUtil.rand.nextInt(5);

			if(ri == 0)
			{
				ChickenEntity chicken = EntityType.CHICKEN.create(world);
				chicken.setLocationAndAngles(center.getX() + (RewardsUtil.rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (RewardsUtil.rand.nextInt(31) - 15), 0, 0);
				world.addEntity(chicken);
			}
			else if(ri == 1)
			{
				CowEntity cow = EntityType.COW.create(world);
				cow.setLocationAndAngles(center.getX() + (RewardsUtil.rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (RewardsUtil.rand.nextInt(31) - 15), 0, 0);
				world.addEntity(cow);
			}
			else if(ri == 2)
			{
				HorseEntity horse = EntityType.HORSE.create(world);
				horse.setLocationAndAngles(center.getX() + (RewardsUtil.rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (RewardsUtil.rand.nextInt(31) - 15), 0, 0);
				world.addEntity(horse);
			}
			else if(ri == 3)
			{
				PigEntity pig = EntityType.PIG.create(world);
				pig.setLocationAndAngles(center.getX() + (RewardsUtil.rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (RewardsUtil.rand.nextInt(31) - 15), 0, 0);
				world.addEntity(pig);
			}
			else if(ri == 4)
			{
				SheepEntity sheep = EntityType.SHEEP.create(world);
				sheep.setLocationAndAngles(center.getX() + (RewardsUtil.rand.nextInt(31) - 15), center.getY() + 1, center.getZ() + (RewardsUtil.rand.nextInt(31) - 15), 0, 0);
				world.addEntity(sheep);
			}
		}
	}
}