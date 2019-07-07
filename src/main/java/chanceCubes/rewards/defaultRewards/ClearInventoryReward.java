package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ClearInventoryReward extends BaseCustomReward
{

	public ClearInventoryReward()
	{
		super(CCubesCore.MODID + ":Clear_Inventory", -100);
	}

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player, Map<String, Object> settings)
	{
		boolean cubes = false;
		final InventoryPlayer inv = new InventoryPlayer(player);
		player.inventory.copyInventory(inv);
		for(int slotNum = 0; slotNum < player.inventory.getSizeInventory(); slotNum++)
		{
			if(!player.inventory.getStackInSlot(slotNum).isEmpty() && player.inventory.getStackInSlot(slotNum).getItem() instanceof ItemChanceCube)
				cubes = true;
			else
				player.inventory.setInventorySlotContents(slotNum, ItemStack.EMPTY);
		}
		world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1f, 1f);

		player.sendMessage(new TextComponentString("I hope you didn't have anything of value with you :)"));
		if(cubes)
			player.sendMessage(new TextComponentString("Don't worry, I left the cubes for you!"));

		if(world.rand.nextInt(5) == 1)
		{
			Scheduler.scheduleTask(new Task("Replace_Inventory", 200)
			{
				@Override
				public void callback()
				{
					player.inventory = inv;
					player.sendMessage(new TextComponentString("AHHHHHH JK!! You should have seen your face!"));
				}

			});
		}
	}
}
