package chanceCubes.rewards;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import chanceCubes.CCubesCore;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MathReward implements IChanceCubeReward
{

	private Map<EntityPlayer, Entry<Integer, Entity>> inQuestion = new HashMap<EntityPlayer, Entry<Integer, Entity>>();

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		if(inQuestion.containsKey(player))
			return;

		int num1 = world.rand.nextInt(100);
		int num2 = world.rand.nextInt(100);

		player.addChatMessage(new ChatComponentText("Quick, what's " + num1 + "+" + num2 + "?"));

		player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 120, 5));

		if (!world.isRemote)
		{
			EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double)((float)player.posX + 0.5F), (double)((float)player.posY + 0.5F), (double)((float)player.posZ + 0.5F), player);
			world.spawnEntityInWorld(entitytntprimed);
			world.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
			entitytntprimed.fuse = 120;
			
			inQuestion.put(player, new CustomEntry<Integer, Entity>(num1+num2, entitytntprimed));
		}
		
		Task task = new Task()
		{
			@Override
			public void callback()
			{
				timeUp(player, false);
			}

		};
		
		Scheduler.scheduleTask("Math", 240, task);
	}

	private void timeUp(EntityPlayer player, boolean correct)
	{
		if(!inQuestion.containsKey(player))
			return;
		
		if(correct)
		{
			player.addChatMessage(new ChatComponentText("Correct!"));
			player.worldObj.removeEntity(inQuestion.get(player).getValue());
		}
		
		inQuestion.remove(player);

	}

	@Override
	public int getChanceValue()
	{
		return -5;
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
			}
			catch(NumberFormatException e)
			{
				player.addChatMessage(new ChatComponentText("Incorrect!"));
			}
			
			if(inQuestion.get(player).getKey() == answer)
				this.timeUp(player, true);
			else
				player.addChatMessage(new ChatComponentText("Incorrect!"));
			event.setCanceled(true);
		}
	}
}