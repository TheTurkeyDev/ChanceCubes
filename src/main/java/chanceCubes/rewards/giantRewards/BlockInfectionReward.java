package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class BlockInfectionReward extends BaseCustomReward
{
	// @formatter:off
	private BlockState[] whitelist = { Blocks.OBSIDIAN.getDefaultState(), Blocks.DIRT.getDefaultState(),
			Blocks.STONE.getDefaultState(),Blocks.MELON.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(),
			Blocks.CLAY.getDefaultState(), RewardsUtil.getRandomWool(),Blocks.BRICKS.getDefaultState(),
			Blocks.COBWEB.getDefaultState(), Blocks.GLOWSTONE.getDefaultState(), Blocks.NETHERRACK.getDefaultState()};
	// @formatter:on

	private BlockPos[] touchingPos = {new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 1, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0)};

	public BlockInfectionReward()
	{
		super(CCubesCore.MODID + ":world_infection", 0);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		int delay = 0;
		int delayShorten = 20;

		BlockPos lastPos = pos;
		List<BlockPos> possibleBlocks = new ArrayList<>();
		List<BlockPos> changedBlocks = new ArrayList<>();
		changedBlocks.add(new BlockPos(0, 0, 0));
		List<OffsetBlock> blocks = new ArrayList<>();
		addSurroundingBlocks(world, pos, new BlockPos(0, 0, 0), changedBlocks, possibleBlocks);

		for(int i = 0; i < 5000; i++)
		{
			BlockPos nextPos;
			if(possibleBlocks.size() > 0)
			{
				int index = RewardsUtil.rand.nextInt(possibleBlocks.size());
				nextPos = possibleBlocks.get(index);
				possibleBlocks.remove(index);
			}
			else
			{
				nextPos = lastPos.add(touchingPos[RewardsUtil.rand.nextInt(touchingPos.length)]);
			}

			changedBlocks.add(nextPos);
			addSurroundingBlocks(world, pos, nextPos, changedBlocks, possibleBlocks);
			BlockState state = whitelist[RewardsUtil.rand.nextInt(whitelist.length)];
			blocks.add(new OffsetBlock(nextPos.getX(), nextPos.getY(), nextPos.getZ(), state, false, (delay / delayShorten)));
			delay++;
			lastPos = nextPos;
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());

	}

	private void addSurroundingBlocks(World world, BlockPos worldCord, BlockPos offsetCord, List<BlockPos> changedBlocks, List<BlockPos> possibleBlocks)
	{
		for(BlockPos pos : touchingPos)
		{
			BlockPos checkPos = offsetCord.add(pos);
			if(!changedBlocks.contains(checkPos) && !possibleBlocks.contains(checkPos))
			{
				if(!(world.getBlockState(worldCord.add(checkPos)).getBlock() instanceof AirBlock))
				{
					possibleBlocks.add(checkPos);
				}
			}
		}
	}
}
