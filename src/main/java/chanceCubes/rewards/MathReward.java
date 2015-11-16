package chanceCubes.rewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

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

		player.addChatMessage(new ChatComponentText("Quick, what's " + num1 + "+" + num2 + "?"));

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
						world.setBlockState(playerPos.add(xx, yy, zz), Blocks.bedrock.getDefaultState());
						boxBlocks.add(new BlockPos(player.posX + xx, player.posY + yy, player.posZ + zz));
					}
					else if(((xx == -1 || xx == 1) && (zz == -1 || zz == 1) && yy == 2))
					{
						world.setBlockState(playerPos.add(xx, yy, zz), Blocks.glowstone.getDefaultState());
						boxBlocks.add(new BlockPos(player.posX + xx, player.posY + yy, player.posZ + zz));
					}
				}
			}
		}

		player.setPositionAndUpdate((int) player.posX, ((int) player.posY) + 2, (int) player.posZ);

		if(!world.isRemote)
		{
			EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, player.posX, player.posY + 1D, player.posZ, player);
			world.spawnEntityInWorld(entitytntprimed);
			world.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
			entitytntprimed.fuse = 120;

			inQuestion.put(player, new RewardInfo(num1 + num2, entitytntprimed, boxBlocks));
		}

		Task task = new Task("Math", 140)
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
			player.addChatMessage(new ChatComponentText("Correct!"));
			info.getTnt().setDead();
		}

		for(BlockPos b : info.getBlocks())
			player.worldObj.setBlockToAir(b);

		inQuestion.remove(player);

	}

	@Override
	public int getChanceValue()
	{
		return -35;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Math";
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		EntityPlayer player = event.player;

		if(inQuestion.containsKey(player))
		{
			int answer = 0;
			try
			{
				answer = Integer.parseInt(event.message);
			} catch(NumberFormatException e)
			{
				player.addChatMessage(new ChatComponentText("Incorrect!"));
			}

			if(inQuestion.get(player).getAnswer() == answer)
				this.timeUp(player, true);
			else
				player.addChatMessage(new ChatComponentText("Incorrect!"));
			event.setCanceled(true);
		}
	}

	private class RewardInfo
	{
		private int answer;
		private Entity tnt;
		private List<BlockPos> blocks;

		public RewardInfo(int answer, Entity tnt, List<BlockPos> blocks)
		{
			this.answer = answer;
			this.tnt = tnt;
			this.blocks = blocks;
		}

		public int getAnswer()
		{
			return answer;
		}

		public Entity getTnt()
		{
			return tnt;
		}

		public List<BlockPos> getBlocks()
		{
			return blocks;
		}
	}
}