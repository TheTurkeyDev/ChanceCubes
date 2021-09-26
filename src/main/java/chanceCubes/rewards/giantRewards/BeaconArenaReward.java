package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BeaconArenaReward extends BaseCustomReward
{
	// @formatter:off
	private final BlockState[] whitelist = { Blocks.OBSIDIAN.defaultBlockState(), Blocks.DIRT.defaultBlockState(),
			Blocks.STONE.defaultBlockState(),Blocks.MELON.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(),
			Blocks.CLAY.defaultBlockState(), RewardsUtil.getRandomWool(),Blocks.BRICKS.defaultBlockState(),
			Blocks.COBWEB.defaultBlockState(), Blocks.GLOWSTONE.defaultBlockState(), Blocks.NETHERRACK.defaultBlockState()};
	// @formatter:on

	public BeaconArenaReward()
	{
		super(CCubesCore.MODID + ":beacon_arena", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		List<OffsetBlock> blocks = new ArrayList<>();
		spawnWall(blocks);
		spawnSmallBeacon(blocks, new BlockPos(17, 0, 17), Blocks.GOLD_BLOCK);
		spawnSmallBeacon(blocks, new BlockPos(-17, 0, 17), Blocks.DIAMOND_BLOCK);
		spawnSmallBeacon(blocks, new BlockPos(-17, 0, -17), Blocks.EMERALD_BLOCK);
		spawnSmallBeacon(blocks, new BlockPos(17, 0, -17), Blocks.IRON_BLOCK);
		spawnBigBeacon(blocks);
		editFloor(blocks);

		for(OffsetBlock b : blocks)
			b.spawnInWorld(level, pos.getX(), pos.getY() - 1, pos.getZ());
	}

	public void spawnSmallBeacon(List<OffsetBlock> blocks, BlockPos at, Block b)
	{
		int delay = 0;
		for(int x = -1; x < 2; x++)
		{
			for(int z = -1; z < 2; z++)
			{
				blocks.add(new OffsetBlock(at.getX() + x, at.getY(), at.getZ() + z, b, false, delay));
				delay++;
			}
		}
		blocks.add(new OffsetBlock(at.getX(), at.getY() + 1, at.getZ(), Blocks.BEACON, false, delay).setCausesBlockUpdate(true));
	}

	public void spawnBigBeacon(List<OffsetBlock> blocks)
	{
		int delay = 0;
		for(int y = 0; y < 2; y++)
		{
			for(int x = -2; x < 3; x++)
			{
				for(int z = -2; z < 3; z++)
				{
					if(y != 1 || (x > -2 && x < 2 && z > -2 && z < 2))
					{
						blocks.add(new OffsetBlock(x, y, z, Blocks.IRON_BLOCK, false, delay));
						delay++;
					}
				}
			}
		}
		blocks.add(new OffsetBlock(0, 2, 0, Blocks.BEACON, false, delay).setCausesBlockUpdate(true));
	}

	public void spawnWall(List<OffsetBlock> blocks)
	{
		List<BlockPos> usedPositions = new ArrayList<>();
		BlockPos temp;
		for(int degree = 0; degree < 360; degree++)
		{
			double arcVal = Math.toRadians(degree);
			int x = (int) (28d * Math.cos(arcVal));
			int z = (int) (28d * Math.sin(arcVal));
			temp = new BlockPos(x, 0, z);
			if(!usedPositions.contains(temp))
			{
				usedPositions.add(temp);
			}
		}

		int delay = 0;
		for(BlockPos pos : usedPositions)
		{
			blocks.add(new OffsetBlock(pos.getX(), pos.getY(), pos.getZ(), Blocks.GLASS, false, delay));
			blocks.add(new OffsetBlock(pos.getX(), pos.getY() + 1, pos.getZ(), Blocks.GLASS, false, delay + 1));
			blocks.add(new OffsetBlock(pos.getX(), pos.getY() + 2, pos.getZ(), Blocks.GLASS, false, delay + 2));
			delay++;
		}
	}

	public void editFloor(List<OffsetBlock> blocks)
	{
		int delay = 0;
		List<BlockPos> usedPositions = new ArrayList<>();
		BlockPos temp;
		for(int radius = 0; radius < 28; radius++)
		{
			for(int degree = 0; degree < 360; degree++)
			{
				double arcVal = Math.toRadians(degree);
				int x = (int) (radius * Math.cos(arcVal));
				int z = (int) (radius * Math.sin(arcVal));
				temp = new BlockPos(x, 0, z);
				if(!usedPositions.contains(temp))
				{
					usedPositions.add(temp);
				}
			}
		}
		for(BlockPos pos : usedPositions)
		{
			BlockState state = whitelist[RewardsUtil.rand.nextInt(whitelist.length)];
			blocks.add(new OffsetBlock(pos.getX(), -1, pos.getZ(), state, false, (delay / 8)));
			delay++;
		}
	}
}
