package chanceCubes.rewards.defaultRewards;

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
	private String[] leaveSayings = new String[] {"I will be back for you.", "Another day, another time.", "No, you are not ready for my wrath.", "Perhaps tomorrow you will be worthy of my challenge", "I sense that I am needed else where. You escape..... For now....", "If only you were worth my time."};
	private String[] staySayings = new String[] {"Today is the day.", "May the other world have mercy on your soul.", "MUWAHAHAHAHAHAHAH", "Time to feast!!", "How fast can your run boy!", "It's a shame this will end so quickly for you.", "My presence alone will be your end"};

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		update(world, x, y, z, player, 0, world.rand.nextInt(5) == 1);
	}

	private void schedule(final World world, final int x, final int y, final int z, final EntityPlayer player, final int stage, final boolean staying)
	{
		Task task = new Task("Herobrine Reward", 40)
		{
			@Override
			public void callback()
			{
				update(world, x, y, z, player, stage, staying);
			}

		};

		Scheduler.scheduleTask(task);
	}

	private void update(World world, int x, int y, int z, EntityPlayer player, int stage, boolean staying)
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
					String command = "/summon Zombie ~ ~ ~ {CustomName:\"Herobrine\",CustomNameVisible:1,IsVillager:0,IsBaby:0,CanBreakDoors:1,Equipment:[{id:276,Count:1,tag:{ench:[{id:16,lvl:10},{id:20,lvl:2}]}},{id:313,Count:1,tag:{ench:[{id:0,lvl:10}]}},{id:312,Count:1,tag:{ench:[{id:0,lvl:10}]}},{id:311,Count:1,tag:{ench:[{id:0,lvl:10}]}},{id:379,Damage:3,Count:1,tag:{SkullOwner:Herobrine}}],DropChances:[0.0F,0.0F,0.0F,0.0F,0.0F],Attributes:[{Name:generic.maxHealth,Base:500}],HealF:500}";
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
			schedule(world, x, y, z, player, stage, staying);
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