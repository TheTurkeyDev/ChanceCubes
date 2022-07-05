package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.mcwrapper.ComponentWrapper;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.List;

public class PotionsReward extends BaseCustomReward
{
	private ThrownPotion pot;

	public PotionsReward()
	{
		super(CCubesCore.MODID + ":raining_potions", 0);
	}

	@Override
	public void trigger(final ServerLevel level, final BlockPos pos, final Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToPlayer(player, ComponentWrapper.translatable("chancecubes.reward.raining_potions"));
		throwPoitonCircle(level, pos, player);
	}

	private void throwPoitonCircle(final ServerLevel level, final BlockPos pos, final Player player)
	{
		Scheduler.scheduleTask(new Task("Potion Circle", 100, 20)
		{
			int tick = 0;

			@Override
			public void callback()
			{
				throwPoiton(level, pos, player);
			}

			@Override
			public void update()
			{
				tick++;
				for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 20))
				{
					MobEffectInstance potionEffect = RewardsUtil.getRandomPotionEffectInstance();
					pot = new ThrownPotion(level, player);
					pot.setItem(PotionUtils.setCustomEffects(new ItemStack(Items.SPLASH_POTION), List.of(potionEffect)));
					pot.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.setDeltaMovement(Math.cos(rad) * (0.1 * tick), 1, Math.sin(rad) * (0.1 * tick));
					level.addFreshEntity(pot);
				}
			}
		});
	}

	private void throwPoiton(final ServerLevel level, final BlockPos pos, final Player player)
	{
		Scheduler.scheduleTask(new Task("Throw potion", 400, 2)
		{
			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				for(double yy = -0.2; yy <= 1; yy += 0.1)
				{
					MobEffectInstance potionEffect = RewardsUtil.getRandomPotionEffectInstance();
					pot = new ThrownPotion(level, player);
					pot.setItem(PotionUtils.setCustomEffects(new ItemStack(Items.SPLASH_POTION), List.of(potionEffect)));
					pot.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.setDeltaMovement(Math.cos((this.delayLeft / 2f) * (Math.PI / 30)), yy, Math.sin((this.delayLeft / 2f) * (Math.PI / 30)));
					level.addFreshEntity(pot);
				}
			}
		});
	}
}