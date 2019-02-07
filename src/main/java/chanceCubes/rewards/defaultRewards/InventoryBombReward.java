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

		for(int i = 0; i < player.inventory.mainInventory.size(); i++)
			player.inventory.mainInventory.set(i, new ItemStack(Blocks.DEADBUSH, 64));

		for(int i = 0; i < player.inventory.armorInventory.size(); i++)
		{
			ItemStack stack = new ItemStack(Blocks.DEADBUSH, 64);
			if(i == 0)
			{
				stack.setStackDisplayName("ButtonBoy");
				stack.setCount(13);
			}
			else if(i == 1)
			{
				stack.setStackDisplayName("TheBlackswordsman");
				stack.setCount(13);
			}
			player.inventory.armorInventory.set(i, stack);
		}

		player.sendMessage(new TextComponentString("Inventory Bomb!!!!"));

	}

	@Override
	public int getChanceValue()
	{
		return -55;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Inventory_Bomb";
	}

}
