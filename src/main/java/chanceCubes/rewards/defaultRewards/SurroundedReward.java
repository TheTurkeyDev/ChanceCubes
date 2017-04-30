package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SurroundedReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		int px = (int) player.posX;
		int pz = (int) player.posZ;
		EntityEnderman enderman;
		for(int xx = 0; xx < 2; xx++)
		{
			int xValue = px + (xx == 0 ? -4 : 4);
			for(int zz = -4; zz < 5; zz++)
			{

				IBlockState blockState = world.getBlockState(new BlockPos(xValue, pos.getY(), pz + zz));
				IBlockState blockState2 = world.getBlockState(new BlockPos(xValue, pos.getY() + 1, pz + zz));
				IBlockState blockState3 = world.getBlockState(new BlockPos(xValue, pos.getY() + 2, pz + zz));
				if(!blockState.isFullBlock() && !blockState2.isFullBlock() && !blockState3.isFullBlock())
				{
					enderman = new EntityEnderman(world);
					enderman.setLocationAndAngles(xValue, pos.getY(), pos.getZ() + zz, xx == 1 ? 90 : -90, 0);
					world.spawnEntityInWorld(enderman);
				}
			}
		}

		for(int zz = 0; zz < 2; zz++)
		{
			int zValue = pz + (zz == 0 ? -4 : 4);
			for(int xx = -4; xx < 5; xx++)
			{
				IBlockState blockState = world.getBlockState(new BlockPos(px + xx, pos.getY(), zValue));
				IBlockState blockState2 = world.getBlockState(new BlockPos(px + xx, pos.getY() + 1, zValue));
				IBlockState blockState3 = world.getBlockState(new BlockPos(px + xx, pos.getY() + 2, zValue));
				if(!blockState.isFullBlock() && !blockState2.isFullBlock() && !blockState3.isFullBlock())
				{
					enderman = new EntityEnderman(world);
					enderman.setLocationAndAngles(pos.getX() + xx, pos.getY(), zValue, zz == 1 ? 180 : 0, 0);
					world.spawnEntityInWorld(enderman);
				}
			}
		}
	}

	@Override
	public int getChanceValue()
	{
		return -45;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Surrounded";
	}

}
