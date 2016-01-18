package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ZombieCopyCatReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		EntityZombie zombie = new EntityZombie(world);
		zombie.setLocationAndAngles(x, y, z, 0, 0);
		ItemStack weapon = null;
		int swordSlot = 0;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack != null && stack.getItem() instanceof ItemSword)
			{
				weapon = stack.copy();
				swordSlot = i;
			}
		}
		if(weapon == null && player.getCurrentEquippedItem() != null)
		{
			weapon = player.getCurrentEquippedItem().copy();
			swordSlot = player.inventory.currentItem;
		}

		zombie.setCurrentItemOrArmor(0, weapon);
		zombie.setEquipmentDropChance(0, 2);

		if(player.inventory.armorInventory[0] != null)
		{
			zombie.setCurrentItemOrArmor(1, player.inventory.armorInventory[0].copy());
			zombie.setEquipmentDropChance(1, 2);
		}
		if(player.inventory.armorInventory[1] != null)
		{
			zombie.setCurrentItemOrArmor(2, player.inventory.armorInventory[1].copy());
			zombie.setEquipmentDropChance(2, 2);
		}
		if(player.inventory.armorInventory[2] != null)
		{
			zombie.setCurrentItemOrArmor(3, player.inventory.armorInventory[2].copy());
			zombie.setEquipmentDropChance(3, 2);
		}
		if(player.inventory.armorInventory[3] != null)
		{
			zombie.setCurrentItemOrArmor(4, player.inventory.armorInventory[3].copy());
			zombie.setEquipmentDropChance(4, 2);
		}
		
		final int slotToSet = swordSlot;
		Task task = new Task("Copycat zombie reward", 1)
		{
			@Override
			public void callback()
			{
				player.inventory.setInventorySlotContents(slotToSet, null);
				player.setCurrentItemOrArmor(1, null);
				player.setCurrentItemOrArmor(2, null);
				player.setCurrentItemOrArmor(3, null);
				player.setCurrentItemOrArmor(4, null);
			}

		};

		Scheduler.scheduleTask(task);

		world.spawnEntityInWorld(zombie);
	}

	@Override
	public int getChanceValue()
	{
		return -25;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Copy_Cat_Zombie";
	}
}