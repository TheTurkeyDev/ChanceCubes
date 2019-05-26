package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MathReward extends BaseCustomReward
{
	public MathReward()
	{
		super(CCubesCore.MODID + ":Math", -30);
	}

	private Map<EntityPlayer, RewardInfo> inQuestion = new HashMap<EntityPlayer, RewardInfo>();

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player, Map<String, Object> settings)
	{
		if(inQuestion.containsKey(player))
			return;

		int num1 = world.rand.nextInt(100);
		int num2 = world.rand.nextInt(100);

		player.sendMessage(new TextComponentString("Quick, what's " + num1 + "+" + num2 + "?"));

		BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
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

		player.setPositionAndUpdate((int) player.posX, ((int) player.posY) + 2, (int) player.posZ);

		if(!world.isRemote)
		{
			List<Entity> tnt = new ArrayList<Entity>();
			for(int i = 0; i < 5; i++)
			{
				EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, player.posX, player.posY + 1D, player.posZ, player);
				world.spawnEntity(entitytntprimed);
				world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
				entitytntprimed.setFuse(140);
				tnt.add(entitytntprimed);
			}

			inQuestion.put(player, new RewardInfo(num1 + num2, tnt, cache));
		}

		Scheduler.scheduleTask(new Task("Math", 100, 20)
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
					this.showTimeLeft(player, Type.ACTIONBAR);
			}

		});
	}

	private void timeUp(EntityPlayer player, boolean correct)
	{
		if(!inQuestion.containsKey(player))
			return;

		if(!RewardsUtil.isPlayerOnline(player))
			return;

		RewardInfo info = inQuestion.get(player);
		if(correct)
		{
			player.sendMessage(new TextComponentString("Correct!"));
			player.sendMessage(new TextComponentString("Here, have a item!"));
			player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
		}
		else
		{
			player.world.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, false);
			player.attackEntityFrom(CCubesDamageSource.MATH_FAIL, Float.MAX_VALUE);
		}

		for(Entity tnt : info.tnt)
			tnt.setDead();

		info.cache.restoreBlocks(player);

		inQuestion.remove(player);

	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		EntityPlayer player = event.getPlayer();

		if(inQuestion.containsKey(player))
		{
			int answer = 0;
			try
			{
				answer = Integer.parseInt(event.getMessage());
			} catch(NumberFormatException e)
			{
				player.sendMessage(new TextComponentString("Incorrect!"));
			}

			if(inQuestion.get(player).answer == answer)
				this.timeUp(player, true);
			else
				player.sendMessage(new TextComponentString("Incorrect!"));
			event.setCanceled(true);
		}
	}

	private class RewardInfo
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