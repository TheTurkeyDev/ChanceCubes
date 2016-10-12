package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesAchievements;
import chanceCubes.util.CCubesCommandSender;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class HerobrineReward implements IChanceCubeReward
{
	private String[] leaveSayings = new String[] { "I will be back for you.", "Another day, another time.", "No, you are not ready for my wrath.", "Perhaps tomorrow you will be worthy of my challenge", "I sense that I am needed else where. You escape..... For now....", "If only you were worth my time." };
	private String[] staySayings = new String[] { "Today is the day.", "May the other world have mercy on your soul.", "MUWAHAHAHAHAHAHAH", "Time to feast!!", "How fast can your run boy!", "It's a shame this will end so quickly for you.", "My presence alone will be your end" };

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		update(world, pos, player, 0, world.rand.nextInt(5) == 1);
	}

	private void schedule(final World world, final BlockPos pos, final EntityPlayer player, final int stage, final boolean staying)
	{
		Task task = new Task("Herobrine Reward", 40)
		{
			@Override
			public void callback()
			{
				update(world, pos, player, stage, staying);
			}

		};

		Scheduler.scheduleTask(task);
	}

	private void update(World world, BlockPos pos, EntityPlayer player, int stage, boolean staying)
	{
		switch(stage)
		{
			case 0:
			{
				RewardsUtil.sendMessageToAllPlayers(world, TextFormatting.YELLOW + "Herobrine joined the game.");
				break;
			}
			case 1:
			{
				if(staying)
					RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + staySayings[world.rand.nextInt(staySayings.length)]);
				else
					RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + leaveSayings[world.rand.nextInt(leaveSayings.length)]);
				break;
			}
			case 2:
			{
				if(staying)
				{
					RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 0));
					MinecraftServer server = world.getMinecraftServer();
					Boolean rule = server.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
					server.worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
					String command = "/summon Zombie ~ ~1 ~ {CustomName:\"Herobrine\",CustomNameVisible:1,IsVillager:0,IsBaby:0,CanBreakDoors:1,ArmorItems:[{id:diamond_boots,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_leggings,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_chestplate,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_helmet,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}}],HandItems:[{id:diamond_sword,Count:1,tag:{Unbreakable:1,display:{Name:\"Wrath of Herobrine\"},ench:[{id:16,lvl:5}]}},{}],ArmorDropChances:[0.0F,0.0F,0.0F,0.0F],HandDropChances:[2.0F,0.085F],Attributes:[{Name:generic.maxHealth,Base:500}],Health:500.0f,Glowing:1b}";
					CCubesCommandSender sender = new CCubesCommandSender(player, pos);
					server.getCommandManager().executeCommand(sender, command);
					server.worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString());
					player.addStat(CCubesAchievements.herobrine);
				}
				else
				{
					RewardsUtil.sendMessageToAllPlayers(world, TextFormatting.YELLOW + "Herobrine left the game.");
				}
				break;
			}
		}

		stage++;

		if(stage < 3)
			schedule(world, pos, player, stage, staying);
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