package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class ZombieCopyCatReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		EntityZombie zombie = new EntityZombie(world);
		zombie.setLocationAndAngles(x, y, z, 0, 0);
		ItemStack weapon = null;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack != null && stack.getItem() instanceof ItemSword)
			{
				weapon = stack.copy();
				// player.inventory.mainInventory[i] = new ItemStack(Blocks.air);
			}
		}
		if(weapon == null && player.getCurrentEquippedItem() != null)
		{
			weapon = player.getCurrentEquippedItem().copy();
			// player.setCurrentItemOrArmor(0, new ItemStack(Blocks.air));
		}

		zombie.setCurrentItemOrArmor(0, weapon);
		zombie.setEquipmentDropChance(0, 0);

		int i = player.inventory.mainInventory.length;

		if(player.inventory.armorInventory[0] != null)
		{
			zombie.setCurrentItemOrArmor(1, player.inventory.armorInventory[0].copy());
			zombie.setEquipmentDropChance(1, 1);
			player.inventory.setInventorySlotContents(i, null);
		}
		if(player.inventory.armorInventory[1] != null)
		{
			zombie.setCurrentItemOrArmor(2, player.inventory.armorInventory[1].copy());
			zombie.setEquipmentDropChance(2, 1);
			player.inventory.setInventorySlotContents(i + 1, null);
		}
		if(player.inventory.armorInventory[2] != null)
		{
			zombie.setCurrentItemOrArmor(3, player.inventory.armorInventory[2].copy());
			zombie.setEquipmentDropChance(3, 1);
			player.inventory.setInventorySlotContents(i + 2, null);
		}
		if(player.inventory.armorInventory[3] != null)
		{
			zombie.setCurrentItemOrArmor(4, player.inventory.armorInventory[3].copy());
			zombie.setEquipmentDropChance(4, 1);
			player.inventory.setInventorySlotContents(i + 3, null);
		}

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