package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class InventoryBombReward extends BaseCustomReward
{
	public InventoryBombReward()
	{
		super(CCubesCore.MODID + ":Inventory_Bomb", -55);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		player.inventory.dropAllItems();

		for(int i = 0; i < player.inventory.mainInventory.size(); i++)
			player.inventory.mainInventory.set(i, new ItemStack(Blocks.DEAD_BUSH, 64));

		for(int i = 0; i < player.inventory.armorInventory.size(); i++)
		{
			ItemStack stack = new ItemStack(Blocks.DEAD_BUSH, 64);
			if(i == 0)
			{
				stack.setDisplayName(new TextComponentString("ButtonBoy"));
				stack.setCount(13);
			}
			else if(i == 1)
			{
				stack.setDisplayName(new TextComponentString("TheBlackswordsman"));
				stack.setCount(13);
			}
			player.inventory.armorInventory.set(i, stack);
		}

		player.sendMessage(new TextComponentString("Inventory Bomb!!!!"));

	}
}
