package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class InventoryBombReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		player.inventory.dropAllItems();

		for(int i = 0; i < player.inventory.mainInventory.length; i++)
			player.inventory.mainInventory[i] = new ItemStack(Blocks.DEADBUSH, 64);

		for(int i = 0; i < player.inventory.armorInventory.length; i++)
		{
			ItemStack stack = new ItemStack(Blocks.DEADBUSH, 64);
			if(i == 0)
			{
				stack.setStackDisplayName("ButtonBoy");
				stack.stackSize = 13;
			}
			else if(i == 1)
			{
				stack.setStackDisplayName("TheBlackswordsman");
				stack.stackSize = 13;
			}
			player.inventory.armorInventory[i] = stack;
		}

		player.addChatMessage(new TextComponentString("Inventory Bomb!!!!"));

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
