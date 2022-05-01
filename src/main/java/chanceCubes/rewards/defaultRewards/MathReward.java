package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.GuiTextLocation;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathReward extends BaseCustomReward
{
	public MathReward()
	{
		super(CCubesCore.MODID + ":math", -30);
	}

	private final Map<Player, RewardInfo> inQuestion = new HashMap<>();

	@Override
	public void trigger(ServerLevel level, BlockPos pos, final Player player, JsonObject settings)
	{
		if(inQuestion.containsKey(player))
			return;

		int num1 = RewardsUtil.rand.nextInt(100);
		int num2 = RewardsUtil.rand.nextInt(100);

		RewardsUtil.sendMessageToPlayer(player, "Quick, what's " + num1 + "+" + num2 + "?");

		BlockPos playerPos = new BlockPos(player.getX(), player.getY(), player.getZ());
		RewardBlockCache cache = new RewardBlockCache(level, playerPos, player.getOnPos());
		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				for(int yy = 1; yy < 5; yy++)
				{
					if(xx == -2 || xx == 2 || zz == -2 || zz == 2 || yy == 1 || yy == 4)
						cache.cacheBlock(new BlockPos(xx, yy, zz), Blocks.BEDROCK.defaultBlockState());
					else if(((xx == -1 || xx == 1) && (zz == -1 || zz == 1) && yy == 2))
						cache.cacheBlock(new BlockPos(xx, yy, zz), Blocks.GLOWSTONE.defaultBlockState());
				}
			}
		}

		player.moveTo((int) player.getX(), ((int) player.getY()) + 2, (int) player.getZ());
		List<Entity> tnt = new ArrayList<>();
		for(int i = 0; i < 5; i++)
		{
			PrimedTnt entitytntprimed = new PrimedTnt(level, player.getX(), player.getY() + 1D, player.getZ(), player);
			level.addFreshEntity(entitytntprimed);
			level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
			entitytntprimed.setFuse(140);
			tnt.add(entitytntprimed);
		}

		inQuestion.put(player, new RewardInfo(num1 + num2, tnt, cache));

		int duration = super.getSettingAsInt(settings, "answerDuration", 100, 20, 2400);

		Scheduler.scheduleTask(new Task("Math", duration, 20)
		{
			@Override
			public void callback()
			{
				timeUp(player, false);
			}

			@Override
			public void update()
			{
				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, GuiTextLocation.ACTION_BAR);
			}

		});
	}

	private void timeUp(Player player, boolean correct)
	{
		if(!inQuestion.containsKey(player))
			return;

		if(!RewardsUtil.isPlayerOnline(player))
			return;

		RewardInfo info = inQuestion.get(player);
		if(correct)
		{
			RewardsUtil.sendMessageToPlayer(player, "Correct!");
			RewardsUtil.sendMessageToPlayer(player, "Here, have a item!");
			player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
		}
		else
		{
			player.level.explode(player, player.getX(), player.getY(), player.getZ(), 1.0F, Explosion.BlockInteraction.NONE);
			player.die(CCubesDamageSource.MATH_FAIL);
		}

		for(Entity tnt : info.tnt)
			tnt.remove(Entity.RemovalReason.DISCARDED);

		info.cache.restoreBlocks(player);

		inQuestion.remove(player);

	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		Player player = event.getPlayer();

		if(inQuestion.containsKey(player))
		{
			int answer = 0;
			try
			{
				answer = Integer.parseInt(event.getMessage());
			} catch(NumberFormatException e)
			{
				RewardsUtil.sendMessageToPlayer(player, "Incorrect!");
			}

			if(inQuestion.get(player).answer == answer)
				this.timeUp(player, true);
			else
				RewardsUtil.sendMessageToPlayer(player, "Incorrect!");
			event.setCanceled(true);
		}
	}

	private record RewardInfo(int answer, List<Entity> tnt, RewardBlockCache cache)
	{
	}
}