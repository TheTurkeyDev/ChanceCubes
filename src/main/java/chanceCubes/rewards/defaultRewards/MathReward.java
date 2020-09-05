package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.server.ServerWorld;
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

	private Map<PlayerEntity, RewardInfo> inQuestion = new HashMap<>();

	@Override
	public void trigger(ServerWorld world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		if(inQuestion.containsKey(player))
			return;

		int num1 = world.rand.nextInt(100);
		int num2 = world.rand.nextInt(100);

		RewardsUtil.sendMessageToPlayer(player, "Quick, what's " + num1 + "+" + num2 + "?");

		BlockPos playerPos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
		RewardBlockCache cache = new RewardBlockCache(world, playerPos, player.getPosition());
		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				for(int yy = 1; yy < 5; yy++)
				{
					if(xx == -2 || xx == 2 || zz == -2 || zz == 2 || yy == 1 || yy == 4)
						cache.cacheBlock(new BlockPos(xx, yy, zz), Blocks.BEDROCK.getDefaultState());
					else if(((xx == -1 || xx == 1) && (zz == -1 || zz == 1) && yy == 2))
						cache.cacheBlock(new BlockPos(xx, yy, zz), Blocks.GLOWSTONE.getDefaultState());
				}
			}
		}

		player.setPositionAndUpdate((int) player.getPosX(), ((int) player.getPosY()) + 2, (int) player.getPosZ());
		List<Entity> tnt = new ArrayList<>();
		for(int i = 0; i < 5; i++)
		{
			TNTEntity entitytntprimed = new TNTEntity(world, player.getPosX(), player.getPosY() + 1D, player.getPosZ(), player);
			world.addEntity(entitytntprimed);
			world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
			entitytntprimed.setFuse(140);
			tnt.add(entitytntprimed);
		}

		inQuestion.put(player, new RewardInfo(num1 + num2, tnt, cache));

		int duration = super.getSettingAsInt(settings, "ans_duration", 100, 20, 2400);

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
					this.showTimeLeft(player, STitlePacket.Type.ACTIONBAR);
			}

		});
	}

	private void timeUp(PlayerEntity player, boolean correct)
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
			player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
		}
		else
		{
			player.world.createExplosion(player, player.getPosX(), player.getPosY(), player.getPosZ(), 1.0F, Explosion.Mode.NONE);
			player.attackEntityFrom(CCubesDamageSource.MATH_FAIL, Float.MAX_VALUE);
		}

		for(Entity tnt : info.tnt)
			tnt.remove();

		info.cache.restoreBlocks(player);

		inQuestion.remove(player);

	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		PlayerEntity player = event.getPlayer();

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

	private static class RewardInfo
	{
		public int answer;
		public List<Entity> tnt;
		public RewardBlockCache cache;

		public RewardInfo(int answer, List<Entity> tnt, RewardBlockCache cache)
		{
			this.answer = answer;
			this.tnt = tnt;
			this.cache = cache;
		}
	}
}