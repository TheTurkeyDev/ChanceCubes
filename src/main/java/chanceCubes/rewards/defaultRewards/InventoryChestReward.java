package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;

public class InventoryChestReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		final List<ItemStack> stacks = new ArrayList<ItemStack>();
		for(ItemStack stack : player.inventory.mainInventory)
			if(stack != null)
				stacks.add(stack);

		ItemStack[] armor = player.inventory.armorInventory.clone();
		player.inventory.clear();
		player.inventory.armorInventory = armor;

		player.addChatMessage(new ChatComponentText("At least i didnt delete your items..."));

		world.setBlockState(pos, Blocks.chest.getDefaultState());
		world.setBlockState(pos.add(1, 0, 0), Blocks.chest.getDefaultState());
		world.setBlockState(pos.add(0, -1, 0), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(1, -1, 0), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(-1, 0, 0), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(2, 0, 0), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(0, 0, 1), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(1, 0, 1), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(0, 0, -1), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(1, 0, -1), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(0, -1, 0), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(1, -1, 0), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(0, 1, 0), Blocks.obsidian.getDefaultState());
		world.setBlockState(pos.add(1, 1, 0), Blocks.obsidian.getDefaultState());
		
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);

		for(int i = 0; i < stacks.size(); i++)
		{
			if(i > chest.getSizeInventory() * 2)
				return;
			else if(i > chest.getSizeInventory())
				chest = (TileEntityChest) world.getTileEntity(pos.add(1, 0, 0));
			
			chest.setInventorySlotContents(i % chest.getSizeInventory(), stacks.get(i));
		}
	}

	@Override
	public int getChanceValue()
	{
		return -95;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Inventory_ Chest";
	}

}
