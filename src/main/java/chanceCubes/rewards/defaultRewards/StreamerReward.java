package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.parsers.RewardParser;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.HTTPUtil;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamerReward
{
	private Socket socket;
	private BufferedWriter writter;
	private String channel;

	private boolean connectedToChat = false;

	private List<String> voted = new ArrayList<>();
	private List<Option> options = new ArrayList<>();
	private int timeLeft;

	public StreamerReward(String channel, JsonArray jsonOptions)
	{
		this.channel = "#" + channel;
		for(JsonElement option : jsonOptions)
		{
			String display = option.getAsJsonObject().get("display").getAsString();
			JsonObject reward = option.getAsJsonObject().getAsJsonObject("reward");
			for(Map.Entry<String, JsonElement> entry : reward.getAsJsonObject().entrySet())
			{
				BaseCustomReward theReward;
				if(entry.getKey().equals("chat_invades"))
					theReward = new ChatInvadeReward(channel);
				else
					theReward = RewardParser.parseReward(entry).getKey();

				options.add(new Option(display, theReward, 0));
				break;
			}

		}
	}

	public boolean trigger(World world, BlockPos pos, PlayerEntity player)
	{
		try
		{
			if(!connectedToChat)
				initTwitchChatconnection();
		} catch(Exception e)
		{
			player.sendMessage(new StringTextComponent("An issue has occurred with this reward! Code: 0x545749544348"));
			e.printStackTrace();
			return false;
		}

		timeLeft = 45;
		voted.clear();
		for(Option opt : options)
			opt.votes = 0;

		Scheduler.scheduleTask(new Task("Streamer_Reward", -1, 20)
		{
			int step = 0;

			@Override
			public void callback()
			{

			}

			public void update()
			{
				step++;
				StringTextComponent message;

				switch(step)
				{
					case 1:
						message = new StringTextComponent("Hey Twitch Chat!");
						message.setStyle(new Style().setColor(TextFormatting.DARK_PURPLE));
						RewardsUtil.setPlayerTitle(player, STitlePacket.Type.TITLE, message, 10, 60, 10);
						break;
					case 6:
						message = new StringTextComponent("Let's Play A Game!");
						message.setStyle(new Style().setColor(TextFormatting.DARK_PURPLE));
						RewardsUtil.setPlayerTitle(player, STitlePacket.Type.TITLE, message, 10, 60, 10);
						break;
					case 11:
						message = new StringTextComponent("Decide My Fate!");
						message.setStyle(new Style().setColor(TextFormatting.DARK_PURPLE));
						RewardsUtil.setPlayerTitle(player, STitlePacket.Type.TITLE, message, 10, 60, 10);
						break;
				}

				if(step > 15)
				{
					updateTitle(player);
					timeLeft--;
				}

				if(timeLeft == 0)
				{
					Scheduler.removeTask(this);

					Option winner = options.get(0);
					for(Option opt : options)
						if(opt.votes > winner.votes)
							winner = opt;

					player.sendMessage(new StringTextComponent(winner.display + " Has won!"));
					winner.reward.trigger(world, player.getPosition(), player, new HashMap<>());
					disconnect();
				}
			}
		});
		return true;
	}

	private void initTwitchChatconnection() throws IOException
	{
		socket = new Socket("irc.chat.twitch.tv", 6667);
		writter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		connectedToChat = true;

		Thread thread = new Thread(() ->
		{
			try
			{
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader breader = new BufferedReader(inputStreamReader);
				String line;
				while((line = breader.readLine()) != null && connectedToChat)
				{
					int firstSpace = line.indexOf(" ");
					int secondSpace = line.indexOf(" ", firstSpace + 1);
					if(secondSpace >= 0)
					{
						String from = line.substring(0, firstSpace);
						String code = line.substring(firstSpace + 1, secondSpace);
						String rest = line.substring(secondSpace + 1);

						switch(code)
						{
							case "PRIVMSG":
								if(voted.contains(from))
									break;

								String message = rest.substring(rest.indexOf(" ") + 2);
								if(IntVar.isInteger(message))
								{
									int choice = Integer.parseInt(message) - 1;
									if(choice >= 0 && choice < 5)
									{
										options.get(choice).votes++;
										voted.add(from);
									}
								}
								break;
							default:
								//System.out.println(">>> " + line);
								break;
						}
					}
				}
			} catch(Exception e)
			{
				if(!(e instanceof SocketException))
					e.printStackTrace();
			}

			disconnect();
		});

		sendString("PASS PASSWORD");
		sendString("NICK justinfan2349");
		sendString("JOIN " + channel);

		thread.start();

		try
		{
			HTTPUtil.getWebFile("POST", "http://api.theprogrammingturkey.com/chance_cubes/triggerStreamerReward.php", new CustomEntry<>("channel", channel));
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "FAILED TO TRIGGER THE CHAT BOT!");
			e.printStackTrace();
		}
	}

	private void disconnect()
	{
		this.connectedToChat = false;
		try
		{
			socket.close();
			writter.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void updateTitle(PlayerEntity player)
	{
		StringBuilder sb = new StringBuilder();
		for(Option option : options)
			sb.append(option.votes).append(" - ");
		sb.delete(sb.length() - 2, sb.length());

		StringTextComponent message = new StringTextComponent(sb.toString());
		message.setStyle(new Style().setColor(TextFormatting.DARK_PURPLE));
		RewardsUtil.setPlayerTitle(player, STitlePacket.Type.TITLE, message, 0, 40, 0);
		message = new StringTextComponent("Time Left: " + timeLeft);
		message.setStyle(new Style().setColor(TextFormatting.DARK_PURPLE));
		RewardsUtil.setPlayerTitle(player, STitlePacket.Type.SUBTITLE, message, 0, 40, 0);

	}

	private void sendString(String str)
	{
		try
		{
			if(writter == null || !connectedToChat)
				initTwitchChatconnection();

			writter.write(str + "\r\n");
			writter.flush();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static class Option
	{
		public String display;
		public BaseCustomReward reward;
		public int votes;

		public Option(String display, BaseCustomReward reward, int votes)
		{
			this.display = display;
			this.reward = reward;
			this.votes = votes;
		}
	}

	private static class ChatInvadeReward extends BaseCustomReward
	{
		private String channel;

		public ChatInvadeReward(String channel)
		{
			super("chatInvades", 0);
			this.channel = channel;
		}

		@Override
		public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
		{
			try
			{
				JsonObject json = HTTPUtil.getWebFile("GET", "http://tmi.twitch.tv/group/user/" + channel.substring(1) + "/chatters").getAsJsonObject();
				for(Map.Entry<String, JsonElement> entry : json.get("chatters").getAsJsonObject().entrySet())
				{
					if(entry.getValue().isJsonArray())
					{
						for(JsonElement user : entry.getValue().getAsJsonArray())
						{
							if(user.isJsonPrimitive())
							{
								ZombieEntity zombie = EntityType.ZOMBIE.create(world);
								if(zombie != null)
								{
									zombie.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
									zombie.setCustomName(new StringTextComponent(user.getAsString()));
									zombie.setCustomNameVisible(true);
									world.addEntity(zombie);
								}
							}
						}
					}
				}
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
