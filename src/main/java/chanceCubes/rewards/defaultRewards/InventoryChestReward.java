package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class InventoryChestReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		final List<ItemStack> stacks = new ArrayList<ItemStack>();
		for(ItemStack stack : player.inventory.mainInventory)
			if(stack != null)
				stacks.add(stack);

		ItemStack[] armor = player.inventory.armorInventory.clone();
		player.inventory.clearInventory(null, -1);
		player.inventory.armorInventory = armor;

		player.addChatMessage(new ChatComponentText("At least i didnt delete your items..."));

		world.setBlock(x, y, z, Blocks.chest);
		world.setBlock(x + 1, y, z, Blocks.chest);
		world.setBlock(x, y - 1, z, Blocks.obsidian);
		world.setBlock(x + 1, y - 1, z, Blocks.obsidian);
		world.setBlock(x - 1, y, z, Blocks.obsidian);
		world.setBlock(x + 2, y, z, Blocks.obsidian);
		world.setBlock(x, y, z + 1, Blocks.obsidian);
		world.setBlock(x + 1, y, z + 1, Blocks.obsidian);
		world.setBlock(x, y, z - 1, Blocks.obsidian);
		world.setBlock(x + 1, y, z - 1, Blocks.obsidian);
		world.setBlock(x, y - 1, z, Blocks.obsidian);
		world.setBlock(x + 1, y - 1, z, Blocks.obsidian);
		world.setBlock(x, y + 1, z, Blocks.obsidian);
		world.setBlock(x + 1, y + 1, z, Blocks.obsidian);
		
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y, z);

		for(int i = 0; i < stacks.size(); i++)
		{
			if(i > chest.getSizeInventory() * 2)
				return;
			else if(i > chest.getSizeInventory())
				chest = (TileEntityChest) world.getTileEntity(x + 1, y, z);
			
			chest.setInventorySlotContents(i % chest.getSizeInventory(), stacks.get(i));
		}
	}

	@Override
	public int getChanceValue()
	{
		return -70;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Inventory_Chest";
	}

}
