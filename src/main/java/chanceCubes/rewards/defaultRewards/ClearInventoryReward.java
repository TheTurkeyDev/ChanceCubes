package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
		final PlayerInventory inv = new PlayerInventory(player);
		inv.copyInventory(player.getInventory());
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
					player.getInventory().copy(inv);
					RewardsUtil.sendMessageToPlayer(player, "AHHHHHH JK!! You should have seen your face!");
				}
			});
		}
	}
}
