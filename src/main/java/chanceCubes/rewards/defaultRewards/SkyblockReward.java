package chanceCubes.rewards.defaultRewards;

import java.util.Map;
import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class SkyblockReward extends BaseCustomReward
{
	// @formatter:off
	
	ItemStack[] chestStuff = { 
		new ItemStack(Items.STRING, 12), new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.BONE), new ItemStack(Items.REEDS), 
		new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.ICE, 2), new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Blocks.SAPLING), 
		new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Items.MELON), new ItemStack(Blocks.CACTUS), new ItemStack(Blocks.LOG, 6)
		};

	// @formatter:on
	public SkyblockReward()
	{
		super(CCubesCore.MODID + ":sky_block", 10);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		int skyblockHeight = world.getActualHeight() - 16;
		if(!world.provider.hasSkyLight())
			skyblockHeight = pos.getY();
		Block b = Blocks.DIRT;
		BlockPos skyblockPos = new BlockPos(pos.getX(), skyblockHeight, pos.getZ());
		for(int i = 0; i < 3; i++)
		{
			if(i == 2)
				b = Blocks.GRASS;
			for(int c = 0; c < 3; c++)
			{
				int xOffset = c == 0 ? -1 : 2;
				int zOffset = c == 2 ? 2 : -1;
				for(int xx = 0; xx < 3; xx++)
				{
					for(int zz = 0; zz < 3; zz++)
					{
						world.setBlockState(skyblockPos.add(xOffset + xx, i, zOffset + zz), b.getDefaultState(), 3);
						// RewardsUtil.placeBlock(b.getDefaultState(), world, skyblockPos.add(xOffset + xx, i, zOffset + zz));
					}
				}
			}
		}
		RewardsUtil.placeBlock(Blocks.BEDROCK.getDefaultState(), world, skyblockPos.add(0, 1, 0));

		WorldGenTrees treeGen = new WorldGenTrees(true, 4, Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState(), false);
		treeGen.generate(world, new Random(), skyblockPos.add(4, 3, 4));

		RewardsUtil.placeBlock(Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST), world, skyblockPos.add(-1, 3, 0));
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(skyblockPos.add(-1, 3, 0));
		for(int i = 0; i < chestStuff.length; i++)
		{
			int slot = ((i < 4 ? 0 : i < 8 ? 1 : 2) * 9) + i % 4;
			chest.setInventorySlotContents(slot, chestStuff[i].copy());
		}

		player.setPositionAndUpdate(pos.getX(), skyblockHeight + 3, pos.getZ());
	}
}