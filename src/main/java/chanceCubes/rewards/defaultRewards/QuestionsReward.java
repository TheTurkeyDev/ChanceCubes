package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageTypes;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.GuiTextLocation;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionsReward extends BaseCustomReward
{
	private final Map<Player, String> inQuestion = new HashMap<>();

	private final List<CustomEntry<String, String>> questionsAndAnswers = new ArrayList<>();

	public QuestionsReward()
	{
		super(CCubesCore.MODID + ":question", -30);
		this.addQuestionAnswer("What is the username of the creator of Chance Cubes?", "Turkey -or- Turkey2349 -or- TurkeyDev");
		this.addQuestionAnswer("How many sides does the sparkly, shiny, colorful, spinny Chance Cube have?", "20");
		this.addQuestionAnswer("What is 9 + 10", "19 -or- 21");
		this.addQuestionAnswer("What year was minecraft officially released", "2011");
		this.addQuestionAnswer("What company developes Java?", "Sun -or- Sun Microsystems -or- Oracle");
		this.addQuestionAnswer("Who created Minecraft?", "Notch");
		// this.addQuestionAnswer("What is the air-speed velocity of an unladen European swallow?", "24 -or- 11 -or- 11m/s -or- 24mph -or- 11 m/s -or- 24 mph");
	}

	public void addQuestionAnswer(String q, String a)
	{
		questionsAndAnswers.add(new CustomEntry<>(q, a));
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		if(inQuestion.containsKey(player))
			return;

		if(!RewardsUtil.isPlayerOnline(player))
			return;

		int question = RewardsUtil.rand.nextInt(questionsAndAnswers.size());

		RewardsUtil.sendMessageToPlayer(player, questionsAndAnswers.get(question).getKey());
		RewardsUtil.sendMessageToPlayer(player, "You have 20 seconds to answer! (Answer is not case sensitive)");

		inQuestion.put(player, questionsAndAnswers.get(question).getValue());

		Scheduler.scheduleTask(new Task("Question", 400, 20)
		{
			@Override
			public void callback()
			{
				timeUp(player, false);
			}

			@Override
			public void update()
			{
				if(!inQuestion.containsKey(player))
					Scheduler.removeTask(this);

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, GuiTextLocation.ACTION_BAR);
			}

		});
	}

	private void timeUp(Player player, boolean correct)
	{
		if(!inQuestion.containsKey(player))
			return;

		if(correct)
		{
			RewardsUtil.sendMessageToPlayer(player, "Correct!");
			RewardsUtil.sendMessageToPlayer(player, "Here, have a item!");
			player.level().addFreshEntity(new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
		}
		else
		{
			RewardsUtil.sendMessageToPlayer(player, "Incorrect! The answer was " + this.inQuestion.get(player));
			player.level().explode(player, player.getX(), player.getY(), player.getZ(), 1.0F, Level.ExplosionInteraction.NONE);
			player.hurt(player.damageSources().source(CCubesDamageTypes.QUESTION_FAIL), Float.MAX_VALUE);
		}

		inQuestion.remove(player);

	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		Player player = event.getPlayer();

		if(inQuestion.containsKey(player))
		{
			String answer = event.getRawText();
			boolean correct = false;
			for(String s : inQuestion.get(player).split("-or-"))
			{
				if(s.trim().equalsIgnoreCase(answer.trim()))
				{
					correct = true;
					break;
				}
			}
			this.timeUp(player, correct);
			event.setCanceled(true);
		}
	}
}