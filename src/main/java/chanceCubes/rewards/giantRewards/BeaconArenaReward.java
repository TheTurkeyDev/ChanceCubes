package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BeaconArenaReward extends BaseCustomReward
{
	// @formatter:off
	private IBlockState[] whitelist = { Blocks.OBSIDIAN.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.STONE.getDefaultState(), 
			Blocks.MELON_BLOCK.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), Blocks.CLAY.getDefaultState(),
			Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(RewardsUtil.rand.nextInt(16))),
			Blocks.BRICK_BLOCK.getDefaultState(), Blocks.WEB.getDefaultState(), Blocks.GLOWSTONE.getDefaultState(),
			Blocks.NETHERRACK.getDefaultState()};
	// @formatter:on

	public BeaconArenaReward()
	{
		super(CCubesCore.MODID + ":Beacon_Arena", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
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
			b.spawnInWorld(world, pos.getX(), pos.getY() - 1, pos.getZ());
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
			IBlockState state = whitelist[RewardsUtil.rand.nextInt(whitelist.length)];
			blocks.add(new OffsetBlock(pos.getX(), -1, pos.getZ(), state, false, (delay / 8)));
			delay++;
		}
	}
}
