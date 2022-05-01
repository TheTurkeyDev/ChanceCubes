package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BioDomeGen;
import chanceCubes.util.GuiTextLocation;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class BossBaseReward extends BaseCustomReward
{
	private final String bossName;

	public BossBaseReward(String bossName)
	{
		super(CCubesCore.MODID + ":boss_" + bossName, -35);
		this.bossName = bossName.replace("_", " ");
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		BattleWrapper battleWrapper = new BattleWrapper();

		battleWrapper.rewardCenterPos = pos;
		battleWrapper.domeGen = new BioDomeGen(player);
		battleWrapper.domeGen.genRandomDome(pos.offset(0, -1, 0), level, 15, false);
		TextComponent message = new TextComponent("BOSS FIGHT!");
		message.setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)));
		RewardsUtil.setNearPlayersTitle(level, pos, 50, GuiTextLocation.TITLE, message, 10, 500, 0);

		Scheduler.scheduleTask(new Task("boss_fight_subtitle_1", 120)
		{
			@Override
			public void callback()
			{
				TextComponent message = new TextComponent("");
				message.append(player.getDisplayName());
				message.append(" VS " + " ".repeat(bossName.length()));
				message.setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)));
				RewardsUtil.setNearPlayersTitle(level, pos, 50, GuiTextLocation.SUBTITLE, message, 0, 500, 0);
			}
		});

		Scheduler.scheduleTask(new Task("boss_fight_subtitle_2", 160)
		{
			@Override
			public void callback()
			{
				TextComponent message = new TextComponent("");
				message.append(player.getDisplayName());
				message.append(" VS ");
				message.append(bossName);
				message.setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)));
				RewardsUtil.setNearPlayersTitle(level, pos, 50, GuiTextLocation.SUBTITLE, message, 0, 100, 10);
			}
		});

		Scheduler.scheduleTask(new Task("boss_fight_start", 200)
		{
			@Override
			public void callback()
			{
				startBossFight(level, pos, player, settings, battleWrapper);
			}
		});
	}

	public void startBossFight(ServerLevel level, BlockPos pos, Player player, JsonObject settings, BattleWrapper battleWrapper)
	{
		LivingEntity ent = initBoss(level, pos, player, settings, battleWrapper);
		ent.moveTo(pos.getX(), pos.getY(), pos.getZ());
		ent.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player, settings));
		ent.setHealth(ent.getMaxHealth());
//		CustomServerBossInfoManager customserverbossinfomanager = level.getServer().getCustomBossEvents();
//		customserverbossinfomanager.add(new ResourceLocation(CCubesCore.MODID, this.getName()), ent.getCustomName());
//		BossBarCommand

		level.addFreshEntity(ent);
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
					if(!ent.isAlive() && (ent.tickCount > 0 || level.getDifficulty().equals(Difficulty.PEACEFUL)))
					{
						battleWrapper.trackedEntities.remove(i);
						if(battleWrapper.trackedEntities.isEmpty())
						{
							endBossfight(true, level, pos, player, battleWrapper);
							Scheduler.removeTask(this);
						}
					}
				}

				for(int i = battleWrapper.trackedPlayers.size() - 1; i >= 0; i--)
				{
					Entity ent = battleWrapper.trackedPlayers.get(i);
					if(ent.getOnPos().distToCenterSqr(battleWrapper.rewardCenterPos.getX(), battleWrapper.rewardCenterPos.getY(), battleWrapper.rewardCenterPos.getZ()) > 15 * 15 || ent.getY() < battleWrapper.rewardCenterPos.getY() - 1)
						ent.moveTo(battleWrapper.rewardCenterPos.getX(), battleWrapper.rewardCenterPos.getY() + 1, battleWrapper.rewardCenterPos.getZ());

					if(!ent.isAlive() && (ent.tickCount > 0 || level.getDifficulty().equals(Difficulty.PEACEFUL)))
					{
						for(Entity entity : battleWrapper.trackedEntities)
							entity.remove(Entity.RemovalReason.DISCARDED);
						battleWrapper.trackedEntities.clear();
						endBossfight(false, level, pos, player, battleWrapper);
						Scheduler.removeTask(this);
						return;
					}
				}
			}
		});
	}

	public void endBossfight(boolean resetPlayer, ServerLevel level, BlockPos pos, Player player, BattleWrapper battleWrapper)
	{
		for(Entity ent : battleWrapper.trackedSubEntities)
			if(ent.isAlive())
				ent.remove(Entity.RemovalReason.DISCARDED);
		battleWrapper.trackedSubEntities.clear();
		onBossFightEnd(level, pos, player);
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

	protected void trackPlayers(BattleWrapper battleWrapper, Player... player)
	{
		battleWrapper.trackedPlayers.addAll(Arrays.asList(player));
	}

	public abstract LivingEntity initBoss(ServerLevel level, BlockPos pos, Player player, JsonObject settings, BattleWrapper battleWrapper);

	public abstract void onBossFightEnd(ServerLevel level, BlockPos pos, Player player);

	public double getBossHealthDynamic(Player player, JsonObject settings)
	{
		double maxDamage = 3;
		for(ItemStack stack : player.getInventory().items)
		{
			Multimap<Attribute, AttributeModifier> atributes = stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND, stack);
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

	public ItemStack getHighestDamageItem(Player player)
	{
		double maxDamage = -1;
		ItemStack maxItem = ItemStack.EMPTY;
		for(ItemStack stack : player.getInventory().items)
		{
			Multimap<Attribute, AttributeModifier> atributes = stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND, stack);
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
		public final List<Entity> trackedEntities = new ArrayList<>();
		public final List<Entity> trackedSubEntities = new ArrayList<>();
		public final List<Player> trackedPlayers = new ArrayList<>();

		public BlockPos rewardCenterPos;
		public BioDomeGen domeGen;
	}
}
