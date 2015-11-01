package chanceCubes.rewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import chanceCubes.CCubesCore;
import chanceCubes.util.Location3I;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MathReward implements IChanceCubeReward
{

	private Map<EntityPlayer, RewardInfo> inQuestion = new HashMap<EntityPlayer, RewardInfo>();

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		if(inQuestion.containsKey(player))
			return;

		int num1 = world.rand.nextInt(100);
		int num2 = world.rand.nextInt(100);

		player.addChatMessage(new ChatComponentText("Quick, what's " + num1 + "+" + num2 + "?"));

		List<Location3I> boxBlocks = new ArrayList<Location3I>();
		for(int xx = -2; xx < 3; xx++)
		{
			for(int zz = -2; zz < 3; zz++)
			{
				for(int yy = 1; yy < 5; yy++)
				{
					if(xx == -2 || xx == 2 || zz == -2 || zz == 2 || yy == 1 || yy == 4)
					{
						world.setBlock((int) player.posX + xx, (int) player.posY + yy, (int) player.posZ + zz, Blocks.bedrock);
						boxBlocks.add(new Location3I((int) player.posX + xx, (int) player.posY + yy, (int) player.posZ + zz));
					}
					else if(((xx == -1 || xx == 1) && (zz == -1 || zz == 1) && yy == 2))
					{
						world.setBlock((int) player.posX + xx, (int) player.posY + yy, (int) player.posZ + zz, Blocks.glowstone);
						boxBlocks.add(new Location3I((int) player.posX + xx, (int) player.posY + yy, (int) player.posZ + zz));
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

		for(Location3I b : info.getBlocks())
			player.worldObj.setBlockToAir(b.getX(), b.getY(), b.getZ());

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
		private List<Location3I> blocks;

		public RewardInfo(int answer, Entity tnt, List<Location3I> blocks)
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

		public List<Location3I> getBlocks()
		{
			return blocks;
		}
	}
}