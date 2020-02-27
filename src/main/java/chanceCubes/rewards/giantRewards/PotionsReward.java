package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Map;

public class PotionsReward extends BaseCustomReward
{
	private PotionEntity pot;

	public PotionsReward()
	{
		super(CCubesCore.MODID + ":raining_potions", 0);
	}

	@Override
	public void trigger(final World world, final BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		player.sendMessage(new TranslationTextComponent("chancecubes.reward.raining_potions"));
		throwPoitonCircle(world, pos, player);
	}

	private void throwPoitonCircle(final World world, final BlockPos pos, final PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("Potion Circle", 100, 20)
		{
			@Override
			public void callback()
			{
				throwPoiton(world, pos, player);
			}

			@Override
			public void update()
			{
				for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 20))
				{
					Potion potionType = RewardsUtil.getRandomPotionType();
					pot = new PotionEntity(world, player);
					pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.setMotion(Math.cos(rad) * (0.1), 1, Math.sin(rad) * (0.1));
					world.addEntity(pot);
				}
			}
		});
	}

	private void throwPoiton(final World world, final BlockPos pos, final PlayerEntity player)
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
					Potion potionType = RewardsUtil.getRandomPotionType();
					pot = new PotionEntity(world, player);
					pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.setMotion(Math.cos((this.delayLeft / 2f) * (Math.PI / 30)), yy, Math.sin((this.delayLeft / 2f) * (Math.PI / 30)));
					world.addEntity(pot);
				}
			}
		});
	}
}