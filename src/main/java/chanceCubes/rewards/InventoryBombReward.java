package chanceCubes.rewards;

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
			EntityItem ient = new EntityItem(world, x, y, z, stack);
			ient.motionX = rand.nextInt(5) - 2;
			ient.motionY = rand.nextInt(5) - 2;
			ient.motionZ = rand.nextInt(5) - 2;
			world.spawnEntityInWorld(ient);
		}
		for(ItemStack stack: player.inventory.armorInventory)
		{
			EntityItem ient = new EntityItem(world, x, y, z, stack);
			ient.motionX = rand.nextInt(5) - 2;
			ient.motionY = rand.nextInt(5) - 2;
			ient.motionZ = rand.nextInt(5) - 2;
			world.spawnEntityInWorld(ient);
		}
		
		player.inventory.clearInventory(null, -1);
		
		player.addChatMessage(new ChatComponentText("Inventory Bomb!!!!"));
		
	}

	@Override
	public int getChanceValue()
	{
		return -80;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Inventort_Bomb";
	}

}

