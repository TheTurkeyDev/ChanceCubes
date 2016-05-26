package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.util.RewardsUtil;

public class FiveProngReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		for(int xx = x - 3; xx <= x + 3; xx++)
			for(int zz = z - 3; zz <= z + 3; zz++)
				for(int yy = y; yy <= y + 4; yy++)
					RewardsUtil.placeBlock(Blocks.air, world, xx, yy, zz);

		RewardsUtil.placeBlock(Blocks.quartz_block, world, x, y, z);
		RewardsUtil.placeBlock(Blocks.quartz_block, world, x, y + 1, z);
		RewardsUtil.placeBlock(CCubesBlocks.chanceIcosahedron, world, x, y + 2, z);

		RewardsUtil.placeBlock(Blocks.quartz_block, world, x - 3, y, z - 3);
		RewardsUtil.placeBlock(CCubesBlocks.chanceCube, world, x - 3, y + 1, z - 3);

		RewardsUtil.placeBlock(Blocks.quartz_block, world, x - 3, y, z + 3);
		RewardsUtil.placeBlock(CCubesBlocks.chanceCube, world, x - 3, y + 1, z + 3);

		RewardsUtil.placeBlock(Blocks.quartz_block, world, x + 3, y, z - 3);
		RewardsUtil.placeBlock(CCubesBlocks.chanceCube, world, x + 3, y + 1, z - 3);

		RewardsUtil.placeBlock(Blocks.quartz_block, world, x + 3, y, z + 3);
		RewardsUtil.placeBlock(CCubesBlocks.chanceCube, world, x + 3, y + 1, z + 3);
	}

	@Override
	public int getChanceValue()
	{
		return -10;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":5_Prongs";
	}
}