package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class ZombieCopyCatReward extends BaseCustomReward
{
	public ZombieCopyCatReward()
	{
		super(CCubesCore.MODID + ":Copy_Cat_Zombie", -25);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		ZombieEntity zombie = EntityType.ZOMBIE.create(world);
		zombie.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
		ItemStack weapon = ItemStack.EMPTY;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = player.inventory.mainInventory.get(i);
			if(!stack.isEmpty() && stack.getItem() instanceof SwordItem)
			{
				weapon = stack.copy();
			}
		}

		if(weapon.isEmpty() && !player.inventory.getCurrentItem().isEmpty())
			weapon = player.inventory.getCurrentItem().copy();

		zombie.setItemStackToSlot(EquipmentSlotType.MAINHAND, weapon);

		if(!player.inventory.armorInventory.get(0).isEmpty())
		{
			zombie.setItemStackToSlot(EquipmentSlotType.FEET, player.inventory.armorInventory.get(0).copy());
			zombie.setDropChance(EquipmentSlotType.FEET, 1);
		}
		if(!player.inventory.armorInventory.get(1).isEmpty())
		{
			zombie.setItemStackToSlot(EquipmentSlotType.LEGS, player.inventory.armorInventory.get(1).copy());
			zombie.setDropChance(EquipmentSlotType.LEGS, 1);
		}
		if(!player.inventory.armorInventory.get(2).isEmpty())
		{
			zombie.setItemStackToSlot(EquipmentSlotType.CHEST, player.inventory.armorInventory.get(2).copy());
			zombie.setDropChance(EquipmentSlotType.CHEST, 1);
		}
		if(!player.inventory.armorInventory.get(3).isEmpty())
		{
			zombie.setItemStackToSlot(EquipmentSlotType.HEAD, player.inventory.armorInventory.get(3).copy());
			zombie.setDropChance(EquipmentSlotType.HEAD, 1);
		}

		world.addEntity(zombie);
	}
}