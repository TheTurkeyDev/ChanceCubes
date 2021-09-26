package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class ZombieCopyCatReward extends BaseCustomReward
{
	public ZombieCopyCatReward()
	{
		super(CCubesCore.MODID + ":Copy_Cat_Zombie", -25);
	}

	@Override
	public void trigger(ServerLevel world, BlockPos pos, Player player, JsonObject settings)
	{
		Zombie zombie = EntityType.ZOMBIE.create(world);
		zombie.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
		ItemStack weapon = ItemStack.EMPTY;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = player.getInventory().items.get(i);
			if(!stack.isEmpty() && stack.getItem() instanceof SwordItem)
			{
				weapon = stack.copy();
			}
		}

		if(weapon.isEmpty() && !player.getInventory().getSelected().isEmpty())
			weapon = player.getInventory().getSelected().copy();

		zombie.setItemSlot(EquipmentSlot.MAINHAND, weapon);

		if(!player.getInventory().armor.get(0).isEmpty())
		{
			zombie.setItemSlot(EquipmentSlot.FEET, player.getInventory().armor.get(0).copy());
			zombie.setDropChance(EquipmentSlot.FEET, 1);
		}
		if(!player.getInventory().armor.get(1).isEmpty())
		{
			zombie.setItemSlot(EquipmentSlot.LEGS, player.getInventory().armor.get(1).copy());
			zombie.setDropChance(EquipmentSlot.LEGS, 1);
		}
		if(!player.getInventory().armor.get(2).isEmpty())
		{
			zombie.setItemSlot(EquipmentSlot.CHEST, player.getInventory().armor.get(2).copy());
			zombie.setDropChance(EquipmentSlot.CHEST, 1);
		}
		if(!player.getInventory().armor.get(3).isEmpty())
		{
			zombie.setItemSlot(EquipmentSlot.HEAD, player.getInventory().armor.get(3).copy());
			zombie.setDropChance(EquipmentSlot.HEAD, 1);
		}

		world.addFreshEntity(zombie);
	}
}