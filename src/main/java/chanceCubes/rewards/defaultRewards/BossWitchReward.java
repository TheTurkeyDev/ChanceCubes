package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BossWitchReward extends BossBaseReward
{

	//@formatter:off
	private List<String> entities = Arrays.asList("creeper", "skeleton", "blaze",  "enderman", "endermite",
			"zombie_pigman",  "silverfish", "slime",  "spider", "zombie");
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
		witch.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player, settings));
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
		LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
		lightningboltentity.moveForced(Vector3d.copyCenteredHorizontally(playerPos));
		lightningboltentity.setEffectOnly(false);
		world.addEntity(lightningboltentity);
	}

	private void throwPotion(WitchEntity witch, BlockPos playerPos, World world)
	{
		PotionEntity pot = new PotionEntity(world, witch);
		pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), RewardsUtil.getRandomPotionType()));
		double d0 = playerPos.getY() + 0.5;
		double d1 = playerPos.getX() - witch.getPosX();
		double d2 = d0 - pot.getPosY();
		double d3 = playerPos.getZ() - witch.getPosZ();
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
				Optional<EntityType<?>> entType = EntityType.byKey(entities.get(RewardsUtil.rand.nextInt(entities.size())));
				Entity ent;
				if(entType.isPresent())
					ent = entType.get().create(world);
				else
					ent = EntityType.CREEPER.create(world);
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
