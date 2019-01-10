package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoubleRainbow implements IChanceCubeReward
{
	Block[] colors = new Block[]{ Blocks.RED_WOOL, Blocks.ORANGE_WOOL, Blocks.YELLOW_WOOL, Blocks.GREEN_WOOL, Blocks.BLUE_WOOL, Blocks.PURPLE_WOOL };

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "Double Rainbow!");
		OffsetBlock b;
		for(int x = -7; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				float dist = (float) (Math.abs(pos.getDistance(pos.getX() + x, pos.getY() + y, pos.getZ())));
				if(dist > 1 && dist <= 8)
				{
					int distIndex = (int) (dist - 2);
					Block wool = colors[distIndex];
					b = new OffsetBlock(x, y, 0, wool, false);
					b.setBlockState(wool.getDefaultState());
					b.setDelay((x + 7) * 10);
					b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}

		for(int x = -17; x < 18; x++)
		{
			for(int y = 0; y < 18; y++)
			{
				float dist = (float) (Math.abs(pos.getDistance(pos.getX() + x, pos.getY() + y, pos.getZ())));
				if(dist >= 12 && dist <= 18)
				{
					int distIndex = (int) (dist - 12);
					Block wool = colors[distIndex];
					b = new OffsetBlock(x, y, 0, wool, false);
					b.setBlockState(wool.getDefaultState());
					b.setDelay((x + 12) * 5);
					b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
	}

	@Override
	public int getChanceValue()
	{
		return 15;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Double_Rainbow";
	}

}
