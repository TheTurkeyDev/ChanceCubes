package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.mcwrapper.ComponentWrapper;
import chanceCubes.mcwrapper.EntityWrapper;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantments;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BossWitchReward extends BossBaseReward
{

	//@formatter:off
	private final List<String> entities = Arrays.asList("creeper", "skeleton", "blaze",  "enderman", "endermite",
			"zombie_pigman",  "silverfish", "slime",  "spider", "zombie");
	//@formatter:on
	public BossWitchReward()
	{
		super("evil_witch");
	}

	@Override
	public LivingEntity initBoss(ServerLevel level, BlockPos pos, Player player, JsonObject settings, BattleWrapper battleWrapper)
	{
		Witch witch = EntityType.WITCH.create(level);
		witch.setCustomName(ComponentWrapper.string("Evil Witch"));

		ItemStack stack = new ItemStack(Items.LEATHER_HELMET);
		stack.enchant(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemSlot(EquipmentSlot.HEAD, stack);
		witch.setDropChance(EquipmentSlot.HEAD, 0);

		stack = new ItemStack(Items.LEATHER_CHESTPLATE);
		stack.enchant(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemSlot(EquipmentSlot.CHEST, stack);
		witch.setDropChance(EquipmentSlot.CHEST, 0);

		stack = new ItemStack(Items.LEATHER_LEGGINGS);
		stack.enchant(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemSlot(EquipmentSlot.LEGS, stack);
		witch.setDropChance(EquipmentSlot.LEGS, 0);

		stack = new ItemStack(Items.LEATHER_BOOTS);
		stack.enchant(Enchantments.BLAST_PROTECTION, 5);
		witch.setItemSlot(EquipmentSlot.FEET, stack);
		witch.setDropChance(EquipmentSlot.FEET, 0);

		spawnMinoins(pos, level, battleWrapper);

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
					spawnMinoins(witch.getOnPos(), level, battleWrapper);
				if(RewardsUtil.rand.nextInt(10) == 4)
					EntityWrapper.spawnLightning(level, player.getOnPos());
				if(RewardsUtil.rand.nextInt(5) == 4)
					throwPotion(witch, player.getOnPos(), level);
			}
		});

		return witch;
	}

	private void throwPotion(Witch witch, BlockPos playerPos, ServerLevel level)
	{
		ThrownPotion pot = new ThrownPotion(level, witch);
		MobEffectInstance potionEffect = RewardsUtil.getRandomPotionEffectInstance();
		pot.setItem(PotionUtils.setCustomEffects(new ItemStack(Items.SPLASH_POTION), List.of(potionEffect)));
		double d0 = playerPos.getY() + 0.5;
		double d1 = playerPos.getX() - witch.getX();
		double d2 = d0 - pot.getY();
		double d3 = playerPos.getZ() - witch.getZ();
		float f = (float) (Math.sqrt(d1 * d1 + d3 * d3) * 0.2F);
		pot.shoot(d1, d2 + (double) f, d3, 1.6F, 12.0F);
		level.addFreshEntity(pot);
	}

	private void spawnMinoins(BlockPos pos, ServerLevel level, BattleWrapper battleWrapper)
	{
		for(Direction facing : Direction.values())
		{
			if(facing == Direction.UP || facing == Direction.DOWN)
				continue;

			try
			{
				Optional<EntityType<?>> entType = EntityType.byString(entities.get(RewardsUtil.rand.nextInt(entities.size())));
				Entity ent;
				if(entType.isPresent())
					ent = entType.get().create(level);
				else
					ent = EntityType.CREEPER.create(level);
				BlockPos adjPos = pos.relative(facing, 1);
				ent.moveTo(adjPos.getX(), adjPos.getY(), adjPos.getZ());
				level.addFreshEntity(ent);
				trackSubEntities(battleWrapper, ent);
			} catch(Exception e)
			{
				CCubesCore.logger.log(Level.ERROR, "Uh oh! Something went wrong and the minions could not be spawned! Please report this to the mod dev!");
				return;
			}
		}
	}

	@Override
	public void onBossFightEnd(ServerLevel world, BlockPos pos, Player player)
	{

	}
}
