package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.sounds.CCubesSounds;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChunkFlipReward extends BaseCustomReward
{
	public ChunkFlipReward()
	{
		super(CCubesCore.MODID + ":chunk_flip", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		int z = (pos.getZ() >> 4) << 4;
		int x = (pos.getX() >> 4) << 4;
		level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), CCubesSounds.GIANT_CUBE_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
		RewardsUtil.sendMessageToPlayer(player, "Inception!!!!");
		Scheduler.scheduleTask(new Task("Chunk_Flip_Delay", -1, 10)
		{
			private int y = 0;

			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				if(y >= level.getHeight() / 2)
				{
					Scheduler.removeTask(this);
					return;
				}

				for(int zz = 0; zz < 16; zz++)
				{
					for(int xx = 0; xx < 16; xx++)
					{
						BlockPos pos1 = new BlockPos(x + xx, y, z + zz);
						BlockPos pos2 = new BlockPos(x + xx, level.getHeight() - y, z + zz);
						BlockState b = level.getBlockState(pos1);
						BlockState b2 = level.getBlockState(pos2);

						BlockEntity te1 = level.getBlockEntity(pos1);
						BlockEntity te2 = level.getBlockEntity(pos2);

						if(!b.getBlock().equals(Blocks.GRAVEL) && !b.getBlock().equals(CCubesBlocks.GIANT_CUBE) && !RewardsUtil.isBlockUnbreakable(level, pos) && !CCubesSettings.nonReplaceableBlocks.contains(level.getBlockState(pos)))
						{
							level.setBlock(pos1, b2, 2);
							level.setBlock(pos2, b, 2);
							if(te1 != null)
								level.setBlockEntity(te1);
							if(te2 != null)
								level.setBlockEntity(te2);
						}
					}
				}
				y++;
			}
		});
	}
}