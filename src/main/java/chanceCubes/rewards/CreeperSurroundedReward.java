package chanceCubes.rewards;

import java.util.Random;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class CreeperSurroundedReward implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 1, true));
		boolean skip = false;
		EntityCreeper creeper;
		for(int xx = 0; xx < 2; xx++)
		{
			for(int zz = -4; zz < 5; zz++)
			{
				if(!skip)
				{
					int xxx = xx == 1 ? x + 4 : x - 4;
					if(world.getBlock(xxx, y, z + zz).equals(Blocks.air) && world.getBlock(xxx, y + 1, z + zz).equals(Blocks.air) && world.getBlock(xxx, y + 2, z + zz).equals(Blocks.air))
					{
						creeper = new EntityCreeper(world);
						creeper.setLocationAndAngles(xxx, y, z + zz, xx == 1 ? 90 : -90, 0);
						if(rand.nextInt(10) == 1)
							creeper.getDataWatcher().updateObject(17, Byte.valueOf((byte) 1));
						world.spawnEntityInWorld(creeper);
					}
				}
				skip = !skip;
			}
		}

		for(int xx = -4; xx < 5; xx++)
		{
			for(int zz = 0; zz < 2; zz++)
			{
				if(!skip)
				{
					int zzz = zz == 1 ? z + 4 : z - 4;
					if(world.getBlock(x + xx, y, zzz).equals(Blocks.air) && world.getBlock(x + xx, y + 1, zzz).equals(Blocks.air) && world.getBlock(x + xx, y + 2, zzz).equals(Blocks.air))
					{
						creeper = new EntityCreeper(world);
						creeper.setLocationAndAngles(x + xx, y, zzz, zz == 1 ? 180 : 0, 0);
						if(rand.nextInt(10) == 1)
							creeper.getDataWatcher().updateObject(17, Byte.valueOf((byte) 1));
						world.spawnEntityInWorld(creeper);
					}
				}
				skip = !skip;
			}
		}
	}

	@Override
	public int getChanceValue()
	{
		return -70;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Surrounded_Creeper";
	}

}
