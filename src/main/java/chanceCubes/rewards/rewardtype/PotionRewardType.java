package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.PotionPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.ArrayList;
import java.util.List;

public class PotionRewardType extends BaseRewardType<PotionPart>
{
	public PotionRewardType(PotionPart... effects)
	{
		super(effects);
	}

	@Override
	public void trigger(final PotionPart part, final ServerLevel level, final int x, final int y, final int z, final Player player)
	{
		Scheduler.scheduleTask(new Task("Potion Reward Delay", part.getDelay())
		{
			@Override
			public void callback()
			{
				ItemStack potion = new ItemStack(Items.SPLASH_POTION);

				List<MobEffectInstance> effects = new ArrayList<>();
				effects.add(part.getEffect());
				PotionUtils.setCustomEffects(potion, effects);

				ThrownPotion entity = new ThrownPotion(level, player);
				entity.setItem(PotionUtils.setCustomEffects(new ItemStack(Items.SPLASH_POTION), effects));
				entity.moveTo(player.getX(), player.getY() + 2, player.getZ());
				entity.setDeltaMovement(0, 0.1, 0);

				level.addFreshEntity(entity);
			}
		});
	}
}