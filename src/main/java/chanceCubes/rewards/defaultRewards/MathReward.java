package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MathReward implements IChanceCubeReward
{

	private Map<EntityPlayer, RewardInfo> inQuestion = new HashMap<EntityPlayer, RewardInfo>();

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		if(inQuestion.containsKey(player))
			return;

		int num1 = world.rand.nextInt(100);
		int num2 = world.rand.nextInt(100);

		player.addChatMessage(new TextComponentString("Quick, what's " + num1 + "+" + num2 + "?"));

		BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
		List<BlockPos> boxBlocks = new ArrayList<BlockPos>();
		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				for(int yy = 1; yy < 5; yy++)
				{
					if(xx == -2 || xx == 2 || zz == -2 || zz == 2 || yy == 1 || yy == 4)
					{
						world.setBlockState(playerPos.add(xx, yy, zz), Blocks.BED.getDefaultState());
						boxBlocks.add(new BlockPos(player.posX + xx, player.posY + yy, player.posZ + zz));
					}
					else if(((xx == -1 || xx == 1) && (zz == -1 || zz == 1) && yy == 2))
					{
						world.setBlockState(playerPos.add(xx, yy, zz), Blocks.GLOWSTONE.getDefaultState());
						boxBlocks.add(new BlockPos(player.posX + xx, player.posY + yy, player.posZ + zz));
					}
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
				world.spawnEntityInWorld(entitytntprimed);
				world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
				entitytntprimed.setFuse(140);
				tnt.add(entitytntprimed);
			}

			inQuestion.put(player, new RewardInfo(num1 + num2, tnt, boxBlocks));
		}

		Task task = new Task("Math", 100)
		{
			@Override
			public void callback()
			{
				timeUp(player, false);
			}

		};

		Scheduler.scheduleTask(task);
	}

	private void timeUp(EntityPlayer player, boolean correct)
	{
		if(!inQuestion.containsKey(player))
			return;

		RewardInfo info = inQuestion.get(player);
		if(correct)
		{
			player.addChatMessage(new TextComponentString("Correct!"));
		}
		else
		{
			player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, false);
			player.attackEntityFrom(CCubesDamageSource.mathfail, Float.MAX_VALUE);
		}

		for(Entity tnt : info.getTnt())
			tnt.setDead();

		for(BlockPos b : info.getBlocks())
			player.worldObj.setBlockToAir(b);

		inQuestion.remove(player);

	}

	@Override
	public int getChanceValue()
	{
		return -30;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Math";
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
				player.addChatMessage(new TextComponentString("Incorrect!"));
			}

			if(inQuestion.get(player).getAnswer() == answer)
				this.timeUp(player, true);
			else
				player.addChatMessage(new TextComponentString("Incorrect!"));
			event.setCanceled(true);
		}
	}

	private class RewardInfo
	{
		private int answer;
		private List<Entity> tnt;
		private List<BlockPos> blocks;

		public RewardInfo(int answer, List<Entity> tnt, List<BlockPos> blocks)
		{
			this.answer = answer;
			this.tnt = tnt;
			this.blocks = blocks;
		}

		public int getAnswer()
		{
			return answer;
		}

		public List<Entity> getTnt()
		{
			return tnt;
		}

		public List<BlockPos> getBlocks()
		{
			return blocks;
		}
	}
}