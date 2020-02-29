package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Map;

public class ClearInventoryReward extends BaseCustomReward
{

	public ClearInventoryReward()
	{
		super(CCubesCore.MODID + ":clear_inventory", -100);
	}

	@Override
	public void trigger(World world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		boolean cubes = false;
		final PlayerInventory inv = new PlayerInventory(player);
		inv.copyInventory(player.inventory);
		for(int slotNum = 0; slotNum < player.inventory.getSizeInventory(); slotNum++)
		{
			if(!player.inventory.getStackInSlot(slotNum).isEmpty() && player.inventory.getStackInSlot(slotNum).getItem() instanceof ItemChanceCube)
				cubes = true;
			else
				player.inventory.setInventorySlotContents(slotNum, ItemStack.EMPTY);
		}
		world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1f, 1f);

		player.sendMessage(new StringTextComponent("I hope you didn't have anything of value with you :)"));
		if(cubes)
			player.sendMessage(new StringTextComponent("Don't worry, I left the cubes for you!"));

		if(world.rand.nextInt(5) == 1)
		{
			Scheduler.scheduleTask(new Task("Replace_Inventory", 200)
			{
				@Override
				public void callback()
				{
					player.inventory.copyInventory(inv);
					player.sendMessage(new StringTextComponent("AHHHHHH JK!! You should have seen your face!"));
				}

			});
		}
	}
}
