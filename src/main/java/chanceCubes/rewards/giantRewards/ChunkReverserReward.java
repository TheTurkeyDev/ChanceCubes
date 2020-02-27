package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.CustomEntry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ChunkReverserReward extends BaseCustomReward
{
	private List<Entry<Block, Block>> swappedMap = new ArrayList<>();

	public ChunkReverserReward()
	{
		super(CCubesCore.MODID + ":chuck_reverse", 0);
		swappedMap.add(new CustomEntry<>(Blocks.DIRT, Blocks.COBBLESTONE));
		swappedMap.add(new CustomEntry<>(Blocks.STONE, Blocks.DIRT));
		swappedMap.add(new CustomEntry<>(Blocks.GRASS, Blocks.STONE));
		swappedMap.add(new CustomEntry<>(Blocks.GRAVEL, Blocks.SAND));
		swappedMap.add(new CustomEntry<>(Blocks.SAND, Blocks.GRAVEL));
		swappedMap.add(new CustomEntry<>(Blocks.IRON_ORE, Blocks.GOLD_ORE));
		swappedMap.add(new CustomEntry<>(Blocks.COAL_ORE, Blocks.DIAMOND_ORE));
		swappedMap.add(new CustomEntry<>(Blocks.DIAMOND_ORE, Blocks.COAL_ORE));
		swappedMap.add(new CustomEntry<>(Blocks.GOLD_ORE, Blocks.IRON_ORE));
		swappedMap.add(new CustomEntry<>(Blocks.LAVA, Blocks.WATER));
		swappedMap.add(new CustomEntry<>(Blocks.WATER, Blocks.LAVA));
		swappedMap.add(new CustomEntry<>(Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES));
		swappedMap.add(new CustomEntry<>(Blocks.ACACIA_LEAVES, Blocks.ACACIA_LOG));
		swappedMap.add(new CustomEntry<>(Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES));
		swappedMap.add(new CustomEntry<>(Blocks.DARK_OAK_LEAVES, Blocks.DARK_OAK_LOG));
		swappedMap.add(new CustomEntry<>(Blocks.OAK_LOG, Blocks.OAK_LEAVES));
		swappedMap.add(new CustomEntry<>(Blocks.OAK_LEAVES, Blocks.OAK_LOG));
		swappedMap.add(new CustomEntry<>(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES));
		swappedMap.add(new CustomEntry<>(Blocks.BIRCH_LEAVES, Blocks.BIRCH_LOG));
		swappedMap.add(new CustomEntry<>(Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES));
		swappedMap.add(new CustomEntry<>(Blocks.JUNGLE_LEAVES, Blocks.JUNGLE_LOG));
		swappedMap.add(new CustomEntry<>(Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES));
		swappedMap.add(new CustomEntry<>(Blocks.SPRUCE_LEAVES, Blocks.SPRUCE_LOG));
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		player.sendMessage(new StringTextComponent("Initiating Block Inverter"));
		List<OffsetBlock> blocks = new ArrayList<>();
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

		player.sendMessage(new StringTextComponent("Inverting " + blocks.size() + " Blocks... May take a minute..."));
		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}
}