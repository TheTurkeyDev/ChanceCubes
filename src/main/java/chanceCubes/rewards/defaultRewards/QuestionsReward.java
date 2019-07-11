package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class QuestionsReward extends BaseCustomReward
{
	private Map<EntityPlayer, String> inQuestion = new HashMap<EntityPlayer, String>();

	private List<CustomEntry<String, String>> questionsAndAnswers = new ArrayList<CustomEntry<String, String>>();

	public QuestionsReward()
	{
		super(CCubesCore.MODID + ":Question", -30);
		this.addQuestionAnswer("What is the username of the creator of Chance Cubes?", "Turkey -or- Turkey2349");
		this.addQuestionAnswer("How many sides does the sparkly, shiny, colorful, spinny Chance Cube have?", "20");
		this.addQuestionAnswer("What is 9 + 10", "19 -or- 21");
		this.addQuestionAnswer("What year was minecraft officially released", "2011");
		this.addQuestionAnswer("What company developes Java?", "Sun -or- Sun Microsystems -or- Oracle");
		this.addQuestionAnswer("Who created Minecraft?", "Notch");
		//this.addQuestionAnswer("What is the air-speed velocity of an unladen European swallow?", "24 -or- 11 -or- 11m/s -or- 24mph -or- 11 m/s -or- 24 mph");
	}

	public void addQuestionAnswer(String q, String a)
	{
		questionsAndAnswers.add(new CustomEntry<String, String>(q, a));
	}

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player, Map<String, Object> settings)
	{
		if(inQuestion.containsKey(player))
			return;

		if(!RewardsUtil.isPlayerOnline(player))
			return;

		int question = world.rand.nextInt(questionsAndAnswers.size());

		player.sendMessage(new TextComponentString(questionsAndAnswers.get(question).getKey()));
		player.sendMessage(new TextComponentString("You have 20 seconds to answer! (Answer is not case sensitive)"));

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
					this.showTimeLeft(player, Type.ACTIONBAR);
			}

		});
	}

	private void timeUp(EntityPlayer player, boolean correct)
	{
		if(!inQuestion.containsKey(player))
			return;

		if(correct)
		{
			player.sendMessage(new TextComponentString("Correct!"));
			player.sendMessage(new TextComponentString("Here, have a item!"));
			player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
		}
		else
		{
			player.sendMessage(new TextComponentString("Incorrect! The answer was " + this.inQuestion.get(player)));
			player.world.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, false);
			player.attackEntityFrom(CCubesDamageSource.QUESTION_FAIL, Float.MAX_VALUE);
		}

		inQuestion.remove(player);

	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		EntityPlayer player = event.getPlayer();

		if(inQuestion.containsKey(player))
		{
			String answer = event.getMessage();
			boolean correct = false;
			for(String s : inQuestion.get(player).split("-or-"))
				if(s.trim().equalsIgnoreCase(answer.trim()))
					correct = true;
			this.timeUp(player, correct);
			event.setCanceled(true);
		}
	}
}