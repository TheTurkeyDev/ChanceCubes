package chanceCubes.rewards;

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
				weapon = stack.copy();
		}
		if(weapon == null && player.getCurrentEquippedItem() != null)
			weapon = player.getCurrentEquippedItem().copy();
		
		zombie.setCurrentItemOrArmor(0, weapon);
		zombie.setCurrentItemOrArmor(1, player.inventory.armorInventory[0]);
		zombie.setCurrentItemOrArmor(2, player.inventory.armorInventory[1]);
		zombie.setCurrentItemOrArmor(3, player.inventory.armorInventory[2]);
		zombie.setCurrentItemOrArmor(4, player.inventory.armorInventory[3]);
		
		world.spawnEntityInWorld(zombie);
	}

	@Override
	public int getChanceValue()
	{
		return -90;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Copy_Cat_Zombie";
	}
}