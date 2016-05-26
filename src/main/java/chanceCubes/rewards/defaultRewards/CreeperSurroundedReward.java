package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CreeperSurroundedReward implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		int px = (int) player.posX;
		int pz = (int) player.posZ;
		player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 1, true, false));
		boolean skip = false;
		EntityCreeper creeper;
		for(int xx = 0; xx < 2; xx++)
		{
			for(int zz = -4; zz < 5; zz++)
			{
				if(!skip)
				{
					int xxx = xx == 1 ? px + 4 : px - 4;
					IBlockState blockState = world.getBlockState(new BlockPos(px + xxx, pos.getY(),pz + zz));
					IBlockState blockState2 = world.getBlockState(new BlockPos(px + xxx, pos.getY() + 1, pz + zz));
					IBlockState blockState3 = world.getBlockState(new BlockPos(px + xxx, pos.getY() + 2, pz + zz));
					if(!blockState.isFullBlock() && !blockState2.isFullBlock() && !blockState3.isFullBlock())
					{
						creeper = new EntityCreeper(world);
						creeper.setLocationAndAngles(xxx, pos.getY(), pz + zz, xx == 1 ? 90 : -90, 0);
						if(rand.nextInt(10) == 1)
							creeper.onStruckByLightning(null);
						creeper.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 5));
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
					int zzz = zz == 1 ? pz + 4 : pz - 4;
					if(world.getBlockState(new BlockPos(px + xx, pos.getY(), zzz)).equals(Blocks.AIR) && world.getBlockState(new BlockPos(px + xx, pos.getY() + 1, zzz)).equals(Blocks.AIR) && world.getBlockState(new BlockPos(px + xx, pos.getY() + 2, zzz)).equals(Blocks.AIR))
					{
						creeper = new EntityCreeper(world);
						creeper.setLocationAndAngles(px + xx, pos.getY(), zzz, zz == 1 ? 180 : 0, 0);
						if(rand.nextInt(10) == 1)
							creeper.onStruckByLightning(null);
						creeper.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 5));
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
		return -85;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Surrounded_Creeper";
	}

}
