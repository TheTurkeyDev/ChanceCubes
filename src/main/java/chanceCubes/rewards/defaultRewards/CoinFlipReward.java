package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoinFlipReward extends BaseCustomReward
{
	private List<PlayerEntity> inFlip = new ArrayList<>();

	public CoinFlipReward()
	{
		super(CCubesCore.MODID + ":heads_or_tails", 0);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		if(inFlip.contains(player))
			return;

		RewardsUtil.sendMessageToPlayer(player, "Heads or Tails?");

		inFlip.add(player);

		Task task = new Task("Heads_Or_Tails", 600)
		{
			@Override
			public void callback()
			{
				timeUp(player);
			}

		};

		Scheduler.scheduleTask(task);
	}

	private void timeUp(PlayerEntity player)
	{
		if(!inFlip.contains(player) || !RewardsUtil.isPlayerOnline(player))
			return;

		RewardsUtil.sendMessageToPlayer(player, "Seem's that you didn't pick heads or tails.");
		RewardsUtil.sendMessageToPlayer(player, "You must be real fun at parties....");

		inFlip.remove(player);
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		PlayerEntity player = event.getPlayer();

		String message = event.getMessage();

		if(!message.equalsIgnoreCase("Heads") && !message.equalsIgnoreCase("Tails"))
			return;

		if(inFlip.contains(player))
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
					player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					RewardsUtil.sendMessageToPlayer(player, "It was heads! You're incorrect!");
					for(int i = 0; i < 5; i++)
					{
						player.world.addEntity(new TNTEntity(player.world, player.getPosX(), player.getPosY() + 1D, player.getPosZ(), player));
						player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
			else
			{
				if(message.equalsIgnoreCase("Tails"))
				{
					RewardsUtil.sendMessageToPlayer(player, "It was tails! You're correct!");
					player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					RewardsUtil.sendMessageToPlayer(player, "It was tails! You're incorrect!");
					for(int i = 0; i < 5; i++)
					{
						player.world.addEntity(new TNTEntity(player.world, player.getPosX(), player.getPosY() + 1D, player.getPosZ(), player));
						player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}

			inFlip.remove(player);
			event.setCanceled(true);
		}
	}
}