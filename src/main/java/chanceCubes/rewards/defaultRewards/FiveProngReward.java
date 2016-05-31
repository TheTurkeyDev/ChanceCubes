package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FiveProngReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int xx = pos.getX() - 3; xx <= pos.getX() + 3; xx++)
			for(int zz = pos.getZ() - 3; zz <= pos.getZ() + 3; zz++)
				for(int yy = pos.getY(); yy <= pos.getY() + 4; yy++)
					RewardsUtil.placeBlock(Blocks.air.getDefaultState(), world, new BlockPos(xx, yy, zz));

		RewardsUtil.placeBlock(Blocks.quartz_block.getDefaultState(), world, pos);
		RewardsUtil.placeBlock(Blocks.quartz_block.getDefaultState(), world, pos.add(0, 1, 0));
		RewardsUtil.placeBlock(CCubesBlocks.chanceIcosahedron.getDefaultState(), world, pos.add(0, 2, 0));

		RewardsUtil.placeBlock(Blocks.quartz_block.getDefaultState(), world, pos.add(-3, 0, -3));
		RewardsUtil.placeBlock(CCubesBlocks.chanceCube.getDefaultState(), world, pos.add(-3, 1, -3));

		RewardsUtil.placeBlock(Blocks.quartz_block.getDefaultState(), world, pos.add(-3, 0, 3));
		RewardsUtil.placeBlock(CCubesBlocks.chanceCube.getDefaultState(), world, pos.add(-3, 1, 3));

		RewardsUtil.placeBlock(Blocks.quartz_block.getDefaultState(), world, pos.add(3, 0, -3));
		RewardsUtil.placeBlock(CCubesBlocks.chanceCube.getDefaultState(), world, pos.add(3, 1, -3));

		RewardsUtil.placeBlock(Blocks.quartz_block.getDefaultState(), world, pos.add(3, 0, 3));
		RewardsUtil.placeBlock(CCubesBlocks.chanceCube.getDefaultState(), world, pos.add(3, 1, 3));
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