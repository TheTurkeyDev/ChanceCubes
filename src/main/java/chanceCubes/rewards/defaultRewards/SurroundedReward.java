package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class SurroundedReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		EntityEnderman enderman;
		for(int xx = 0; xx < 2; xx++)
		{
			for(int zz = -4; zz < 5; zz++)
			{
				int xxx = xx == 1 ? x + 4 : x - 4;
				if(!world.getBlock(xxx, y, z + zz).isOpaqueCube() && !world.getBlock(xxx, y + 1, z + zz).isOpaqueCube() && !world.getBlock(xxx, y + 2, z + zz).isOpaqueCube())
				{
					enderman = new EntityEnderman(world);
					enderman.setLocationAndAngles(xxx, y, z + zz, xx == 1 ? 90 : -90, 0);
					world.spawnEntityInWorld(enderman);
				}
			}
		}

		for(int xx = -4; xx < 5; xx++)
		{
			for(int zz = 0; zz < 2; zz++)
			{
				int zzz = zz == 1 ? z + 4 : z - 4;
				if(world.getBlock(x + xx, y, zzz).equals(Blocks.air) && world.getBlock(x + xx, y + 1, zzz).equals(Blocks.air) && world.getBlock(x + xx, y + 2, zzz).equals(Blocks.air))
				{
					enderman = new EntityEnderman(world);
					enderman.setLocationAndAngles(x + xx, y, zzz, zz == 1 ? 180 : 0, 0);
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
