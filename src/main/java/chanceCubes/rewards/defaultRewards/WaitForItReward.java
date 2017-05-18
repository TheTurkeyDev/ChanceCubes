package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class WaitForItReward implements IChanceCubeReward
{
	@Override
	public void trigger(final World world, BlockPos pos, final EntityPlayer player)
	{
		player.addChatMessage(new TextComponentString("Wait for it......."));

		Scheduler.scheduleTask(new Task("Wait For It", RewardsUtil.rand.nextInt(4000) + 1000)
		{
			@Override
			public void callback()
			{
				int reward = RewardsUtil.rand.nextInt(3);
				player.addChatMessage(new TextComponentString("NOW!"));

				if(reward == 0)
				{
					world.spawnEntityInWorld(new EntityTNTPrimed(world, player.posX, player.posY + 1, player.posZ, null));
				}
				else if(reward == 1)
				{
					EntityCreeper ent = new EntityCreeper(world);
					ent.setLocationAndAngles(player.posX, player.posY + 1, player.posZ, 0, 0);
					ent.onStruckByLightning(null);
					world.spawnEntityInWorld(ent);
				}
				else if(reward == 2)
				{
					world.setBlockState(new BlockPos(player.posX, player.posY, player.posZ), Blocks.BEDROCK.getDefaultState());
				}
				else if(reward == 3)
				{
					world.setBlockState(new BlockPos(player.posX, player.posY, player.posZ), Blocks.EMERALD_ORE.getDefaultState());
				}
				else if(reward == 4)
				{
					EntityZombie zomb = new EntityZombie(world);
					zomb.setChild(true);
					zomb.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100000, 0));
					zomb.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100000, 0));
					world.spawnEntityInWorld(zomb);
				}
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return -30;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Wait_For_It";
	}

}
