package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionsReward extends BaseCustomReward
{
	private Map<PlayerEntity, String> inQuestion = new HashMap<>();

	private List<CustomEntry<String, String>> questionsAndAnswers = new ArrayList<>();

	public QuestionsReward()
	{
		super(CCubesCore.MODID + ":question", -30);
		this.addQuestionAnswer("What is the username of the creator of Chance Cubes?", "Turkey -or- Turkey2349");
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
	public void trigger(World world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		if(inQuestion.containsKey(player))
			return;

		if(!RewardsUtil.isPlayerOnline(player))
			return;

		int question = world.rand.nextInt(questionsAndAnswers.size());

		player.sendMessage(new StringTextComponent(questionsAndAnswers.get(question).getKey()));
		player.sendMessage(new StringTextComponent("You have 20 seconds to answer! (Answer is not case sensitive)"));

		if(!world.isRemote)
		{
			inQuestion.put(player, questionsAndAnswers.get(question).getValue());
		}

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
					this.showTimeLeft(player, STitlePacket.Type.ACTIONBAR);
			}

		});
	}

	private void timeUp(PlayerEntity player, boolean correct)
	{
		if(!inQuestion.containsKey(player))
			return;

		if(correct)
		{
			player.sendMessage(new StringTextComponent("Correct!"));
			player.sendMessage(new StringTextComponent("Here, have a item!"));
			player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
		}
		else
		{
			player.sendMessage(new StringTextComponent("Incorrect! The answer was " + this.inQuestion.get(player)));
			player.world.createExplosion(player, player.getPosX(), player.getPosY(), player.getPosZ(), 1.0F, Explosion.Mode.NONE);
			player.attackEntityFrom(CCubesDamageSource.QUESTION_FAIL, Float.MAX_VALUE);
		}

		inQuestion.remove(player);

	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		PlayerEntity player = event.getPlayer();

		if(inQuestion.containsKey(player))
		{
			String answer = event.getMessage();
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