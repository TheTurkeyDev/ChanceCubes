package chanceCubes.rewards.defaultRewards;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Multimap;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MobEffectsReward extends BaseCustomReward
{
	public MobEffectsReward()
	{
		super(CCubesCore.MODID + ":mob_abilities_effects", -15);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		int highestDamage = 1;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = player.inventory.mainInventory.get(i);
			if(!stack.isEmpty())
			{
				Multimap<String, AttributeModifier> modifiers = stack.getItem().getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack);
				Collection<AttributeModifier> modiferCollect = modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
				if(modiferCollect.size() > 0)
					for(AttributeModifier moddifer : modiferCollect)
						if(highestDamage < moddifer.getAmount())
							highestDamage = (int) moddifer.getAmount();
			}
		}

		EntityLivingBase ent;
		switch(RewardsUtil.rand.nextInt(3))
		{
			case 0:
				ent = new EntityZombie(world);
				break;
			case 1:
				ent = new EntityCreeper(world);
				break;
			case 2:
				ent = new EntitySkeleton(world);
				break;
			default:
				ent = new EntityZombie(world);
				break;
		}

		ent.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		ent.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(highestDamage * 30);
		ent.setHealth(highestDamage * 30);
		ent.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 2));
		world.spawnEntity(ent);

		Scheduler.scheduleTask(new Task("Mob_Effects_Reward_Task", 6000, 5)
		{
			int effect = RewardsUtil.rand.nextInt(5);
			int delay = 0;

			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				if(ent.isDead)
					Scheduler.removeTask(this);

				BlockPos pos = ent.getPosition();
				EntityPotion pot;
				switch(effect)
				{
					case 0:
						world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
						break;
					case 1:
						delay--;
						if(delay > 0)
							return;
						delay = 6;

						for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 5))
						{
							PotionType potionType = PotionType.REGISTRY.getObjectById(RewardsUtil.rand.nextInt(PotionType.REGISTRY.getKeys().size()));
							pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
							pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
							pot.motionX = Math.cos(rad) * (0.1 + (0.05 * 2));
							pot.motionY = 1;
							pot.motionZ = Math.sin(rad) * (0.1 + (0.05 * 2));
							world.spawnEntity(pot);
						}
						break;
					case 2:
						delay--;
						if(delay > 0)
							return;
						delay = 8;
						BlockPos posShift = pos.add((RewardsUtil.rand.nextInt(9) - 4), 0, (RewardsUtil.rand.nextInt(9) - 4));
						world.addWeatherEffect(new EntityLightningBolt(world, posShift.getX(), posShift.getY(), posShift.getZ(), false));
						break;
					case 3:
						delay--;
						if(delay > 0)
							return;
						delay = 8;
						EntityArrow entitysnowball = new EntityTippedArrow(world, ent);
						double d0 = player.posY + (double) player.getEyeHeight() - 1.100000023841858D;
						double d1 = player.posX - ent.posX;
						double d2 = d0 - entitysnowball.posY;
						double d3 = player.posZ - ent.posZ;
						float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
						entitysnowball.shoot(d1, d2 + (double) f, d3, 1.6F, 12.0F);
						player.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (ent.getRNG().nextFloat() * 0.4F + 0.8F));
						world.spawnEntity(entitysnowball);
						break;
					case 4:
						delay--;
						if(delay > 0)
							return;
						delay = 10;
						PotionType potionType = PotionType.REGISTRY.getObjectById(RewardsUtil.rand.nextInt(PotionType.REGISTRY.getKeys().size()));
						pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), potionType));
						pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
						pot.motionX = -0.1;
						pot.motionY = 1;
						pot.motionZ = -.1;
						world.spawnEntity(pot);
						break;
					default:
						world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
						break;
				}
			}

		});
	}
}
