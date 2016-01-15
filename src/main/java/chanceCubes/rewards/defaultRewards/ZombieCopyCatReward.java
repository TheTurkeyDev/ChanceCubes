package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class ZombieCopyCatReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		EntityZombie zombie = new EntityZombie(world);
		zombie.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
		ItemStack weapon = null;
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack != null && stack.getItem() instanceof ItemSword)
			{
				weapon = stack.copy();
			}
		}
		if(weapon == null && player.getCurrentEquippedItem() != null)
		{
			weapon = player.getCurrentEquippedItem().copy();
		}

		zombie.setCurrentItemOrArmor(0, weapon);
		zombie.setEquipmentDropChance(0, 0);

		if(player.inventory.armorInventory[0] != null)
		{
			zombie.setCurrentItemOrArmor(1, player.inventory.armorInventory[0].copy());
			zombie.setEquipmentDropChance(1, 0);
		}
		if(player.inventory.armorInventory[1] != null)
		{
			zombie.setCurrentItemOrArmor(2, player.inventory.armorInventory[1].copy());
			zombie.setEquipmentDropChance(2, 0);
		}
		if(player.inventory.armorInventory[2] != null)
		{
			zombie.setCurrentItemOrArmor(3, player.inventory.armorInventory[2].copy());
			zombie.setEquipmentDropChance(3, 0);
		}
		if(player.inventory.armorInventory[3] != null)
		{
			zombie.setCurrentItemOrArmor(4, player.inventory.armorInventory[3].copy());
			zombie.setEquipmentDropChance(4, 0);
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