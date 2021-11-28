package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.mcwrapper.EntityWrapper;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class WaitForItReward extends BaseCustomReward
{
	public WaitForItReward()
	{
		super(CCubesCore.MODID + ":wait_for_it", -30);
	}

	@Override
	public void trigger(final ServerLevel level, BlockPos pos, final Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToPlayer(player, "Wait for it.......");

		int minDuration = super.getSettingAsInt(settings, "minDuration", 1000, 0, Integer.MAX_VALUE - 1);
		int maxDuration = minDuration - super.getSettingAsInt(settings, "maxDuration", 5000, 1, Integer.MAX_VALUE);
		if(maxDuration < 1)
			maxDuration = 1;
		//Because someone will do it...
		if(minDuration > maxDuration)
		{
			int swap = minDuration;
			minDuration = maxDuration;
			maxDuration = swap;
		}

		Scheduler.scheduleTask(new Task("Wait For It", RewardsUtil.rand.nextInt(maxDuration) + minDuration)
		{
			@Override
			public void callback()
			{
				int reward = RewardsUtil.rand.nextInt(5);
				RewardsUtil.sendMessageToPlayer(player, "NOW!");

				if(reward == 0)
				{
					level.addFreshEntity(new PrimedTnt(level, player.getX(), player.getY() + 1, player.getZ(), null));
				}
				else if(reward == 1)
				{
					Creeper ent = EntityType.CREEPER.create(level);
					ent.moveTo(player.getX(), player.getY() + 1, player.getZ(), 0, 0);
					EntityWrapper.setCreeperPowered(ent);
					level.addFreshEntity(ent);
				}
				else if(reward == 2)
				{
					RewardsUtil.placeBlock(Blocks.BEDROCK.defaultBlockState(), level, new BlockPos(player.getX(), player.getY(), player.getZ()));
				}
				else if(reward == 3)
				{
					RewardsUtil.placeBlock(Blocks.EMERALD_ORE.defaultBlockState(), level, new BlockPos(player.getX(), player.getY(), player.getZ()));
				}
				else
				{
					Zombie zomb = EntityType.ZOMBIE.create(level);
					zomb.setBaby(true);
					zomb.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100000, 0));
					zomb.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100000, 0));
					level.addFreshEntity(zomb);
				}
			}
		});
	}
}
