package chanceCubes.rewards.defaultRewards;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Multimap;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;

public class MobEffectsReward extends BaseCustomReward
{
	public MobEffectsReward()
	{
		super(CCubesCore.MODID + ":Mob_Abilities_Effects", -15);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		int highestDamage = 1;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = player.inventory.mainInventory.get(i);
			if(!stack.isEmpty())
			{
				Multimap<String, AttributeModifier> modifiers = stack.getItem().getAttributeModifiers(EquipmentSlotType.MAINHAND, stack);
				Collection<AttributeModifier> modiferCollect = modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
				if(modiferCollect.size() > 0)
					for(AttributeModifier moddifer : modiferCollect)
						if(highestDamage < moddifer.getAmount())
							highestDamage = (int) moddifer.getAmount();
			}
		}

		LivingEntity ent;
		switch(RewardsUtil.rand.nextInt(3))
		{
			case 0:
				ent = EntityType.ZOMBIE.create(world);
				break;
			case 1:
				ent = EntityType.CREEPER.create(world);
				break;
			case 2:
				ent = EntityType.SKELETON.create(world);
				break;
			default:
				ent = EntityType.ZOMBIE.create(world);
				break;
		}

		ent.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		ent.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(highestDamage * 30);
		ent.setHealth(highestDamage * 30);
		ent.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 6000, 2));
		world.addEntity(ent);

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
				if(!ent.isAlive())
					Scheduler.removeTask(this);

				BlockPos pos = ent.getPosition();
				PotionEntity pot;
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
							Potion potionType = RewardsUtil.getRandomPotionType();
							pot = new PotionEntity(world, player);
							pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
							pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
							pot.setMotion(Math.cos(rad) * (0.1 + (0.05 * 2)), 1, Math.sin(rad) * (0.1 + (0.05 * 2)));
							world.addEntity(pot);
						}
						break;
					case 2:
						delay--;
						if(delay > 0)
							return;
						delay = 8;
						BlockPos posShift = pos.add((RewardsUtil.rand.nextInt(9) - 4), 0, (RewardsUtil.rand.nextInt(9) - 4));
						((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, posShift.getX(), posShift.getY(), posShift.getZ(), false));
						break;
					case 3:
						delay--;
						if(delay > 0)
							return;
						delay = 8;
						ArrowEntity entitysnowball = new ArrowEntity(world, ent);
						double d0 = player.posY + (double) player.getEyeHeight() - 1.100000023841858D;
						double d1 = player.posX - ent.posX;
						double d2 = d0 - entitysnowball.posY;
						double d3 = player.posZ - ent.posZ;
						float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
						entitysnowball.shoot(d1, d2 + (double) f, d3, 1.6F, 12.0F);
						player.playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 1.0F / (ent.getRNG().nextFloat() * 0.4F + 0.8F));
						world.addEntity(entitysnowball);
						break;
					case 4:
						delay--;
						if(delay > 0)
							return;
						delay = 10;
						Potion potionType = RewardsUtil.getRandomPotionType();
						pot = new PotionEntity(world, player);
						pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
						pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
						pot.setMotion(-0.1, 1, -0.1);
						world.addEntity(pot);
						break;
					default:
						world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
						break;
				}
			}

		});
	}
}
