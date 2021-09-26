package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.Random;

public class SkyblockReward extends BaseCustomReward
{
	// @formatter:off

	private static final Random TREE_RAND = new Random(System.currentTimeMillis());
	
	ItemStack[] chestStuff = {
		new ItemStack(Items.STRING, 12), new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.BONE), new ItemStack(Items.SUGAR_CANE),
		new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.ICE, 2), new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Blocks.OAK_SAPLING),
		new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Items.MELON), new ItemStack(Blocks.CACTUS), new ItemStack(Blocks.OAK_LOG, 6)
		};

	// @formatter:on
	public SkyblockReward()
	{
		super(CCubesCore.MODID + ":sky_block", 10);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		int skyblockHeight = level.getHeight() - 16;
		if(!level.getDimensionType().hasSkyLight())
			skyblockHeight = pos.getY();
		Block b = Blocks.DIRT;
		BlockPos skyblockPos = new BlockPos(pos.getX(), skyblockHeight, pos.getZ());
		for(int i = 0; i < 3; i++)
		{
			if(i == 2)
				b = Blocks.GRASS_BLOCK;
			for(int c = 0; c < 3; c++)
			{
				int xOffset = c == 0 ? -1 : 2;
				int zOffset = c == 2 ? 2 : -1;
				for(int xx = 0; xx < 3; xx++)
				{
					for(int zz = 0; zz < 3; zz++)
					{
						level.setBlock(skyblockPos.offset(xOffset + xx, i, zOffset + zz), b.defaultBlockState(), 3);
						// RewardsUtil.placeBlock(b.getDefaultState(), world, skyblockPos.add(xOffset + xx, i, zOffset + zz));
					}
				}
			}
		}
		RewardsUtil.placeBlock(Blocks.BEDROCK.defaultBlockState(), level, skyblockPos.offset(0, 1, 0));

		//OakTree tree = new OakTree();
		//tree.func_225545_a_(world, world.getChunkProvider().getChunkGenerator(), skyblockPos.add(3, 3, 3), Blocks.OAK_SAPLING.getDefaultState().with(SaplingBlock.STAGE, 1), TREE_RAND);

		RewardsUtil.placeBlock(Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST), level, skyblockPos.offset(-1, 3, 0));
		BlockEntity te = level.getBlockEntity(skyblockPos.offset(-1, 3, 0));

		if(!(te instanceof ChestBlockEntity chest))
			return;

		for(int i = 0; i < chestStuff.length; i++)
		{
			int slot = ((i < 4 ? 0 : i < 8 ? 1 : 2) * 9) + i % 4;
			chest.setItem(slot, chestStuff[i].copy());
		}

		player.moveTo(pos.getX(), skyblockHeight + 3, pos.getZ());
	}
}