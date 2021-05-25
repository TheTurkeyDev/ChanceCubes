package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BioDomeGen;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class BossBaseReward extends BaseCustomReward
{
	private String bossName;

	public BossBaseReward(String bossName)
	{
		super(CCubesCore.MODID + ":boss_" + bossName, -35);
		this.bossName = bossName.replace("_", " ");
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		BattleWrapper battleWrapper = new BattleWrapper();

		battleWrapper.rewardCenterPos = pos;
		battleWrapper.domeGen = new BioDomeGen(player);
		battleWrapper.domeGen.genRandomDome(pos.add(0, -1, 0), world, 15, false);
		StringTextComponent message = new StringTextComponent("BOSS FIGHT!");
		message.setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED)));
		RewardsUtil.setNearPlayersTitle(world, pos, 50, STitlePacket.Type.TITLE, message, 10, 500, 0);

		Scheduler.scheduleTask(new Task("boss_fight_subtitle_1", 120)
		{
			@Override
			public void callback()
			{
				StringTextComponent message = new StringTextComponent("");
				message.append(player.getDisplayName());

				StringBuilder sbSpace = new StringBuilder();
				sbSpace.append(" VS ");
				for(int i = 0; i < bossName.length(); i++)
					sbSpace.append(" ");
				message.appendString(sbSpace.toString());

				message.setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED)));
				RewardsUtil.setNearPlayersTitle(world, pos, 50, STitlePacket.Type.SUBTITLE, message, 0, 500, 0);
			}
		});

		Scheduler.scheduleTask(new Task("boss_fight_subtitle_2", 160)
		{
			@Override
			public void callback()
			{
				StringTextComponent message = new StringTextComponent("");
				message.append(player.getDisplayName());
				message.appendString(" VS ");
				message.appendString(bossName);
				message.setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED)));
				RewardsUtil.setNearPlayersTitle(world, pos, 50, STitlePacket.Type.SUBTITLE, message, 0, 100, 10);
			}
		});

		Scheduler.scheduleTask(new Task("boss_fight_start", 200)
		{
			@Override
			public void callback()
			{
				startBossFight(world, pos, player, settings, battleWrapper);
			}
		});
	}

	public void startBossFight(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings, BattleWrapper battleWrapper)
	{
		LivingEntity ent = initBoss(world, pos, player, settings, battleWrapper);
		ent.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		ent.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player, settings));
		ent.setHealth(ent.getMaxHealth());
//		CustomServerBossInfoManager customserverbossinfomanager = world.getServer().getCustomBossEvents();
//		customserverbossinfomanager.add(new ResourceLocation(CCubesCore.MODID, this.getName()), ent.getCustomName());
//		BossBarCommand

		world.addEntity(ent);
		trackEntities(battleWrapper, ent);
		trackPlayers(battleWrapper, player);
		Scheduler.scheduleTask(new Task("boss_fight_tracker", -1, 5)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				//System.out.println(trackedEntities.size());
				//System.out.println(trackedPlayers.size());
				for(int i = battleWrapper.trackedEntities.size() - 1; i >= 0; i--)
				{
					Entity ent = battleWrapper.trackedEntities.get(i);
					if(!ent.isAlive() && (ent.ticksExisted > 0 || world.getDifficulty().equals(Difficulty.PEACEFUL)))
					{
						battleWrapper.trackedEntities.remove(i);
						if(battleWrapper.trackedEntities.isEmpty())
						{
							endBossfight(true, world, pos, player, battleWrapper);
							Scheduler.removeTask(this);
						}
					}
				}

				for(int i = battleWrapper.trackedPlayers.size() - 1; i >= 0; i--)
				{
					Entity ent = battleWrapper.trackedPlayers.get(i);
					if(ent.getDistanceSq(battleWrapper.rewardCenterPos.getX(), battleWrapper.rewardCenterPos.getY(), battleWrapper.rewardCenterPos.getZ()) > 15 * 15 || ent.getPosY() < battleWrapper.rewardCenterPos.getY() - 1)
						ent.setPositionAndUpdate(battleWrapper.rewardCenterPos.getX(), battleWrapper.rewardCenterPos.getY() + 1, battleWrapper.rewardCenterPos.getZ());

					if(!ent.isAlive() && (ent.ticksExisted > 0 || world.getDifficulty().equals(Difficulty.PEACEFUL)))
					{
						for(Entity entity : battleWrapper.trackedEntities)
							entity.remove();
						battleWrapper.trackedEntities.clear();
						endBossfight(false, world, pos, player, battleWrapper);
						Scheduler.removeTask(this);
						return;
					}
				}
			}
		});
	}

	public void endBossfight(boolean resetPlayer, ServerWorld world, BlockPos pos, PlayerEntity player, BattleWrapper battleWrapper)
	{
		for(Entity ent : battleWrapper.trackedSubEntities)
			if(ent.isAlive())
				ent.remove();
		battleWrapper.trackedSubEntities.clear();
		onBossFightEnd(world, pos, player);
		battleWrapper.domeGen.removeDome(resetPlayer);
	}

	protected void trackEntities(BattleWrapper battleWrapper, Entity... ents)
	{
		battleWrapper.trackedEntities.addAll(Arrays.asList(ents));
	}

	protected void trackSubEntities(BattleWrapper battleWrapper, Entity... ents)
	{
		battleWrapper.trackedSubEntities.addAll(Arrays.asList(ents));
	}

	protected void trackPlayers(BattleWrapper battleWrapper, PlayerEntity... player)
	{
		battleWrapper.trackedPlayers.addAll(Arrays.asList(player));
	}

	public abstract LivingEntity initBoss(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings, BattleWrapper battleWrapper);

	public abstract void onBossFightEnd(ServerWorld world, BlockPos pos, PlayerEntity player);

	public double getBossHealthDynamic(PlayerEntity player, JsonObject settings)
	{
		double maxDamage = 3;
		for(ItemStack stack : player.inventory.mainInventory)
		{
			Multimap<Attribute, AttributeModifier> atributes = stack.getItem().getAttributeModifiers(EquipmentSlotType.MAINHAND, stack);
			if(atributes.containsKey(Attributes.ATTACK_DAMAGE))
			{
				Collection<AttributeModifier> damageList = atributes.get(Attributes.ATTACK_DAMAGE);
				for(AttributeModifier damage : damageList)
					if(maxDamage < damage.getAmount())
						maxDamage = damage.getAmount();
			}
		}

		double prePofileHealth = maxDamage * 15.0;

		double profileMult = super.getSettingAsDouble(settings, "bossHealthMultiplier", 1.0, 0.0, 10.0);

		return prePofileHealth * profileMult;
	}

	public ItemStack getHighestDamageItem(PlayerEntity player)
	{
		double maxDamage = -1;
		ItemStack maxItem = ItemStack.EMPTY;
		for(ItemStack stack : player.inventory.mainInventory)
		{
			Multimap<Attribute, AttributeModifier> atributes = stack.getItem().getAttributeModifiers(EquipmentSlotType.MAINHAND, stack);
			if(atributes.containsKey(Attributes.ATTACK_DAMAGE))
			{
				Collection<AttributeModifier> damageList = atributes.get(Attributes.ATTACK_DAMAGE);
				for(AttributeModifier damage : damageList)
				{
					if(maxDamage < damage.getAmount())
					{
						maxDamage = damage.getAmount();
						maxItem = stack;
					}
				}
			}
		}
		return maxItem;
	}

	protected static class BattleWrapper
	{
		public List<Entity> trackedEntities = new ArrayList<>();
		public List<Entity> trackedSubEntities = new ArrayList<>();
		public List<PlayerEntity> trackedPlayers = new ArrayList<>();

		public BlockPos rewardCenterPos;
		public BioDomeGen domeGen;
	}
}
