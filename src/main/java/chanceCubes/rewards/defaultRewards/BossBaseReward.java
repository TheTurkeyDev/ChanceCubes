package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BioDomeGen;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class BossBaseReward extends BaseCustomReward
{
	private String bossName;

	private List<Entity> trackedEntities = new ArrayList<>();
	private List<Entity> trackedSubEntities = new ArrayList<>();
	private List<EntityPlayer> trackedPlayers = new ArrayList<>();
	private BioDomeGen domeGen;

	private BlockPos rewardCenterPos;

	public BossBaseReward(String bossName)
	{
		super(CCubesCore.MODID + ":Boss_" + bossName, -35);
		this.bossName = bossName.replace("_", " ");
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		this.rewardCenterPos = pos;
		domeGen = new BioDomeGen(player);
		domeGen.genRandomDome(pos.add(0, -1, 0), world, 15, false);
		TextComponentString message = new TextComponentString("BOSS FIGHT!");
		message.setStyle((new Style()).setColor(TextFormatting.RED));
		SPacketTitle titlePacket = new SPacketTitle(SPacketTitle.Type.TITLE, message, 10, 500, 0);
		RewardsUtil.setNearPlayersTitle(world, titlePacket, pos, 50);

		Scheduler.scheduleTask(new Task("boss_fight_subtitle_1", 120)
		{
			@Override
			public void callback()
			{
				TextComponentString message = new TextComponentString("");
				message.appendSibling(player.getDisplayName());

				StringBuilder sbSpace = new StringBuilder();
				sbSpace.append(" VS ");
				for(int i = 0; i < bossName.length(); i++)
				{
					sbSpace.append(" ");
				}
				message.appendText(sbSpace.toString());

				message.setStyle((new Style()).setColor(TextFormatting.RED));
				SPacketTitle titlePacket = new SPacketTitle(SPacketTitle.Type.SUBTITLE, message, 0, 500, 0);
				RewardsUtil.setNearPlayersTitle(world, titlePacket, pos, 50);
			}
		});

		Scheduler.scheduleTask(new Task("boss_fight_subtitle_2", 160)
		{
			@Override
			public void callback()
			{
				TextComponentString message = new TextComponentString("");
				message.appendSibling(player.getDisplayName());
				message.appendText(" VS ");
				message.appendText(bossName);
				message.setStyle((new Style()).setColor(TextFormatting.RED));
				SPacketTitle titlePacket = new SPacketTitle(SPacketTitle.Type.SUBTITLE, message, 0, 100, 10);
				RewardsUtil.setNearPlayersTitle(world, titlePacket, pos, 50);
			}
		});

		Scheduler.scheduleTask(new Task("boss_fight_start", 200)
		{
			@Override
			public void callback()
			{
				startBossFight(world, pos, player, settings);
			}
		});
	}

	public void startBossFight(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		spawnBoss(world, pos, player, settings);
		Scheduler.scheduleTask(new Task("boss_fight_tracker", -1, 5)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				for(int i = trackedEntities.size() - 1; i >= 0; i--)
				{
					Entity ent = trackedEntities.get(i);
					if(ent.isDead)
					{
						trackedEntities.remove(i);
						if(trackedEntities.isEmpty())
						{
							endBossfight(true, world, pos, player);
							Scheduler.removeTask(this);
						}
					}
				}

				for(int i = trackedPlayers.size() - 1; i >= 0; i--)
				{
					Entity ent = trackedPlayers.get(i);
					if(ent.getDistance(rewardCenterPos.getX(), rewardCenterPos.getY(), rewardCenterPos.getZ()) > 15 || ent.posY < rewardCenterPos.getY() - 1)
						ent.setPositionAndUpdate(rewardCenterPos.getX(), rewardCenterPos.getY() + 1, rewardCenterPos.getZ());

					if(ent.isDead)
					{
						for(Entity entity : trackedEntities)
							entity.setDead();
						trackedEntities.clear();
						endBossfight(false, world, pos, player);
						Scheduler.removeTask(this);
						return;
					}
				}
			}
		});
	}

	public void endBossfight(boolean resetPlayer, World world, BlockPos pos, EntityPlayer player)
	{
		for(Entity ent : trackedSubEntities)
			if(!ent.isDead)
				ent.setDead();
		trackedSubEntities.clear();
		onBossFightEnd(world, pos, player);
		domeGen.removeDome(resetPlayer);
	}

	protected void trackEntities(Entity... ents)
	{
		trackedEntities.addAll(Arrays.asList(ents));
	}

	protected void trackSubEntities(Entity... ents)
	{
		trackedSubEntities.addAll(Arrays.asList(ents));
	}

	protected void trackedPlayers(EntityPlayer... player)
	{
		trackedPlayers.addAll(Arrays.asList(player));
	}

	public abstract void spawnBoss(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings);

	public abstract void onBossFightEnd(World world, BlockPos pos, EntityPlayer player);

	public double getBossHealthDynamic(EntityPlayer player, Map<String, Object> settings)
	{
		double maxDamage = 3;
		for(ItemStack stack : player.inventory.mainInventory)
		{
			Multimap<String, AttributeModifier> atributes = stack.getItem().getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack);
			if(atributes.containsKey(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
			{
				Collection<AttributeModifier> damageList = atributes.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
				for(AttributeModifier damage : damageList)
					if(maxDamage < damage.getAmount())
						maxDamage = damage.getAmount();
			}
		}

		double prePofileHealth = maxDamage * 15.0;

		double profileMult = super.getSettingAsDouble(settings, "boss_health_multiplier", 1.0, 0.0, 10.0);

		return prePofileHealth * profileMult;
	}

	public ItemStack getHighestDamageItem(EntityPlayer player)
	{
		double maxDamage = -1;
		ItemStack maxItem = ItemStack.EMPTY;
		for(ItemStack stack : player.inventory.mainInventory)
		{
			Multimap<String, AttributeModifier> atributes = stack.getItem().getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack);
			if(atributes.containsKey(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
			{
				Collection<AttributeModifier> damageList = atributes.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
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
}
