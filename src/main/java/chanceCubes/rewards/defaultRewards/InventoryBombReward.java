package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class InventoryBombReward implements IChanceCubeReward
{
	private Random rand = new Random();
	
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		for(ItemStack stack: player.inventory.mainInventory)
		{
			if(stack == null)
				continue;
			EntityItem ient = new EntityItem(world, x, y, z, stack);
			ient.motionY = rand.nextInt(1) - 0.5;
			ient.delayBeforeCanPickup = 40;
			world.spawnEntityInWorld(ient);
		}
		for(ItemStack stack: player.inventory.armorInventory)
		{
			if(stack == null)
				continue;
			EntityItem ient = new EntityItem(world, x, y, z, stack);
			ient.motionY = rand.nextInt(1) - 0.5;
			ient.delayBeforeCanPickup = 40;
			world.spawnEntityInWorld(ient);
		}
		
		player.inventory.openInventory();
		player.inventory.clearInventory(null, -1);
		player.inventory.closeInventory();
		
		player.addChatMessage(new ChatComponentText("Inventory Bomb!!!!"));
		
	}

	@Override
	public int getChanceValue()
	{
		return -65;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Inventory_Bomb";
	}

}

