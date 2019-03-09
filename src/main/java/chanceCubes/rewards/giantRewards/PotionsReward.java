package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class PotionsReward extends BaseCustomReward
{
	private EntityPotion pot;
	
	public PotionsReward()
	{
		super(CCubesCore.MODID + ":Raining_Potions", 0);
	}

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player)
	{
		player.sendMessage(new TextComponentTranslation("chancecubes.reward.raining_potions"));
		throwPoitonCircle(0, world, pos, player);
	}

	private void throwPoitonCircle(final int itteration, final World world, final BlockPos pos, final EntityPlayer player)
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
					PotionType potionType = PotionType.REGISTRY.getObjectById(RewardsUtil.rand.nextInt(PotionType.REGISTRY.getKeys().size()));
					pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.motionX = Math.cos(rad) * (0.1 + (0.05 * itteration));
					pot.motionY = 1;
					pot.motionZ = Math.sin(rad) * (0.1 + (0.05 * itteration));
					world.spawnEntity(pot);
				}
			}
		});
	}

	private void throwPoiton(final World world, final BlockPos pos, final EntityPlayer player)
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
					PotionType potionType = PotionType.REGISTRY.getObjectById(RewardsUtil.rand.nextInt(PotionType.REGISTRY.getKeys().size()));
					pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.motionX = Math.cos((this.delayLeft / 2) * (Math.PI / 30));
					pot.motionY = yy;
					pot.motionZ = Math.sin((this.delayLeft / 2) * (Math.PI / 30));
					world.spawnEntity(pot);
				}
			}
		});
	}
}