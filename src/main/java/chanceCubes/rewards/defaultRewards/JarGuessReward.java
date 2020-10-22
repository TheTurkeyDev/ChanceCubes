package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JarGuessReward extends BaseCustomReward
{
	private Map<PlayerEntity, PlayerGuessing> inGuess = new HashMap<>();

	public JarGuessReward()
	{
		super(CCubesCore.MODID + ":jar_guess", 25);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		if(inGuess.containsKey(player))
			return;

		if(!RewardsUtil.isPlayerOnline(player))
			return;

		int amount = world.rand.nextInt(50) + 60;

		RewardBlockCache blockCache = new RewardBlockCache(world, pos, player.getPosition());
		for(int x = -3; x < 4; x++)
		{
			for(int z = -3; z < 4; z++)
			{
				for(int y = 0; y < 7; y++)
				{
					if(x == -3 || x == 3 || z == -3 || z == 3 || y == 0 || y == 6)
						blockCache.cacheBlock(new BlockPos(x, y, z), Blocks.GLASS.getDefaultState());
					else
						blockCache.cacheBlock(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
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
							BlockPos pos2 = pos.add(x + 0.5, y + 0.5, z + 0.5);
							BatEntity bat = EntityType.BAT.create(world);
							bat.setLocationAndAngles(pos2.getX(), pos2.getY(), pos2.getZ(), 0, 0);
							world.addEntity(bat);
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
					this.showTimeLeft(player, STitlePacket.Type.ACTIONBAR);
			}

		});
	}

	private void timeUp(PlayerEntity player, int guessed)
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
			player.inventory.addItemStackToInventory(new ItemStack(Items.EMERALD, winAmount));
		}
		else
		{
			RewardsUtil.sendMessageToPlayer(player, "Times up! The answer was " + answer);
		}

		for(BatEntity batEntity : this.inGuess.get(player).bats)
			batEntity.remove();
		this.inGuess.get(player).blockCache.restoreBlocks(player);
		inGuess.remove(player);
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		PlayerEntity player = event.getPlayer();

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
		public List<BatEntity> bats = new ArrayList<>();

		public PlayerGuessing(int answer, RewardBlockCache blockCache)
		{
			this.answer = answer;
			this.blockCache = blockCache;
		}
	}
}