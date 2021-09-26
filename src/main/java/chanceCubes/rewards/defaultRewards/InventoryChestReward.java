package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class InventoryChestReward extends BaseCustomReward
{
	public InventoryChestReward()
	{
		super(CCubesCore.MODID + ":inventory_chest", -70);
	}

	@Override
	public void trigger(ServerLevel world, BlockPos pos, final Player player, JsonObject settings)
	{
		final List<ItemStack> stacks = new ArrayList<>();
		for(ItemStack stack : player.getInventory().items)
			if(!stack.isEmpty())
				stacks.add(stack.copy());

		NonNullList<ItemStack> armor = player.getInventory().armor;
		for(ItemStack stack : player.getInventory().armor)
			if(!stack.isEmpty())
				armor.add(stack.copy());
		player.getInventory().clearContent();
		for(int i = 0; i < armor.size(); i++)
			player.getInventory().armor.set(i, armor.get(i));

		RewardsUtil.sendMessageToPlayer(player, "At least i didnt delete your items...");

		RewardsUtil.placeBlock(Blocks.CHEST.defaultBlockState(), world, pos);
		RewardsUtil.placeBlock(Blocks.CHEST.defaultBlockState(), world, pos.offset(1, 0, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(0, -1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(1, -1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(-1, 0, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(2, 0, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(0, 0, 1));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(1, 0, 1));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(0, 0, -1));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(1, 0, -1));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(0, -1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(1, -1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(0, 1, 0));
		RewardsUtil.placeBlock(Blocks.OBSIDIAN.defaultBlockState(), world, pos.offset(1, 1, 0));

		ChestBlockEntity chest = (ChestBlockEntity) world.getBlockEntity(pos);

		if(chest == null)
			return;

		for(int i = 0; i < stacks.size(); i++)
		{
			if(i > chest.getContainerSize() * 2)
				return;
			else if(i > chest.getContainerSize())
				chest = (ChestBlockEntity) world.getBlockEntity(pos.offset(1, 0, 0));

			chest.setItem(i % chest.getContainerSize(), stacks.get(i));
		}
	}
}
