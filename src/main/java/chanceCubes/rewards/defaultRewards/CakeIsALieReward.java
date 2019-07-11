package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockCake;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CakeIsALieReward extends BaseCustomReward
{
	public CakeIsALieReward()
	{
		super(CCubesCore.MODID + ":Cake", 20);
	}

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player, Map<String, Object> settings)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "But is it a lie?");

		RewardsUtil.placeBlock(Blocks.CAKE.getDefaultState(), world, pos);

		if(RewardsUtil.rand.nextInt(3) == 1)
		{
			Scheduler.scheduleTask(new Task("Cake_Is_A_Lie", 6000, 20)
			{
				@Override
				public void callback()
				{
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}

				@Override
				public void update()
				{
					if(!world.getBlockState(pos).getBlock().equals(Blocks.CAKE))
					{
						Scheduler.removeTask(this);
					}
					else if(world.getBlockState(pos).get(BlockCake.BITES) > 0)
					{
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "It's a lie!!!");
						EntityCreeper creeper = new EntityCreeper(world);
						creeper.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), pos.getX() == 1 ? 90 : -90, 0);
						if(RewardsUtil.rand.nextInt(10) == 1)
							creeper.onStruckByLightning(null);
						creeper.addPotionEffect(new PotionEffect(MobEffects.SPEED, 9999, 2));
						creeper.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 999));
						world.spawnEntity(creeper);
						RewardsUtil.executeCommand(world, player, player.getPositionVector(), "/advancement grant @p only chancecubes:its_a_lie");
						Scheduler.removeTask(this);
					}
				}
			});
		}
	}
}