package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;

public class CakeIsALieReward extends BaseCustomReward
{
	public CakeIsALieReward()
	{
		super(CCubesCore.MODID + ":cake", 20);
	}

	@Override
	public void trigger(final ServerLevel level, final BlockPos pos, final Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToNearPlayers(level, pos, 32, "But is it a lie?");

		RewardsUtil.placeBlock(Blocks.CAKE.defaultBlockState(), level, pos);

		final int lieChance = super.getSettingAsInt(settings, "lieChance", 10, 0, 100);

		if(RewardsUtil.rand.nextInt(3) == 1)
		{
			Scheduler.scheduleTask(new Task("Cake_Is_A_Lie", 6000, 20)
			{
				@Override
				public void callback()
				{
					level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				}

				@Override
				public void update()
				{
					if(!level.getBlockState(pos).getBlock().equals(Blocks.CAKE))
					{
						Scheduler.removeTask(this);
					}
					else if(level.getBlockState(pos).getValue(CakeBlock.BITES) > 0)
					{
						level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
						RewardsUtil.sendMessageToNearPlayers(level, pos, 32, "It's a lie!!!");
						Creeper creeper = EntityType.CREEPER.create(level);
						creeper.moveTo(pos.getX(), pos.getY(), pos.getZ(), pos.getX() == 1 ? 90 : -90, 0);
						if(RewardsUtil.rand.nextInt(100) < lieChance)
							creeper.thunderHit(level, null);
						creeper.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 9999, 2));
						creeper.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 999));
						level.addFreshEntity(creeper);
						RewardsUtil.executeCommand(level, player, player.getOnPos(), "/advancement grant @p only chancecubes:its_a_lie");
						Scheduler.removeTask(this);
					}
				}
			});
		}
	}
}