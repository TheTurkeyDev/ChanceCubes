package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BossWitchReward extends BossBaseReward
{

	//@formatter:off
	private List<Class<? extends Entity>> entities = Arrays.asList(EntityCreeper.class, EntitySkeleton.class, EntityBlaze.class,
			EntityEnderman.class, EntityEndermite.class, EntityPigZombie.class, EntitySilverfish.class, EntitySlime.class,
			EntitySpider.class, EntityZombie.class);
	//@formatter:on
	public BossWitchReward()
	{
		super("Evil_Witch");
	}

	@Override
	public void spawnBoss(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		EntityWitch witch = new EntityWitch(world);
		witch.setCustomNameTag("Evil Witch");
		witch.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		witch.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player, settings));
		witch.setHealth(witch.getMaxHealth());

		ItemStack stack = new ItemStack(Items.LEATHER_HELMET);
		stack.addEnchantment(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemStackToSlot(EntityEquipmentSlot.HEAD, stack);
		witch.setDropChance(EntityEquipmentSlot.HEAD, 0);

		stack = new ItemStack(Items.LEATHER_CHESTPLATE);
		stack.addEnchantment(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemStackToSlot(EntityEquipmentSlot.CHEST, stack);
		witch.setDropChance(EntityEquipmentSlot.CHEST, 0);

		stack = new ItemStack(Items.LEATHER_LEGGINGS);
		stack.addEnchantment(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemStackToSlot(EntityEquipmentSlot.LEGS, stack);
		witch.setDropChance(EntityEquipmentSlot.LEGS, 0);

		stack = new ItemStack(Items.LEATHER_BOOTS);
		stack.addEnchantment(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemStackToSlot(EntityEquipmentSlot.FEET, stack);
		witch.setDropChance(EntityEquipmentSlot.FEET, 0);

		spawnMinoins(pos, world);

		Scheduler.scheduleTask(new Task("witch_abilities", -1, 20)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				if(witch.isDead)
				{
					Scheduler.removeTask(this);
					return;
				}

				if(RewardsUtil.rand.nextInt(15) == 4)
					spawnMinoins(witch.getPosition(), world);
				if(RewardsUtil.rand.nextInt(10) == 4)
					lightningStrike(player.getPosition(), world);
				if(RewardsUtil.rand.nextInt(5) == 4)
					throwPotion(witch, player.getPosition(), world);
			}
		});


		world.spawnEntity(witch);
		super.trackEntities(witch);
		super.trackedPlayers(player);
	}

	private void lightningStrike(BlockPos playerPos, World world)
	{
		world.addWeatherEffect(new EntityLightningBolt(world, playerPos.getX(), playerPos.getY(), playerPos.getZ(), false));
	}

	private void throwPotion(EntityWitch witch, BlockPos playerPos, World world)
	{
		PotionType potionType = PotionType.REGISTRY.getObjectById(RewardsUtil.rand.nextInt(PotionType.REGISTRY.getKeys().size()));
		EntityPotion pot = new EntityPotion(world, witch, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
		double d0 = playerPos.getY() + 0.5;
		double d1 = playerPos.getX() - witch.posX;
		double d2 = d0 - pot.posY;
		double d3 = playerPos.getZ() - witch.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
		pot.shoot(d1, d2 + (double) f, d3, 1.6F, 12.0F);
		world.spawnEntity(pot);
	}

	private void spawnMinoins(BlockPos pos, World world)
	{
		for(EnumFacing facing : EnumFacing.values())
		{
			if(facing == EnumFacing.UP || facing == EnumFacing.DOWN)
				continue;

			try
			{
				Entity ent = entities.get(RewardsUtil.rand.nextInt(entities.size())).getConstructor(World.class).newInstance(world);
				BlockPos adjPos = pos.offset(facing);
				ent.setPosition(adjPos.getX(), adjPos.getY(), adjPos.getZ());
				world.spawnEntity(ent);
				trackSubEntities(ent);
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, "Uh oh! Something went wrong and the minions could not be spawned! Please report this to the mod dev!");
				return;
			}
		}
	}

	@Override
	public void onBossFightEnd(World world, BlockPos pos, EntityPlayer player)
	{

	}
}
