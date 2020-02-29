package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BossWitchReward extends BossBaseReward
{

	//@formatter:off
	private List<Class<? extends Entity>> entities = Arrays.asList(CreeperEntity.class, SkeletonEntity.class, BlazeEntity.class,
			EndermanEntity.class, EndermiteEntity.class, ZombiePigmanEntity.class, SilverfishEntity.class, SlimeEntity.class,
			SpiderEntity.class, ZombieEntity.class);
	//@formatter:on
	public BossWitchReward()
	{
		super("evil_witch");
	}

	@Override
	public void spawnBoss(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		WitchEntity witch = EntityType.WITCH.create(world);
		witch.setCustomName(new StringTextComponent("Evil Witch"));
		witch.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		witch.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player, settings));
		witch.setHealth(witch.getMaxHealth());

		ItemStack stack = new ItemStack(Items.LEATHER_HELMET);
		stack.addEnchantment(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemStackToSlot(EquipmentSlotType.HEAD, stack);
		witch.setDropChance(EquipmentSlotType.HEAD, 0);

		stack = new ItemStack(Items.LEATHER_CHESTPLATE);
		stack.addEnchantment(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemStackToSlot(EquipmentSlotType.CHEST, stack);
		witch.setDropChance(EquipmentSlotType.CHEST, 0);

		stack = new ItemStack(Items.LEATHER_LEGGINGS);
		stack.addEnchantment(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemStackToSlot(EquipmentSlotType.LEGS, stack);
		witch.setDropChance(EquipmentSlotType.LEGS, 0);

		stack = new ItemStack(Items.LEATHER_BOOTS);
		stack.addEnchantment(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemStackToSlot(EquipmentSlotType.FEET, stack);
		witch.setDropChance(EquipmentSlotType.FEET, 0);

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
				if(!witch.isAlive())
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


		world.addEntity(witch);
		super.trackEntities(witch);
		super.trackedPlayers(player);
	}

	private void lightningStrike(BlockPos playerPos, World world)
	{
		((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, playerPos.getX(), playerPos.getY(), playerPos.getZ(), false));
	}

	private void throwPotion(WitchEntity witch, BlockPos playerPos, World world)
	{
		PotionEntity pot = new PotionEntity(world, witch);
		pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), RewardsUtil.getRandomPotionType()));
		double d0 = playerPos.getY() + 0.5;
		double d1 = playerPos.getX() - witch.posX;
		double d2 = d0 - pot.posY;
		double d3 = playerPos.getZ() - witch.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
		pot.shoot(d1, d2 + (double) f, d3, 1.6F, 12.0F);
		world.addEntity(pot);
	}

	private void spawnMinoins(BlockPos pos, World world)
	{
		for(Direction facing : Direction.values())
		{
			if(facing == Direction.UP || facing == Direction.DOWN)
				continue;

			try
			{
				Entity ent = entities.get(RewardsUtil.rand.nextInt(entities.size())).getConstructor(World.class).newInstance(world);
				BlockPos adjPos = pos.offset(facing);
				ent.setPosition(adjPos.getX(), adjPos.getY(), adjPos.getZ());
				world.addEntity(ent);
				trackSubEntities(ent);
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, "Uh oh! Something went wrong and the minions could not be spawned! Please report this to the mod dev!");
				return;
			}
		}
	}

	@Override
	public void onBossFightEnd(World world, BlockPos pos, PlayerEntity player)
	{

	}
}
