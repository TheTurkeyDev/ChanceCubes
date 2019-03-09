package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ZombieCopyCatReward extends BaseCustomReward
{
	public ZombieCopyCatReward()
	{
		this.setChanceValue(-25);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		EntityZombie zombie = new EntityZombie(world);
		zombie.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
		ItemStack weapon = ItemStack.EMPTY;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = player.inventory.mainInventory.get(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemSword)
			{
				weapon = stack.copy();
			}
		}

		if(weapon.isEmpty() && !player.inventory.getCurrentItem().isEmpty())
			weapon = player.inventory.getCurrentItem().copy();

		zombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weapon);
		// TODO: Change?
		zombie.setDropItemsWhenDead(true);

		if(!player.inventory.armorInventory.get(0).isEmpty())
			zombie.setItemStackToSlot(EntityEquipmentSlot.FEET, player.inventory.armorInventory.get(0).copy());
		if(!player.inventory.armorInventory.get(1).isEmpty())
			zombie.setItemStackToSlot(EntityEquipmentSlot.LEGS, player.inventory.armorInventory.get(1).copy());
		if(!player.inventory.armorInventory.get(2).isEmpty())
			zombie.setItemStackToSlot(EntityEquipmentSlot.CHEST, player.inventory.armorInventory.get(2).copy());
		if(!player.inventory.armorInventory.get(3).isEmpty())
			zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, player.inventory.armorInventory.get(3).copy());

		world.spawnEntity(zombie);
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Copy_Cat_Zombie";
	}
}