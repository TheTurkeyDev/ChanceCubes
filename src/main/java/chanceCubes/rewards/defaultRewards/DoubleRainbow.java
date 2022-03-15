package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class DoubleRainbow extends BaseCustomReward
{
	private static final Block[] colors = new Block[]{Blocks.RED_WOOL, Blocks.ORANGE_WOOL, Blocks.YELLOW_WOOL, Blocks.GREEN_WOOL, Blocks.BLUE_WOOL, Blocks.PURPLE_WOOL};


	public DoubleRainbow()
	{
		super(CCubesCore.MODID + ":double_rainbow", 15);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToNearPlayers(level, pos, 32, "Double Rainbow!");
		OffsetBlock b;
		for(int x = -7; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				float dist = (float) Math.sqrt(Math.abs(pos.distToLowCornerSqr(pos.getX() + x, pos.getY() + y, pos.getZ())));
				if(dist > 1 && dist <= 8)
				{
					int distIndex = (int) (dist - 2);
					Block wool = colors[distIndex];
					b = new OffsetBlock(x, y, 0, wool, false);
					b.setDelay((x + 7) * 10);
					b.spawnInWorld(level, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}

		for(int x = -17; x < 18; x++)
		{
			for(int y = 0; y < 18; y++)
			{
				float dist = (float) Math.sqrt(Math.abs(pos.distToLowCornerSqr(pos.getX() + x, pos.getY() + y, pos.getZ())));
				if(dist >= 12 && dist <= 18)
				{
					int distIndex = (int) (dist - 12);
					Block wool = colors[distIndex];
					b = new OffsetBlock(x, y, 0, wool, false);
					b.setDelay((x + 12) * 5);
					b.spawnInWorld(level, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
	}
}
