package chanceCubes.rewards.defaultRewards;

import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BossMimicReward extends BossBaseReward
{
	public BossMimicReward()
	{
		super("Mimic");
	}

	@Override
	public void spawnBoss(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		EntityZombie mimic = new EntityZombie(world);
		mimic.setCustomNameTag("Mimic");
		mimic.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		mimic.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player));
		mimic.setHealth(mimic.getMaxHealth());
		List<ItemStack> playerArmorInv = player.inventory.armorInventory;
		mimic.setItemStackToSlot(EntityEquipmentSlot.HEAD, playerArmorInv.get(3).copy());
		mimic.setItemStackToSlot(EntityEquipmentSlot.CHEST, playerArmorInv.get(2).copy());
		mimic.setItemStackToSlot(EntityEquipmentSlot.LEGS, playerArmorInv.get(1).copy());
		mimic.setItemStackToSlot(EntityEquipmentSlot.FEET, playerArmorInv.get(0).copy());
		mimic.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, getHighestDamageItem(player));
		mimic.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, player.inventory.offHandInventory.get(0).copy());

		world.spawnEntity(mimic);
		super.trackEntities(mimic);
		super.trackEntities(player);
	}

	@Override
	public void onBossFightEnd(World world, BlockPos pos, EntityPlayer player)
	{

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
