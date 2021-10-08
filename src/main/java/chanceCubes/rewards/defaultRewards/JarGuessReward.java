package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.GuiTextLocation;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JarGuessReward extends BaseCustomReward
{
	private final Map<Player, PlayerGuessing> inGuess = new HashMap<>();

	public JarGuessReward()
	{
		super(CCubesCore.MODID + ":jar_guess", 25);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, final Player player, JsonObject settings)
	{
		if(inGuess.containsKey(player))
			return;

		if(!RewardsUtil.isPlayerOnline(player))
			return;

		int amount = RewardsUtil.rand.nextInt(50) + 60;

		RewardBlockCache blockCache = new RewardBlockCache(level, pos, player.getOnPos());
		for(int x = -3; x < 4; x++)
		{
			for(int z = -3; z < 4; z++)
			{
				for(int y = 0; y < 7; y++)
				{
					if(x == -3 || x == 3 || z == -3 || z == 3 || y == 0 || y == 6)
						blockCache.cacheBlock(new BlockPos(x, y, z), Blocks.GLASS.defaultBlockState());
					else
						blockCache.cacheBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
				}
			}
		}

		PlayerGuessing guessing = new PlayerGuessing(amount, blockCache);

		int batsSpawned = 0;
		for(int x = -1; x < 2; x++)
		{
			for(int z = -1; z < 2; z++)
			{
				for(int y = 2; y < 5; y++)
				{
					for(int i = 0; i < 4; i++)
					{
						if(batsSpawned < amount)
						{
							BlockPos pos2 = pos.offset(x + 0.5, y + 0.5, z + 0.5);
							Bat bat = EntityType.BAT.create(level);
							bat.moveTo(pos2.getX(), pos2.getY(), pos2.getZ(), 0, 0);
							level.addFreshEntity(bat);
							guessing.bats.add(bat);
							batsSpawned++;
						}
					}
				}
			}
		}

		RewardsUtil.sendMessageToPlayer(player, "How many bats are in UrjnaswX Jar?");
		RewardsUtil.sendMessageToPlayer(player, "You have 30 seconds to answer!");
		RewardsUtil.sendMessageToPlayer(player, "Guess right and you get 20 Emeralds!");
		RewardsUtil.sendMessageToPlayer(player, "But you lose 1 emerald per bat you are off!");

		inGuess.put(player, guessing);

		Scheduler.scheduleTask(new Task("Jar Guess", 600, 20)
		{
			@Override
			public void callback()
			{
				timeUp(player, -1);
			}

			@Override
			public void update()
			{
				if(!inGuess.containsKey(player))
					Scheduler.removeTask(this);

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, GuiTextLocation.ACTION_BAR);
			}

		});
	}

	private void timeUp(Player player, int guessed)
	{
		if(!inGuess.containsKey(player))
			return;

		int answer = this.inGuess.get(player).answer;
		if(guessed != -1)
		{
			RewardsUtil.sendMessageToPlayer(player, "The Correct answer was " + answer);
			RewardsUtil.sendMessageToPlayer(player, "You were off by " + Math.abs(answer - guessed));
			int winAmount = Math.max(0, 20 - Math.abs(answer - guessed));
			RewardsUtil.sendMessageToPlayer(player, "You won " + winAmount + " Emeralds!");
			player.getInventory().add(new ItemStack(Items.EMERALD, winAmount));
		}
		else
		{
			RewardsUtil.sendMessageToPlayer(player, "Times up! The answer was " + answer);
		}

		for(Bat bat : this.inGuess.get(player).bats)
			bat.remove(Entity.RemovalReason.DISCARDED);
		this.inGuess.get(player).blockCache.restoreBlocks(player);
		inGuess.remove(player);
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		Player player = event.getPlayer();

		if(inGuess.containsKey(player))
		{
			String answer = event.getMessage();
			if(answer.matches("[0-9]+"))
			{
				this.timeUp(player, Integer.parseInt(answer));
				event.setCanceled(true);
			}
			else
			{
				RewardsUtil.sendMessageToPlayer(player, answer + " is not a accepted number!");
			}
		}
	}

	private static class PlayerGuessing
	{
		public int answer;
		public RewardBlockCache blockCache;
		public List<Bat> bats = new ArrayList<>();

		public PlayerGuessing(int answer, RewardBlockCache blockCache)
		{
			this.answer = answer;
			this.blockCache = blockCache;
		}
	}
}