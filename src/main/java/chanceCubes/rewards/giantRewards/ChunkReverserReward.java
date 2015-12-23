package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.CustomEntry;

public class ChunkReverserReward implements IChanceCubeReward
{

	private List<Entry<Block, Block>> swappedMap = new ArrayList<Entry<Block, Block>>();

	public ChunkReverserReward()
	{
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.stone, Blocks.dirt));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.dirt, Blocks.cobblestone));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.grass, Blocks.stone));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.iron_ore, Blocks.gold_ore));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.coal_ore, Blocks.diamond_ore));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.diamond_ore, Blocks.coal_ore));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.gold_ore, Blocks.iron_ore));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.lava, Blocks.water));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.water, Blocks.lava));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.log, Blocks.leaves));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.log2, Blocks.leaves2));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.leaves, Blocks.log));
		swappedMap.add(new CustomEntry<Block, Block>(Blocks.leaves2, Blocks.log2));
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		player.addChatMessage(new ChatComponentText("Initiating Block Inverter"));
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();
		int delay = 0;
		for(int yy = 256; yy > 0; yy--)
		{
			int xx = 0, zz = 0, dx = 0, dy = -1;
			int t = 16;
			int maxI = t * t;

			for(int i = 0; i < maxI; i++)
			{
				if((-16 / 2 <= xx) && (xx <= 16 / 2) && (-16 / 2 <= zz) && (zz <= 16 / 2))
				{
					Block blockAt = world.getBlockState(new BlockPos(pos.getX() + xx, yy, pos.getZ() + zz)).getBlock();
					Block toSwapTo = null;
					for(Entry<Block, Block> blockSwap : swappedMap)
					{
						if(blockSwap.getKey().equals(blockAt))
						{
							toSwapTo = blockSwap.getValue();
						}
					}
					if(toSwapTo != null)
					{
						blocks.add(new OffsetBlock(xx, yy - pos.getY(), zz, toSwapTo, false, delay / 5));
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

		player.addChatMessage(new ChatComponentText("Inverting " + blocks.size() + " Blocks... May take a minute..."));
		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Chuck_Reverse";
	}

}