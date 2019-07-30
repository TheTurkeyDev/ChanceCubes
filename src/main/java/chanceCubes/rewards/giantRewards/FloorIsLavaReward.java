package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class FloorIsLavaReward extends BaseCustomReward
{
	public FloorIsLavaReward()
	{
		super(CCubesCore.MODID + ":Floor_Is_Lava", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		player.sendMessage(new StringTextComponent("Quick! The Floor is lava!"));
		List<OffsetBlock> blocks = new ArrayList<>();
		int delay = 0;
		for(int yy = pos.getY() + 5; yy > pos.getY() - 5; yy--)
		{
			int xx = 0, zz = 0, dx = 0, dy = -1;
			int t = 32;
			int maxI = t * t;

			for(int i = 0; i < maxI; i++)
			{
				if((-16 / 2 <= xx) && (xx <= 16 / 2) && (-16 / 2 <= zz) && (zz <= 16 / 2))
				{
					Block blockAt = world.getBlockState(new BlockPos(pos.getX() + xx, yy, pos.getZ() + zz)).getBlock();
					if(!blockAt.equals(Blocks.AIR) && !blockAt.equals(CCubesBlocks.GIANT_CUBE))
					{
						blocks.add(new OffsetBlock(xx, yy - pos.getY(), zz, Blocks.LAVA, false, delay));
						delay++;
					}
				}

				if((xx == zz) || ((xx < 0) && (xx == -zz)) || ((xx > 0) && (xx == 1 - zz)))
				{
					t = dx;
					dx = -dy;
					dy = t;
				}
				xx += dx;
				zz += dy;
			}
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}
}