package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

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
		for(int i = 0; i < armor.length; i++)
			player.inventory.armorInventory[i] = armor[i];

		player.addChatMessage(new TextComponentString("At least i didnt delete your items..."));

		RewardsUtil.placeBlock(Blocks.CHEST.getDefaultState(), world, pos);
		RewardsUtil.placeBlock(Blocks.CHEST.getDefaultState(), world, pos.add(1, 0, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(0, -1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(1, -1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(-1, 0, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(2, 0, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(0, 0, 1));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(1, 0, 1));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(0, 0, -1));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(1, 0, -1));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(0, -1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(1, -1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(0, 1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.getDefaultState(), world, pos.add(1, 1, 0));

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
		return -70;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Inventory_Chest";
	}

}
