package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class InventoryChestReward extends BaseCustomReward
{
	public InventoryChestReward()
	{
		super(CCubesCore.MODID + ":inventory_chest", -70);
	}

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player, Map<String, Object> settings)
	{
		final List<ItemStack> stacks = new ArrayList<ItemStack>();
		for(ItemStack stack : player.inventory.mainInventory)
			if(!stack.isEmpty())
				stacks.add(stack.copy());

		NonNullList<ItemStack> armor = player.inventory.armorInventory;
		for(ItemStack stack : player.inventory.armorInventory)
			if(!stack.isEmpty())
				armor.add(stack.copy());
		player.inventory.clear();
		for(int i = 0; i < armor.size(); i++)
			player.inventory.armorInventory.set(i, armor.get(i));

		player.sendMessage(new TextComponentString("At least i didnt delete your items..."));

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
}
