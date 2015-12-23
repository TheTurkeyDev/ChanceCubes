package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesCommandSender;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class HerobrineReward implements IChanceCubeReward
{
	private boolean triggered = false;
	private int stage = 0;
	private boolean staying = false;
	private String[] leaveSayings = new String[] {"I will be back for you.", "Another day, another time.", "No, you are not ready for my wrath.", "Perhaps tomorrow you will be worthy of my challenge", "I sense that I am needed else where. You escape..... For now....", "If only you were worth my time."};
	private String[] staySayings = new String[] {"Today is the day.", "May the other world have mercy on your soul.", "MUWAHAHAHAHAHAHAH", "Time to feast!!", "How fast can your run boy!", "It's a shame this will end so quickly for you.", "My presence alone will be your end"};
	private EntityPlayer player;
	private World world;
	private int x, y, z;

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		if(triggered)
			return;
		triggered = true;
		stage = 0;
		staying = world.rand.nextInt(10) == 1;
		this.player = player;
		this.world = world;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		update();
	}

	private void schedule()
	{
		Task task = new Task("Herobrine Reward", 40)
		{
			@Override
			public void callback()
			{
				update();
			}

		};

		Scheduler.scheduleTask(task);
	}

	private void update()
	{
		switch(stage)
		{
			case 0:
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Herobrine joined the game."));
				break;
			}
			case 1:
			{
				if(staying)
					player.addChatMessage(new ChatComponentText("<Herobrine> " + staySayings[world.rand.nextInt(staySayings.length)]));
				else
					player.addChatMessage(new ChatComponentText("<Herobrine> " + leaveSayings[world.rand.nextInt(leaveSayings.length)]));
				break;
			}
			case 2:
			{
				if(staying)
				{
					Boolean rule = MinecraftServer.getServer().worldServers[0].getGameRules().getGameRuleBooleanValue("commandBlockOutput");
					MinecraftServer.getServer().worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
					String command = "/summon Zombie ~ ~1 ~ {Riding:{id:ThrownPotion,Potion:{id:15,Damage:16,tag:{CustomPotionEffects:[{Id:15,Amplifier:0,Duration:3000,ShowParticles:0b}]}}},CustomName:\"Herobrine\",CustomNameVisible:1,CanBreakDoors:1,Equipment:[{id:276,Count:1},{},{},{},{id:397,Damage:3,Count:1,tag:{SkullOwner:Herobrine}}],DropChances:[1F,1F,1F,1F,0.0F],Attributes:[{Name:zombie.spawnReinforcements,Base:1.0F}],ActiveEffects:[{Id:5,Amplifier:15,Duration:199980,ShowParticles:0b}, {Id:10,Amplifier:50,Duration:500,ShowParticles:0b}],Attributes:[{Name:generic.maxHealth,Base:500}],Health:500}";
					CCubesCommandSender sender = new CCubesCommandSender(player, x, y, z);
		        	MinecraftServer.getServer().getCommandManager().executeCommand(sender, command);
		        	MinecraftServer.getServer().worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString());
				}
				else
				{
					player.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Herobrine left the game."));
				}
				break;
			}
		}

		stage++;

		if(stage < 3)
			schedule();
		else
			this.triggered = false;
	}

	@Override
	public int getChanceValue()
	{
		return -85;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Herobrine";
	}
}