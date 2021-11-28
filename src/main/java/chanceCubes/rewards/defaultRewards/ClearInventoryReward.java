package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.mcwrapper.InventoryWrapper;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ClearInventoryReward extends BaseCustomReward
{

	public ClearInventoryReward()
	{
		super(CCubesCore.MODID + ":clear_inventory", -100);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, final Player player, JsonObject settings)
	{
		boolean cubes = false;
		final Inventory inv = new Inventory(player);
		InventoryWrapper.copyInvAToB(player.getInventory(), inv);
		for(int slotNum = 0; slotNum < player.getInventory().getContainerSize(); slotNum++)
		{
			if(!player.getInventory().getItem(slotNum).isEmpty() && player.getInventory().getItem(slotNum).getItem() instanceof ItemChanceCube)
				cubes = true;
			else
				player.getInventory().setItem(slotNum, ItemStack.EMPTY);
		}
		level.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1f, 1f);

		RewardsUtil.sendMessageToPlayer(player, "I hope you didn't have anything of value with you :)");
		if(cubes)
			RewardsUtil.sendMessageToPlayer(player, "Don't worry, I left the cubes for you!");

		if(RewardsUtil.rand.nextInt(5) == 1)
		{
			Scheduler.scheduleTask(new Task("Replace_Inventory", 200)
			{
				@Override
				public void callback()
				{
					InventoryWrapper.copyInvAToB(inv, player.getInventory());
					RewardsUtil.sendMessageToPlayer(player, "AHHHHHH JK!! You should have seen your face!");
				}
			});
		}
	}
}
