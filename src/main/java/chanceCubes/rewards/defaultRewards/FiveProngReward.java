package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;

public class FiveProngReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		for(int xx = pos.getX()-3; xx <= pos.getX()+3; xx++)
			for(int zz = pos.getZ()-3; zz <= pos.getZ()+3; zz++)
				for(int yy = pos.getY(); yy <= pos.getY()+4; yy++)
					world.setBlockToAir(new BlockPos(xx, yy, zz));
		
		world.setBlockState(pos, Blocks.quartz_block.getDefaultState());
		world.setBlockState(pos.add(0, 1, 0), Blocks.quartz_block.getDefaultState());
		//world.setBlockState(pos.add(0, 2, 0), CCubesBlocks.chanceIcosahedron.getDefaultState());
		
		world.setBlockState(pos.add(-3, 0, -3), Blocks.quartz_block.getDefaultState());
		world.setBlockState(pos.add(-3, 1, -3), CCubesBlocks.chanceCube.getDefaultState());
		
		world.setBlockState(pos.add(-3, 0, 3), Blocks.quartz_block.getDefaultState());
		world.setBlockState(pos.add(-3, 1, 3), CCubesBlocks.chanceCube.getDefaultState());
		
		world.setBlockState(pos.add(3, 0, -3), Blocks.quartz_block.getDefaultState());
		world.setBlockState(pos.add(3, 1, -3), CCubesBlocks.chanceCube.getDefaultState());
		
		world.setBlockState(pos.add(3, 0, 3), Blocks.quartz_block.getDefaultState());
		world.setBlockState(pos.add(3, 1, 3), CCubesBlocks.chanceCube.getDefaultState());
	}

	@Override
	public int getChanceValue()
	{
		return -10;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID+":5_Prongs";
	}
}