package chanceCubes.rewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;

public class ClearInventoryReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		boolean cubes = false;
		for(int slotNum = 0; slotNum < player.inventory.getSizeInventory(); slotNum++)
		{
			if(player.inventory.getStackInSlot(slotNum) != null && player.inventory.getStackInSlot(slotNum).getItem() instanceof ItemChanceCube)
				cubes = true;
			else
				player.inventory.setInventorySlotContents(slotNum, null);
		}
		
		player.addChatMessage(new ChatComponentText("I hope you didn't have anything of value with you :)"));
		if(cubes)
			player.addChatMessage(new ChatComponentText("Don't worry, I left the cubes for you!"));
	}

	@Override
	public int getChanceValue()
	{
		return -50;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Clear_Inventory";
	}

}
