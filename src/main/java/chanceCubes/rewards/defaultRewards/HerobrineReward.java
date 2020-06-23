package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Map;

public class HerobrineReward extends BaseCustomReward
{
	private String[] leaveSayings = new String[]{"I will be back for you.", "Another day, another time.", "No, you are not ready for my wrath.", "Perhaps tomorrow you will be worthy of my challenge", "I sense that I am needed else where. You escape..... For now....", "If only you were worth my time."};
	private String[] staySayings = new String[]{"Today is the day.", "May the other world have mercy on your soul.", "MUWAHAHAHAHAHAHAH", "Time to feast!!", "How fast can your run boy!", "It's a shame this will end so quickly for you.", "My presence alone will be your end"};

	public HerobrineReward()
	{
		super(CCubesCore.MODID + ":herobrine", -65);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		int realChance = super.getSettingAsInt(settings, "is_real", 20, 0, 100);
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
					case 0:
					{
						RewardsUtil.sendMessageToAllPlayers(world, TextFormatting.YELLOW + "Herobrine joined the game.");
						break;
					}
					case 1:
					{
						if(real)
							RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + staySayings[RewardsUtil.rand.nextInt(staySayings.length)]);
						else
							RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + leaveSayings[RewardsUtil.rand.nextInt(leaveSayings.length)]);
						break;
					}
					case 2:
					{
						if(real)
						{
							spawnHerobrine(world, pos, player);
							Scheduler.removeTask(this);
						}
						else
						{
							RewardsUtil.sendMessageToAllPlayers(world, TextFormatting.YELLOW + "Herobrine left the game.");

							if(RewardsUtil.rand.nextInt(10) != 4)
								Scheduler.removeTask(this);
						}
						break;
					}
				}

				stage++;
			}
		});
	}

	public void spawnHerobrine(World world, BlockPos pos, PlayerEntity player)
	{
		RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 0));
		RewardsUtil.executeCommand(world, player, pos, "/summon minecraft:zombie ~ ~ ~ {Glowing:1b,CustomNameVisible:1b,Health:500f,IsBaby:0b,CanBreakDoors:1b,CustomName:'{\"text\":\"Herobrine\",\"color\":\"white\",\"bold\":true}',HandItems:[{id:\"minecraft:diamond_sword\",Count:1b,tag:{display:{Name:'{\"text\":\"Wrath of Herobrine\",\"color\":\"white\"}'},Unbreakable:1b,Enchantments:[{id:\"minecraft:sharpness\",lvl:5s}]}},{}],HandDropChances:[1.000F,0.085F],ArmorItems:[{id:\"minecraft:diamond_boots\",Count:1b,tag:{Enchantments:[{id:\"minecraft:protection\",lvl:5s}]}},{id:\"minecraft:diamond_leggings\",Count:1b,tag:{Enchantments:[{id:\"minecraft:protection\",lvl:5s}]}},{id:\"minecraft:diamond_chestplate\",Count:1b,tag:{Enchantments:[{id:\"minecraft:protection\",lvl:5s}]}},{id:\"minecraft:diamond_helmet\",Count:1b,tag:{Enchantments:[{id:\"minecraft:protection\",lvl:5s}]}}],ArmorDropChances:[0.000F,0.000F,0.000F,0.000F],Attributes:[{Name:generic.maxHealth,Base:500}]}");
		Scheduler.scheduleTask(new Task("Herobrine Reward Delayed Advancement", 1)
		{
			@Override
			public void callback()
			{
				RewardsUtil.executeCommand(world, player, pos, "/advancement grant @p only chancecubes:herobrine");
			}
		});
	}
}