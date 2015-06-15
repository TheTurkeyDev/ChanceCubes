package chanceCubes.rewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
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
	private boolean staying = true;
	private String[] leaveSayings = new String[] {"I will be back for you.", "Another day, another time.", "No, you are not ready for my wrath." };
	private String[] staySayings = new String[] {"Today is the day.", "May the other world have mercy on your soul.", "MUWAHAHAHAHAHAHAH", "Time to feast!!" };
	private EntityPlayer player;
	private World world;
	private int x, y, z;

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		if(triggered)
			return;
		triggered = true;
		stage = 0;
		//staying = world.rand.nextInt(50) == 1;
		this.player = player;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		update();
	}

	private void schedule()
	{
		Task task = new Task()
		{
			@Override
			public void callback()
			{
				update();
			}

		};

		Scheduler.scheduleTask("Herobrine Reward", 40, task);
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
					player.addChatMessage(new ChatComponentText("[Herobrine] " + staySayings[world.rand.nextInt(staySayings.length)]));
				else
					player.addChatMessage(new ChatComponentText("[Herobrine] " + leaveSayings[world.rand.nextInt(leaveSayings.length)]));
				break;
			}
			case 2:
			{
				if(staying)
				{
					//String command = "/summon Zombie ~ ~1 ~ {Riding:{id:ThrownPotion,Potion:{id:15,Damage:16,tag:{CustomPotionEffects:[{Id:15,Amplifier:0,Duration:2400,ShowParticles:0b}]}}},CustomName:\"Herobrine\",CustomNameVisible:1,CanBreakDoors:1,Equipment:[{id:278,Count:1,tag:{ench:[{id:32,lvl:5}]}},{},{},{},{id:397,Damage:3,Count:1,tag:{SkullOwner:Herobrine,ench:[{id:7,lvl:16}]}}],DropChances:[1F,1F,1F,1F,0.0F],Attributes:[{Name:zombie.spawnReinforcements,Base:1.0F}],ActiveEffects:[{Id:5,Amplifier:15,Duration:199980,ShowParticles:0b},{Id:11,Amplifier:10,Duration:2400,ShowParticles:0b}],Attributes:[{Name:generic.maxHealth,Base:200}],Health:200}";
					String command = "/summon Zombie ~ ~ ~ {Equipment:[{},{},{},{},{id:397,Damage:3,tag:{SkullOwner:\"Herobrine\"}}]}";
					CCubesCommandSender sender = new CCubesCommandSender(player, x, y, z);
		        	MinecraftServer.getServer().getCommandManager().executeCommand(sender, command);
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
		return -60;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Herobrine";
	}

}
