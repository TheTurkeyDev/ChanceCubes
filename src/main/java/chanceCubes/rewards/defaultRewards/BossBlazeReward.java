package chanceCubes.rewards.defaultRewards;

import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BossBlazeReward extends BossBaseReward
{
	public BossBlazeReward()
	{
		super("demonic_blaze");
	}

	@Override
	public LivingEntity initBoss(ServerLevel level, BlockPos pos, Player player, JsonObject settings, BattleWrapper battleWrapper)
	{
		Blaze blaze = EntityType.BLAZE.create(level);
		blaze.setCustomName(new TextComponent("Demonic Blaze"));

		Scheduler.scheduleTask(new Task("blaze_abilities", -1, 20)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				if(!blaze.isAlive())
				{
					Scheduler.removeTask(this);
					return;
				}

				if(RewardsUtil.rand.nextInt(10) == 4)
					goInvisible(blaze);
				if(RewardsUtil.rand.nextInt(10) == 4)
					setGroundOnFire(level, player.getOnPos());
				if(RewardsUtil.rand.nextInt(3) == 1)
					shootFireballs(level, blaze, player);
			}
		});
		return blaze;
	}

	private void goInvisible(Blaze blaze)
	{
		blaze.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 5));
	}

	private void setGroundOnFire(Level level, BlockPos playerPos)
	{
		for(int xOff = -1; xOff <= 1; xOff++)
		{
			for(int zOff = -1; zOff <= 1; zOff++)
			{
				BlockPos offPos = playerPos.offset(xOff, 0, zOff);
				RewardsUtil.placeBlock(Blocks.FIRE.defaultBlockState(), level, offPos);
			}
		}
	}

	private void shootFireballs(Level level, Blaze blaze, Player player)
	{
		double d1 = player.getX() - blaze.getX();
		double d2 = player.getBoundingBox().minY + (double) (player.getEyeHeight() / 2.0F) - (blaze.getY() + (double) (blaze.getEyeHeight() / 2.0F));
		double d3 = player.getZ() - blaze.getZ();
		for(int i = 0; i < 5; i++)
		{
			SmallFireball entitysmallfireball = new SmallFireball(level, blaze, d1 + RewardsUtil.rand.nextGaussian(), d2, d3 + RewardsUtil.rand.nextGaussian());
			entitysmallfireball.getOnPos().offset(0, blaze.getY() + (double) (blaze.getEyeHeight() / 2.0F) + 0.5D, 0);
			level.addFreshEntity(entitysmallfireball);
		}
	}

	@Override
	public void onBossFightEnd(ServerLevel world, BlockPos pos, Player player)
	{

	}
}
