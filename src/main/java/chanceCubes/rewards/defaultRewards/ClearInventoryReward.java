package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ClearInventoryReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		boolean cubes = false;
		final InventoryPlayer inv = new InventoryPlayer(player);
		player.inventory.copyInventory(inv);
		for(int slotNum = 0; slotNum < player.inventory.getSizeInventory(); slotNum++)
		{
			if(player.inventory.getStackInSlot(slotNum) != null && player.inventory.getStackInSlot(slotNum).getItem() instanceof ItemChanceCube)
				cubes = true;
			else
				player.inventory.setInventorySlotContents(slotNum, null);
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

	@Override
	public int getChanceValue()
	{
		return -100;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Clear_Inventory";
	}

}
