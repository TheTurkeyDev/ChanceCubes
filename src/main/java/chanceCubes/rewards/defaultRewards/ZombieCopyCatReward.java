package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		if(weapon == null && player.inventory.getCurrentItem() != null)
		{
			weapon = player.inventory.getCurrentItem().copy();
		}

		zombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weapon);
		//TODO: Change?
		zombie.setDropItemsWhenDead(true);

		if(player.inventory.armorInventory[0] != null)
		{
			zombie.setItemStackToSlot(EntityEquipmentSlot.FEET, player.inventory.armorInventory[0].copy());
		}
		if(player.inventory.armorInventory[1] != null)
		{
			zombie.setItemStackToSlot(EntityEquipmentSlot.LEGS, player.inventory.armorInventory[0].copy());
		}
		if(player.inventory.armorInventory[2] != null)
		{
			zombie.setItemStackToSlot(EntityEquipmentSlot.CHEST, player.inventory.armorInventory[0].copy());
		}
		if(player.inventory.armorInventory[3] != null)
		{
			zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, player.inventory.armorInventory[0].copy());
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