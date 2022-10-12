package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class CoinFlipReward extends BaseCustomReward
{
	private final Map<Player, Task> inFlip = new HashMap<>();

	public CoinFlipReward()
	{
		super(CCubesCore.MODID + ":heads_or_tails", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, final Player player, JsonObject settings)
	{
		if(inFlip.containsKey(player))
			return;

		RewardsUtil.sendMessageToPlayer(player, "Heads or Tails?");


		Task task = new Task("Heads_Or_Tails", 600)
		{
			@Override
			public void callback()
			{
				timeUp(player);
			}
		};
		inFlip.put(player, task);

		Scheduler.scheduleTask(task);
	}

	private void timeUp(Player player)
	{
		if(!inFlip.containsKey(player) || !RewardsUtil.isPlayerOnline(player))
			return;

		RewardsUtil.sendMessageToPlayer(player, "Seem's that you didn't pick heads or tails.");
		RewardsUtil.sendMessageToPlayer(player, "You must be real fun at parties....");

		inFlip.remove(player);
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent.Submitted event)
	{
		Player player = event.getPlayer();

		String message = event.getRawText();

		if(!message.equalsIgnoreCase("Heads") && !message.equalsIgnoreCase("Tails"))
			return;

		if(inFlip.containsKey(player))
		{
			int flip = RewardsUtil.rand.nextInt(6000);

			// Yes I know heads has a 49.9833% chance and Tails has a 50% Chance. Deal with it.
			if(flip == 1738)
			{
				RewardsUtil.sendMessageToPlayer(player, "The coin landed on it's side.....");
				RewardsUtil.sendMessageToPlayer(player, "Well this is awkward");
			}
			else if(flip < 3000)
			{
				if(message.equalsIgnoreCase("Heads"))
				{
					RewardsUtil.sendMessageToPlayer(player, "It was heads! You're correct!");
					player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					RewardsUtil.sendMessageToPlayer(player, "It was heads! You're incorrect!");
					for(int i = 0; i < 5; i++)
					{
						player.level.addFreshEntity(new PrimedTnt(player.level, player.getX(), player.getY() + 1D, player.getZ(), player));
						player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
			else
			{
				if(message.equalsIgnoreCase("Tails"))
				{
					RewardsUtil.sendMessageToPlayer(player, "It was tails! You're correct!");
					player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					RewardsUtil.sendMessageToPlayer(player, "It was tails! You're incorrect!");
					for(int i = 0; i < 5; i++)
					{
						player.level.addFreshEntity(new PrimedTnt(player.level, player.getX(), player.getY() + 1D, player.getZ(), player));
						player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
					}
				}
			}

			Scheduler.removeTask(inFlip.remove(player));
			event.setCanceled(true);
		}
	}
}