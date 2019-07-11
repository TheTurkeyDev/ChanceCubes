package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class HerobrineReward extends BaseCustomReward
{
	private String[] leaveSayings = new String[] { "I will be back for you.", "Another day, another time.", "No, you are not ready for my wrath.", "Perhaps tomorrow you will be worthy of my challenge", "I sense that I am needed else where. You escape..... For now....", "If only you were worth my time." };
	private String[] staySayings = new String[] { "Today is the day.", "May the other world have mercy on your soul.", "MUWAHAHAHAHAHAHAH", "Time to feast!!", "How fast can your run boy!", "It's a shame this will end so quickly for you.", "My presence alone will be your end" };

	public HerobrineReward()
	{
		super(CCubesCore.MODID + ":Herobrine", -65);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		boolean staying = RewardsUtil.rand.nextInt(5) == 1;
		Scheduler.scheduleTask(new Task("Herobrine Reward", 280, 40)
		{
			int stage = 0;

			@Override
			public void callback()
			{
				RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + "I've changed My Mind!");
				RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 0));
				RewardsUtil.executeCommand(world, player, new Vec3d(pos), "/summon minecraft:zombie %x %y %z {CustomName:\"\\\"Herobrine\\\"\",CustomNameVisible:1,IsVillager:0,IsBaby:0,CanBreakDoors:1,ArmorItems:[{id:diamond_boots,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_leggings,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_chestplate,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_helmet,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}}],HandItems:[{id:diamond_sword,Count:1,tag:{Unbreakable:1,display:{Name:\"Wrath of Herobrine\"},ench:[{id:16,lvl:5}]}},{}],ArmorDropChances:[0.0F,0.0F,0.0F,0.0F],HandDropChances:[2.0F,0.085F],Attributes:[{Name:generic.maxHealth,Base:500}],Health:500.0f,Glowing:1b}");
				Scheduler.scheduleTask(new Task("Herobrine Reward Delayed Advancement", 1)
				{
					@Override
					public void callback()
					{
						RewardsUtil.executeCommand(world, player, player.getPositionVector(), "/advancement grant @p only chancecubes:herobrine");
					}
				});
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
						if(staying)
							RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + staySayings[RewardsUtil.rand.nextInt(staySayings.length)]);
						else
							RewardsUtil.sendMessageToAllPlayers(world, "<Herobrine> " + leaveSayings[RewardsUtil.rand.nextInt(leaveSayings.length)]);
						break;
					}
					case 2:
					{
						if(staying)
						{
							RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 0));
							RewardsUtil.executeCommand(world, player, new Vec3d(pos), "/summon minecraft:zombie %x %y %z {CustomName:\"\\\"Herobrine\\\"\",CustomNameVisible:1,IsVillager:0,IsBaby:0,CanBreakDoors:1,ArmorItems:[{id:diamond_boots,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_leggings,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_chestplate,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}},{id:diamond_helmet,Count:1,tag:{Unbreakable:1,ench:[{id:0,lvl:5}]}}],HandItems:[{id:diamond_sword,Count:1,tag:{Unbreakable:1,display:{Name:\"Wrath of Herobrine\"},ench:[{id:16,lvl:5}]}},{}],ArmorDropChances:[0.0F,0.0F,0.0F,0.0F],HandDropChances:[2.0F,0.085F],Attributes:[{Name:generic.maxHealth,Base:500}],Health:500.0f,Glowing:1b}");
							Scheduler.scheduleTask(new Task("Herobrine Reward Delayed Advancement", 1)
							{
								@Override
								public void callback()
								{
									RewardsUtil.executeCommand(world, player, player.getPositionVector(), "/advancement grant @p only chancecubes:herobrine");
								}
							});
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
}