package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;

public class QuestionsReward implements IChanceCubeReward
{

	private Map<EntityPlayer, String> inQuestion = new HashMap<EntityPlayer, String>();
	
	private List<CustomEntry<String, String>> questionsAndAnswers = new ArrayList<CustomEntry<String, String>>();
	
	public QuestionsReward()
	{
		this.addQuestionAnswer("What is the username of the creator of Chance Cubes?", "Turkey2349");
		this.addQuestionAnswer("How many sides does the sparkly, shiny, colorful, spinny Chance Cube have?", "20");
		this.addQuestionAnswer("What is 9 + 10", "19");
		this.addQuestionAnswer("What year was minecraft officially released", "2011");
	}
	
	public void addQuestionAnswer(String q, String a)
	{
		questionsAndAnswers.add(new CustomEntry<String, String>(q, a));
	}

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		if(inQuestion.containsKey(player))
			return;

		int question = world.rand.nextInt(questionsAndAnswers.size());

		player.addChatMessage(new ChatComponentText(questionsAndAnswers.get(question).getKey()));
		player.addChatMessage(new ChatComponentText("You have 45 seconds to answer! (Answer is not case sensitive)"));


		if(!world.isRemote)
		{
			inQuestion.put(player, questionsAndAnswers.get(question).getValue());
		}

		Task task = new Task("Question", 900)
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

		if(correct)
		{
			player.addChatMessage(new ChatComponentText("Correct!"));
		}
		else
		{
			player.addChatMessage(new ChatComponentText("Incorrect! The answer was " + this.inQuestion.get(player)));
			player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, false);
			player.attackEntityFrom(CCubesDamageSource.questionfail, Float.MAX_VALUE);
		}

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
		return CCubesCore.MODID + ":Question";
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		EntityPlayer player = event.player;

		if(inQuestion.containsKey(player))
		{
			String answer = event.message;
			this.timeUp(player, inQuestion.get(player).equalsIgnoreCase(answer));
			event.setCanceled(true);
		}
	}
}