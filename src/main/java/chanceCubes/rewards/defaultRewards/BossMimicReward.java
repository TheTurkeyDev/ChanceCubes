package chanceCubes.rewards.defaultRewards;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BossMimicReward extends BossBaseReward
{
	public BossMimicReward()
	{
		super("mimic");
	}

	@Override
	public LivingEntity initBoss(ServerLevel level, BlockPos pos, Player player, JsonObject settings, BattleWrapper battleWrapper)
	{
		Zombie mimic = EntityType.ZOMBIE.create(level);
		mimic.setCustomName(new TextComponent("Mimic"));
		mimic.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(mimic.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 1.25);
		List<ItemStack> playerArmorInv = player.getInventory().armor;

		mimic.setItemSlot(EquipmentSlot.HEAD, playerArmorInv.get(3).copy());
		mimic.setDropChance(EquipmentSlot.HEAD, 0);
		mimic.setItemSlot(EquipmentSlot.CHEST, playerArmorInv.get(2).copy());
		mimic.setDropChance(EquipmentSlot.CHEST, 0);
		mimic.setItemSlot(EquipmentSlot.LEGS, playerArmorInv.get(1).copy());
		mimic.setDropChance(EquipmentSlot.LEGS, 0);
		mimic.setItemSlot(EquipmentSlot.FEET, playerArmorInv.get(0).copy());
		mimic.setDropChance(EquipmentSlot.FEET, 0);

		mimic.setItemSlot(EquipmentSlot.MAINHAND, getHighestDamageItem(player).copy());
		mimic.setDropChance(EquipmentSlot.MAINHAND, 0);
		mimic.setItemSlot(EquipmentSlot.OFFHAND, player.getInventory().offhand.get(0).copy());
		mimic.setDropChance(EquipmentSlot.OFFHAND, 0);

		return mimic;
	}

	@Override
	public void onBossFightEnd(ServerLevel level, BlockPos pos, Player player)
	{

	}
}
