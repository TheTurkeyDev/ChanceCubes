package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ClearInventoryReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		boolean cubes = false;
		final ItemStack[] inv = player.inventory.mainInventory.clone();
		final ItemStack[] armorinv = player.inventory.armorInventory.clone();
		for(int slotNum = 0; slotNum < player.inventory.getSizeInventory(); slotNum++)
		{
			if(player.inventory.getStackInSlot(slotNum) != null && player.inventory.getStackInSlot(slotNum).getItem() instanceof ItemChanceCube)
				cubes = true;
			else
				player.inventory.setInventorySlotContents(slotNum, null);
		}
		
		world.playSoundEffect(x, y, z, "random.burp", 1, 1);
		player.addChatMessage(new ChatComponentText("I hope you didn't have anything of value with you :)"));
		if(cubes)
			player.addChatMessage(new ChatComponentText("Don't worry, I left the cubes for you!"));
		
		if(world.rand.nextInt(5) == 1)
		{
			Task task = new Task("Replace_Inventory", 200)
			{
				@Override
				public void callback()
				{
					player.inventory.mainInventory = inv;
					player.inventory.armorInventory = armorinv;
					player.addChatMessage(new ChatComponentText("AHHHHHH JK!! You should have seen your face!"));
				}

			};

			Scheduler.scheduleTask(task);
		}
	}

	@Override
	public int getChanceValue()
	{
		return -100;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Clear_Inventory";
	}

}