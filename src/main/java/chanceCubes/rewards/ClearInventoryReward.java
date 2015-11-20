package chanceCubes.rewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class ClearInventoryReward implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		List<Integer> validSlots = new ArrayList<Integer>();
		ItemStack[] mainInv = player.inventory.mainInventory;
		ItemStack[] armorInv = player.inventory.armorInventory;
		for(int i = 0; i < mainInv.length + armorInv.length; i++)
		{
			if(i < mainInv.length)
			{
				if(mainInv[i] != null)
					validSlots.add(i);
			}
			else
			{
				if(armorInv[i - mainInv.length] != null)
					validSlots.add(i);
			}
		}

		int toRemove = rand.nextInt(5) + 1;

		for(int i = 0; i < toRemove; i++)
		{
			if(validSlots.size() > 0)
			{
				int randomint = rand.nextInt(validSlots.size());
				int slot = validSlots.get(randomint);
				player.inventory.setInventorySlotContents(slot, null);
				validSlots.remove(randomint);
			}
		}

		player.addChatMessage(new ChatComponentText("The Cubes have spoken!"));
		player.addChatMessage(new ChatComponentText(toRemove + " item(s) were removed from your inventory!"));
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
