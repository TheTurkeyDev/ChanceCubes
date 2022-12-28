package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class HerobrineReward extends BaseCustomReward
{
	private final String[] leaveSayings = new String[]{"I will be back for you.", "Another day, another time.", "No, you are not ready for my wrath.", "Perhaps tomorrow you will be worthy of my challenge", "I sense that I am needed else where. You escape..... For now....", "If only you were worth my time."};
	private final String[] staySayings = new String[]{"Today is the day.", "May the other world have mercy on your soul.", "MUWAHAHAHAHAHAHAH", "Time to feast!!", "How fast can you run boy!", "It's a shame this will end so quickly for you.", "My presence alone will be your end"};

	public HerobrineReward()
	{
		super(CCubesCore.MODID + ":herobrine", -65);
	}

	@Override
	public void trigger(ServerLevel world, BlockPos pos, Player player, JsonObject settings)
	{
		int realChance = super.getSettingAsInt(settings, "isReal", 20, 0, 100);
		boolean real = RewardsUtil.rand.nextInt(100) < realChance;
		Scheduler.scheduleTask(new Task("Herobrine Reward", 280, 40)
		{
			int stage = 0;

			@Override
			public void callback()
			{
				RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + "I've changed My Mind!");
				spawnHerobrine(world, pos, player);
			}

			@Override
			public void update()
			{
				switch(stage)
				{
					case 0 -> RewardsUtil.sendMessageToAllPlayers(world, ChatFormatting.YELLOW + "Herobrine joined the game.");
					case 1 -> {
						if(real)
							RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + staySayings[RewardsUtil.rand.nextInt(staySayings.length)]);
						else
							RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + leaveSayings[RewardsUtil.rand.nextInt(leaveSayings.length)]);
					}
					case 2 -> {
						if(real)
						{
							spawnHerobrine(world, pos, player);
							Scheduler.removeTask(this);
						}
						else
						{
							RewardsUtil.sendMessageToAllPlayers(world, ChatFormatting.YELLOW + "Herobrine left the game.");

							if(RewardsUtil.rand.nextInt(10) != 4)
								Scheduler.removeTask(this);
						}
					}
				}

				stage++;
			}
		});
	}

	public void spawnHerobrine(ServerLevel level, BlockPos pos, Player player)
	{
		RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(0, 1, 0));
		RewardsUtil.executeCommand(level, player, pos, "/summon zombie ~ ~ ~ {Glowing:1b,CustomNameVisible:1b,Health:500f,IsBaby:0b,CanBreakDoors:1b,CustomName:'{\"text\":\"Herobrine\",\"color\":\"white\",\"bold\":true}',HandItems:[{id:\"minecraft:diamond_sword\",Count:1b,tag:{display:{Name:'{\"text\":\"Wrath of Herobrine\",\"color\":\"white\"}'},Unbreakable:1b,Enchantments:[{id:\"minecraft:sharpness\",lvl:5s}]}},{}],HandDropChances:[1.000F,0.085F],ArmorItems:[{id:\"minecraft:diamond_boots\",Count:1b,tag:{Enchantments:[{id:\"minecraft:protection\",lvl:5s}]}},{id:\"minecraft:diamond_leggings\",Count:1b,tag:{Enchantments:[{id:\"minecraft:protection\",lvl:5s}]}},{id:\"minecraft:diamond_chestplate\",Count:1b,tag:{Enchantments:[{id:\"minecraft:protection\",lvl:5s}]}},{id:\"minecraft:diamond_helmet\",Count:1b,tag:{Enchantments:[{id:\"minecraft:protection\",lvl:5s}]}}],ArmorDropChances:[0.000F,0.000F,0.000F,9.000F],Attributes:[{Name:generic.max_health,Base:500},{Name:generic.follow_range,Base:500},{Name:generic.knockback_resistance,Base:10},{Name:zombie.spawn_reinforcements,Base:0}]}");
		Scheduler.scheduleTask(new Task("Herobrine Reward Delayed Advancement", 1)
		{
			@Override
			public void callback()
			{
				RewardsUtil.executeCommand(level, player, pos, "/advancement grant @p only chancecubes:herobrine");
			}
		});
	}
}
