package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BlockInfectionReward extends BaseCustomReward
{
	// @formatter:off
	private final BlockState[] whitelist = { Blocks.OBSIDIAN.defaultBlockState(), Blocks.DIRT.defaultBlockState(),
			Blocks.STONE.defaultBlockState(),Blocks.MELON.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(),
			Blocks.CLAY.defaultBlockState(), RewardsUtil.getRandomWool(),Blocks.BRICKS.defaultBlockState(),
			Blocks.COBWEB.defaultBlockState(), Blocks.GLOWSTONE.defaultBlockState(), Blocks.NETHERRACK.defaultBlockState()};
	// @formatter:on

	private final BlockPos[] touchingPos = {new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 1, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0)};

	public BlockInfectionReward()
	{
		super(CCubesCore.MODID + ":world_infection", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		int delay = 0;
		int delayShorten = 20;

		BlockPos lastPos = pos;
		List<BlockPos> possibleBlocks = new ArrayList<>();
		List<BlockPos> changedBlocks = new ArrayList<>();
		changedBlocks.add(new BlockPos(0, 0, 0));
		List<OffsetBlock> blocks = new ArrayList<>();
		addSurroundingBlocks(level, pos, new BlockPos(0, 0, 0), changedBlocks, possibleBlocks);

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
				nextPos = lastPos.offset(touchingPos[RewardsUtil.rand.nextInt(touchingPos.length)]);
			}

			changedBlocks.add(nextPos);
			addSurroundingBlocks(level, pos, nextPos, changedBlocks, possibleBlocks);
			BlockState state = whitelist[RewardsUtil.rand.nextInt(whitelist.length)];
			blocks.add(new OffsetBlock(nextPos.getX(), nextPos.getY(), nextPos.getZ(), state, false, (delay / delayShorten)));
			delay++;
			lastPos = nextPos;
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(level, pos.getX(), pos.getY(), pos.getZ());

	}

	private void addSurroundingBlocks(Level level, BlockPos worldCord, BlockPos offsetCord, List<BlockPos> changedBlocks, List<BlockPos> possibleBlocks)
	{
		for(BlockPos pos : touchingPos)
		{
			BlockPos checkPos = offsetCord.offset(pos);
			if(!changedBlocks.contains(checkPos) && !possibleBlocks.contains(checkPos))
			{
				if(!(level.getBlockState(worldCord.offset(checkPos)).getBlock() instanceof AirBlock))
				{
					possibleBlocks.add(checkPos);
				}
			}
		}
	}
}
