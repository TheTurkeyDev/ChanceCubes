package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class SkyblockReward implements IChanceCubeReward
{

	private int skyblockHeight = 240;

	// @formatter:off
	
	ItemStack[] chestStuff = { 
		new ItemStack(Items.string, 12), new ItemStack(Items.lava_bucket), new ItemStack(Items.bone), new ItemStack(Items.reeds), 
		new ItemStack(Blocks.red_mushroom), new ItemStack(Blocks.ice, 2), new ItemStack(Items.pumpkin_seeds), new ItemStack(Blocks.sapling), 
		new ItemStack(Blocks.brown_mushroom), new ItemStack(Items.melon), new ItemStack(Blocks.cactus), new ItemStack(Blocks.log, 6)
		};

	// @formatter:on

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{

		Block b = Blocks.dirt;
		for(int i = 0; i < 3; i++)
		{
			if(i == 2)
				b = Blocks.grass;
			for(int c = 0; c < 3; c++)
			{
				int xOffset = c == 0 ? -1 : 2;
				int zOffset = c == 2 ? 2 : -1;
				for(int xx = 0; xx < 3; xx++)
				{
					for(int zz = 0; zz < 3; zz++)
					{
						RewardsUtil.placeBlock(b, world, x + xOffset + xx, skyblockHeight + i, z + zOffset + zz);
					}
				}
			}
		}
		RewardsUtil.placeBlock(Blocks.bedrock, world, x, skyblockHeight + 1, z);

		WorldGenTrees treeGen = new WorldGenTrees(true, 4, 0, 0, false);
		treeGen.generate(world, new Random(), x + 4, skyblockHeight + 3, z + 4);

		RewardsUtil.placeBlock(Blocks.chest, world, x - 1, skyblockHeight + 3, z);
		world.setBlockMetadataWithNotify(x - 1, skyblockHeight + 3, z, 5, 3);
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(x - 1, skyblockHeight + 3, z);
		for(int i = 0; i < chestStuff.length; i++)
		{
			int slot = ((i < 4 ? 0 : i < 8 ? 1 : 2) * 9) + i % 4;
			chest.setInventorySlotContents(slot, chestStuff[i]);
		}

		player.setPositionAndUpdate(x, skyblockHeight + 3, z);
	}

	@Override
	public int getChanceValue()
	{
		return 10;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":SkyBlock";
	}
}
