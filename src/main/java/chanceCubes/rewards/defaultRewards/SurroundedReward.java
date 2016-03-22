package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SurroundedReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		EntityEnderman enderman;
		for(int xx = 0; xx < 2; xx++)
		{
			for(int zz = -4; zz < 5; zz++)
			{
				if(!world.getBlockState(pos.add(xx == 1 ? 4 : -4, 0, zz)).getBlock().isFullBlock() && !world.getBlockState(pos.add(xx == 1 ? 4 : -4, 1, zz)).getBlock().isFullBlock() && !world.getBlockState(pos.add(xx == 1 ? 4 : -4, 2, zz)).getBlock().isFullBlock())
				{
					enderman = new EntityEnderman(world);
					enderman.setLocationAndAngles(xx == 1 ? pos.getX() + 4 : pos.getX() - 4, pos.getY(), pos.getZ() + zz, xx == 1 ? 90 : -90, 0);
					world.spawnEntityInWorld(enderman);
				}
			}
		}

		for(int xx = -4; xx < 5; xx++)
		{
			for(int zz = 0; zz < 2; zz++)
			{
				if(world.getBlockState(pos.add(xx, 0, zz == 1 ? 4 : -4)).equals(Blocks.air) && world.getBlockState(pos.add(xx, 1, zz == 1 ? 4 : -4)).equals(Blocks.air) && world.getBlockState(pos.add(xx, 2, zz == 1 ? 4 : -4)).equals(Blocks.air))
				{
					enderman = new EntityEnderman(world);
					enderman.setLocationAndAngles(pos.getX() + xx, pos.getY(), zz == 1 ? pos.getZ() + 4 : pos.getZ() - 4, zz == 1 ? 180 : 0, 0);
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
