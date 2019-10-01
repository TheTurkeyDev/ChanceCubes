package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
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

public class WaitForItReward extends BaseCustomReward
{
	public WaitForItReward()
	{
		super(CCubesCore.MODID + ":Wait_For_It", -30);
	}

	@Override
	public void trigger(final World world, BlockPos pos, final EntityPlayer player, Map<String, Object> settings)
	{
		player.sendMessage(new TextComponentString("Wait for it......."));

		int minDuration = super.getSettingAsInt(settings, "min_duration", 1000, 0, Integer.MAX_VALUE - 1);
		int maxDuration = minDuration - super.getSettingAsInt(settings, "max_duration", 5000, 1, Integer.MAX_VALUE);
		if(maxDuration < 1)
			maxDuration = 1;

		Scheduler.scheduleTask(new Task("Wait For It", RewardsUtil.rand.nextInt(maxDuration) + minDuration)
		{
			@Override
			public void callback()
			{
				int reward = RewardsUtil.rand.nextInt(3);
				player.sendMessage(new TextComponentString("NOW!"));

				if(reward == 0)
				{
					world.spawnEntity(new EntityTNTPrimed(world, player.posX, player.posY + 1, player.posZ, null));
				}
				else if(reward == 1)
				{
					EntityCreeper ent = new EntityCreeper(world);
					ent.setLocationAndAngles(player.posX, player.posY + 1, player.posZ, 0, 0);
					ent.onStruckByLightning(null);
					world.spawnEntity(ent);
				}
				else if(reward == 2)
				{
					RewardsUtil.placeBlock(Blocks.BEDROCK.getDefaultState(), world, new BlockPos(player.posX, player.posY, player.posZ));
				}
				else if(reward == 3)
				{
					RewardsUtil.placeBlock(Blocks.EMERALD_ORE.getDefaultState(), world, new BlockPos(player.posX, player.posY, player.posZ));
				}
				else if(reward == 4)
				{
					EntityZombie zomb = new EntityZombie(world);
					zomb.setChild(true);
					zomb.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100000, 0));
					zomb.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100000, 0));
					world.spawnEntity(zomb);
				}
			}
		});
	}
}
