package chanceCubes.rewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.parsers.RewardParser;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.defaultRewards.*;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.OffsetTileEntity;
import chanceCubes.rewards.rewardparts.SchematicPart;
import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.rewards.rewardtype.BlockRewardType;
import chanceCubes.rewards.rewardtype.CommandRewardType;
import chanceCubes.rewards.rewardtype.EntityRewardType;
import chanceCubes.rewards.rewardtype.ItemRewardType;
import chanceCubes.rewards.rewardtype.MessageRewardType;
import chanceCubes.rewards.rewardtype.SchematicRewardType;
import chanceCubes.rewards.rewardtype.SoundRewardType;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.FloatVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.util.CustomEntry;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultRewards
{
	/**
	 * loads the default rewards of the Chance Cube
	 */
	public static void loadDefaultRewards()
	{
		RewardsUtil.initData();

		if(!CCubesSettings.enableHardCodedRewards)
			return;

		for(String fileName : RewardsUtil.getHardcodedRewards())
		{
			JsonObject json = RewardsUtil.getRewardJson(fileName);
			for(Map.Entry<String, JsonElement> reward : json.entrySet())
			{
				CustomEntry<BasicReward, Boolean> parsedReward = RewardParser.parseReward(reward);
				BasicReward basicReward = parsedReward.getKey();
				if(basicReward == null)
				{
					CCubesCore.logger.log(Level.ERROR, "A hard coded reward failed to parse! Please report this to the mod dev! " + reward.getKey() + " for the file " + fileName);
					continue;
				}

				if(parsedReward.getValue())
					GlobalCCRewardRegistry.GIANT.registerReward(basicReward);
				else
					GlobalCCRewardRegistry.DEFAULT.registerReward(basicReward);
			}
		}

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":lava_ring", -40, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":rain", -5, new CommandRewardType("/weather thunder 20000")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":silverfish_surround", -20, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":fish_dog", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.FISH, 5)), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("wolf"))))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":bone_cat", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.BONE, 5)), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("ocelot"))))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":xp_crystal", -60, new CommandRewardType("/summon xp_orb ~ ~1 ~ {Value:1,Passengers:[{id:\"ender_crystal\"}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":tnt_cat", -25, new CommandRewardType("/summon ocelot ~ ~1 ~ {CatType:0,Sitting:0,Passengers:[{id:\"tnt\",Fuse:80}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":slime_man", 10, new CommandRewardType("/summon slime ~ ~1 ~ {Size:3,Glowing:1b,Passengers:[{id:\"Slime\",Size:2,Glowing:1b,Passengers:[{id:\"Slime\",CustomName:\"Slime Man\",CustomNameVisible:1,Size:1,Glowing:1b}]}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":sail_away", 5, new BlockRewardType(new OffsetBlock(0, -1, 0, Blocks.WATER, false)), new CommandRewardType("/summon Boat %x %y %z"), new MessageRewardType("Come sail away!")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":witch", -15, new CommandRewardType("/summon witch %x %y %z ")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":spawn_cluckington", 25, new CommandRewardType("/summon Chicken ~ ~1 ~ {CustomName:\"Cluckington\",CustomNameVisible:1,ActiveEffects:[{Id:1,Amplifier:3,Duration:199980}],Passengers:[{id:\"Zombie\",CustomName:\"wyld\",CustomNameVisible:1,IsVillager:0,IsBaby:1,ArmorItems:[{},{},{},{id:\"minecraft:skull\",Damage:3,tag:{SkullOwner:\"wyld\"},Count:1}]}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":spawn_jerry", 25, new CommandRewardType("/summon slime %x %y %z {Size:1,CustomName:\"Jerry\",CustomNameVisible:1}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":spawn_glenn", 25, new CommandRewardType("/summon zombie %x %y %z {CustomName:\"Glenn\",CustomNameVisible:1}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":spawn_dr_trayaurus", 25, new CommandRewardType("/summon villager %x %y %z {CustomName:\"Dr Trayaurus\",CustomNameVisible:1,Profession:1}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":spawn_pickles", 25, new CommandRewardType("/summon mooshroom ~ ~1 ~ {Age:-10000,CustomName:\"Pickles\"}"), new MessageRewardType("Why is his name pickles? The world may never know")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":want_to_build_a_snowman", 45, new MessageRewardType("Do you want to build a snowman?"), new ItemRewardType(new ItemPart(new ItemStack(Blocks.SNOW, 2)), new ItemPart(new ItemStack(Blocks.PUMPKIN)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":diamond_block", 85, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.DIAMOND_BLOCK, true, 200))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":tnt_diamond", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.DIAMOND_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":fake_tnt", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_GENERIC_EXPLODE, 120).setAtPlayersLocation(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":invisible_ghasts", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_GHAST_SCREAM).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":no", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.CHANCE_CUBE, false)), new MessageRewardType("No")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":invisible_creeper", -30, new CommandRewardType("/summon Creeper %x %y %z {ActiveEffects:[{Id:14,Amplifier:0,Duration:200,ShowParticles:0b}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":knockback_zombie", -35, new CommandRewardType("/summon Zombie ~ ~1 ~ {CustomName:\"Leonidas\",CustomNameVisible:1,IsVillager:0,IsBaby:1,HandItems:[{id:stick,Count:1,tag:{AttributeModifiers:[{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:100,Operation:0,UUIDLeast:724513,UUIDMost:715230}],ench:[{id:19,lvl:100}],display:{Name:\"The Spartan Kick\"}}},{}],HandDropChances:[0.0F,0.085F],ActiveEffects:[{Id:1,Amplifier:5,Duration:199980,ShowParticles:0b},{Id:8,Amplifier:2,Duration:199980}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":actual_invisible_ghast", -80, new CommandRewardType("/summon Ghast ~ ~10 ~ {ActiveEffects:[{Id:14,Amplifier:0,Duration:2000,ShowParticles:0b}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":fireworks", 5, new CommandRewardType(RewardsUtil.executeXCommands("/summon fireworks_rocket ~ ~1 ~ {LifeTime:15,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Colors:[I;16711680],FadeColors:[I;16711680]}]}}}}", 4))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":tnt_bats", -50, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~1 ~ {Passengers:[{id:\"tnt\",Fuse:80}]}", 10))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":nether_jelly_fish", -40, new CommandRewardType(RewardsUtil.executeXCommands("/summon bat ~ ~1 ~ {Passengers:[{id:\"magma_cube\",CustomName:\"Nether Jelly Fish\",CustomNameVisible:1,Size:3}]}", 10))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":pig_of_destiny", 15, new CommandRewardType("/summon Pig ~ ~1 ~ {CustomName:\"The Pig of Destiny\",CustomNameVisible:1,ArmorItems:[{},{},{id:diamond_chestplate,Count:1,tag:{ench:[{id:7,lvl:1000}]}},{}],ArmorDropChances:[0.085F,0.085F,0.0F,0.085F]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":diy_pie", 5, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.PUMPKIN, false), new OffsetBlock(1, 1, 0, Blocks.REEDS, false)), new CommandRewardType("/summon Chicken ~ ~1 ~ {CustomName:\"Zeeth_Kyrah\",CustomNameVisible:1}"), new MessageRewardType("Do it yourself Pumpkin Pie!")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":coal_to_diamonds", 10, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.COAL_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5)), new ItemRewardType(new ItemPart(new ItemStack(Items.DIAMOND, 5), 50))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":spongebob_squarepants", 15, new CommandRewardType("/summon Item ~ ~ ~ {Item:{id:sponge,Count:1,tag:{display:{Name:\"SpongeBob\"}}}}", "/summon Item ~ ~ ~ {Item:{id:leather_leggings,Count:1,tag:{display:{Name:\"SquarePants\"}}}}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":quidditch", -30, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~ ~ {Passengers:[{id:\"Witch\"}]}", 7)), new MessageRewardType(new MessagePart("Quidditch anyone?").setRange(32))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":one_man_army", -10, new EntityRewardType("zombie_pigman"), new CommandRewardType(RewardsUtil.executeXCommands("/summon zombie_pigman ~ ~ ~ {Silent:1,ActiveEffects:[{Id:14,Amplifier:0,Duration:19980,ShowParticles:1b}]}", 9)), new MessageRewardType(new MessagePart("One man army").setRange(32))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":cuteness", 10, new CommandRewardType(RewardsUtil.executeXCommands("/summon rabbit ~ ~1 ~ {CustomName:\"Fluffy\",CustomNameVisible:1,RabbitType:5}", 20)), new MessageRewardType(new MessagePart("Cuteness overload!").setRange(32))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":silvermite_stacks", -35, new CommandRewardType(RewardsUtil.executeXCommands("/summon Silverfish ~ ~1 ~ {Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\"}]}]}]}]}]}]}]}]}]}]}", 5))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":invizible_silverfish", -55, new CommandRewardType(RewardsUtil.executeXCommands("/summon Silverfish ~ ~1 ~ {Glowing:1b,ActiveEffects:[{Id:1,Amplifier:1,Duration:200000},{Id:14,Amplifier:0,Duration:20000}],Passengers:[{id:\"Silverfish\",ActiveEffects:[{Id:14,Amplifier:0,Duration:20000}]}]}", 5))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":arrow_trap", -25, new SchematicRewardType(new SchematicPart("/assets/chancecubes/schematics/arrow_trap.ccs", true, new IntVar(1), new IntVar(-1), new IntVar(1), new FloatVar(0), new BoolVar(false), new BoolVar(true), new BoolVar(true), new BoolVar(true), new IntVar(0)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":trampoline", 15, new MessageRewardType("Time to bounce!"), new SchematicRewardType(new SchematicPart("/assets/chancecubes/schematics/trampoline.ccs", true, new IntVar(1), new IntVar(-3), new IntVar(1), new FloatVar(0), new BoolVar(false), new BoolVar(true), new BoolVar(true), new BoolVar(true), new IntVar(0))), new BlockRewardType(new OffsetBlock(2, -2, -2, Blocks.REDSTONE_BLOCK, false, 3).setRelativeToPlayer(true).setCausesBlockUpdate(true), new OffsetBlock(2, -2, -2, Blocks.REDSTONE_WIRE, false, 5).setRelativeToPlayer(true).setCausesBlockUpdate(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":skeleton_bats", -40, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~1 ~ {Passengers:[{id:\"Skeleton\",ArmorItems:[{},{},{},{id:leather_helmet,Count:1}],HandItems:[{id:bow,Count:1},{}]}]}", 10))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":cookie_monster", -5, new MessageRewardType(new MessagePart("Here have some cookies!").setRange(32), new MessagePart("[Cookie Monster] Hey! Those are mine!", 30).setRange(32)), new CommandRewardType(new CommandPart("/summon item ~ ~1 ~ {Item:{id:\"minecraft:cookie\",Count:8b}}"), new CommandPart("/summon zombie ~ ~1 ~ {CustomName:\"Cookie Monster\",CustomNameVisible:1,IsVillager:0,IsBaby:1}", 30))));

		NBTTagCompound nbt;

		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = new TextComponentString("The broken path");
		sign.signText[1] = new TextComponentString("to succeed");
		nbt = new NBTTagCompound();
		((TileEntity) sign).writeToNBT(nbt);
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":path_to_succeed", 0, new BlockRewardType(new OffsetTileEntity(0, 0, -5, Blocks.STANDING_SIGN, nbt, true, 20), new OffsetBlock(0, -1, 0, Blocks.COBBLESTONE, true, 0), new OffsetBlock(0, -1, -1, Blocks.COBBLESTONE, true, 4), new OffsetBlock(0, -1, -2, Blocks.COBBLESTONE, true, 8), new OffsetBlock(0, -1, -3, Blocks.COBBLESTONE, true, 12), new OffsetBlock(0, -1, -4, Blocks.COBBLESTONE, true, 16), new OffsetBlock(0, -1, -5, Blocks.COBBLESTONE, true, 20))));


		OffsetBlock[] blocks = new OffsetBlock[35];
		int i = 0;
		for(int y = 0; y < 2; y++)
		{
			for(int x = 0; x < 5; x++)
			{
				for(int z = 0; z < 5; z++)
				{
					if(y == 1 && (x == 0 || x == 4 || z == 0 || z == 4))
						continue;
					blocks[i] = new OffsetBlock(x - 2, y, z - 2, Blocks.IRON_BLOCK, true, i * 5);
					i++;
				}
			}
		}
		blocks[i] = new OffsetBlock(0, 2, 0, Blocks.BEACON, true, 200);
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":beacon_build", 100, new BlockRewardType(blocks)));

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":half_heart", -30)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				player.setHealth(1f);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":no_exp", -40)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				player.experienceLevel = 0;
				player.experienceTotal = 0;
				player.experience = 0;
				player.sendMessage(new TextComponentString("Rip EXP"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":smite", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ, false));
				player.sendMessage(new TextComponentString("Thou has been smitten!"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":cookie-splosion", 35)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				EntityItem cookie;
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						cookie = new EntityItem(world, pos.getX(), pos.getY() + 1D, pos.getZ(), new ItemStack(Items.COOKIE));
						world.spawnEntity(cookie);
						cookie.motionX = xx;
						cookie.motionY = Math.random();
						cookie.motionZ = zz;
					}
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":random_status_effect", 0)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				player.sendMessage(new TextComponentString("Selecting random potion effect to apply..."));

				Scheduler.scheduleTask(new Task("Cookie Monster", 30)
				{
					@Override
					public void callback()
					{
						PotionEffect effect = RewardsUtil.getRandomPotionEffect();
						player.sendMessage(new TextComponentString("You have been given " + I18n.translateToLocal(effect.getEffectName()).trim() + " " + (effect.getAmplifier() + 1) + " for " + (effect.getDuration() / 20) + " seconds!"));
						player.addPotionEffect(effect);
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":arrow_spray", -15)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				EntityTippedArrow arrow;
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						arrow = new EntityTippedArrow(world);
						arrow.setLocationAndAngles(pos.getX(), pos.getY() + 0.5f, pos.getZ(), 0, 0);
						arrow.motionX = xx;
						arrow.motionY = .3;
						arrow.motionZ = zz;
						world.spawnEntity(arrow);
					}
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":lingering_potions_ring", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				EntityPotion pot;
				for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 10))
				{
					PotionType potionType = PotionType.REGISTRY.getObjectById(RewardsUtil.rand.nextInt(PotionType.REGISTRY.getKeys().size()));
					pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), potionType));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.motionX = Math.cos(rad) * (0.1 + (0.05 * 3));
					pot.motionY = 1;
					pot.motionZ = Math.sin(rad) * (0.1 + (0.05 * 3));
					world.spawnEntity(pot);
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":charged_creeper", -40)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 0));
				EntityCreeper ent = new EntityCreeper(world);
				ent.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
				ent.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300, 99, true, false));
				ent.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 300, 99, true, false));
				world.spawnEntity(ent);

				Scheduler.scheduleTask(new Task("Charged Creeper Reward", 2)
				{
					@Override
					public void callback()
					{
						world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false));
						ent.setFire(0);
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":disco", 40)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				for(int xx = -4; xx < 5; xx++)
					for(int zz = -4; zz < 5; zz++)
						RewardsUtil.placeBlock(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(RewardsUtil.rand.nextInt(16))), world, pos.add(xx, -1, zz));

				for(int i = 0; i < 10; i++)
				{
					EntitySheep sheep = new EntitySheep(world);
					sheep.setCustomNameTag("jeb_");
					sheep.setLocationAndAngles(pos.getX(), pos.getY() + 1, pos.getZ(), 0, 0);
					world.spawnEntity(sheep);
				}

				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_ICOSAHEDRON.getDefaultState(), world, pos.add(0, 3, 0));

				RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "Disco Party!!!!");
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":ender_crystal_timer", -90)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				for(int i = 30; i > 0; i--)
					RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, i, 0));

				EntityEnderCrystal ent = new EntityEnderCrystal(world);
				ent.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
				world.spawnEntity(ent);

				EntityArrow arrow = new EntityTippedArrow(world, pos.getX() + 0.5, pos.getY() + 29, pos.getZ() + 0.5);
				arrow.motionX = 0;
				arrow.motionY = -0.25f;
				arrow.motionZ = 0;
				world.spawnEntity(arrow);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":5_prongs", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				for(int xx = pos.getX() - 3; xx <= pos.getX() + 3; xx++)
					for(int zz = pos.getZ() - 3; zz <= pos.getZ() + 3; zz++)
						for(int yy = pos.getY(); yy <= pos.getY() + 4; yy++)
							RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, new BlockPos(xx, yy, zz));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.getDefaultState(), world, pos);
				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.getDefaultState(), world, pos.add(0, 1, 0));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_ICOSAHEDRON.getDefaultState(), world, pos.add(0, 2, 0));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.getDefaultState(), world, pos.add(-3, 0, -3));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(-3, 1, -3));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.getDefaultState(), world, pos.add(-3, 0, 3));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(-3, 1, 3));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.getDefaultState(), world, pos.add(3, 0, -3));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(3, 1, -3));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.getDefaultState(), world, pos.add(3, 0, 3));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(3, 1, 3));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":inventory_bomb", -55)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				player.inventory.dropAllItems();

				for(int i = 0; i < player.inventory.mainInventory.size(); i++)
					player.inventory.mainInventory.set(i, new ItemStack(Blocks.DEADBUSH, 64));

				for(int i = 0; i < player.inventory.armorInventory.size(); i++)
				{
					ItemStack stack = new ItemStack(Blocks.DEADBUSH, 64);
					if(i == 0)
					{
						stack.setStackDisplayName("ButtonBoy");
						stack.setCount(13);
					}
					else if(i == 1)
					{
						stack.setStackDisplayName("TheBlackswordsman");
						stack.setCount(13);
					}
					player.inventory.armorInventory.set(i, stack);
				}

				player.sendMessage(new TextComponentString("Inventory Bomb!!!!"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":nuke", -75)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "May death rain upon them");
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 6, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 6, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 6, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 6, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 2, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 2, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 2, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 2, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 2, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 2, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 2, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 2, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 6, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 6, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 6, player));
				world.spawnEntity(new EntityTNTPrimed(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 6, player));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":random_teleport", -15)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				int xChange = ((world.rand.nextInt(50) + 20) + pos.getX()) - 35;
				int zChange = ((world.rand.nextInt(50) + 20) + pos.getZ()) - 35;

				int yChange = -1;

				for(int yy = 0; yy <= world.getActualHeight(); yy++)
				{
					if(world.isAirBlock(new BlockPos(xChange, yy, zChange)) && world.isAirBlock(new BlockPos(xChange, yy + 1, zChange)))
					{
						yChange = yy;
						break;
					}
				}
				if(yChange == -1)
					return;

				player.setPositionAndUpdate(xChange, yChange, zChange);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":rotten_food", -30)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				for(int i = 0; i < player.inventory.mainInventory.size(); i++)
				{
					ItemStack stack = player.inventory.mainInventory.get(i);
					if(!stack.isEmpty() && stack.getItem() instanceof ItemFood)
						player.inventory.mainInventory.set(i, new ItemStack(Items.ROTTEN_FLESH, stack.getCount()));
				}

				player.sendMessage(new TextComponentString("Ewwww it's all rotten"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":thrown_in_air", -35)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				int px = (int) Math.floor(player.posX);
				int py = (int) Math.floor(player.posY) + 1;
				int pz = (int) Math.floor(player.posZ);

				for(int y = 0; y < 40; y++)
					for(int x = -1; x < 2; x++)
						for(int z = -1; z < 2; z++)
							RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(px + x, py + y, pz + z));

				Scheduler.scheduleTask(new Task("Thrown_In_Air_Reward", 5)
				{
					@Override
					public void callback()
					{
						player.isAirBorne = true;
						player.motionY = 20;
						((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityVelocity(player.getEntityId(), player.motionX, player.motionY, player.motionZ));
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":torches_to_creepers", -40)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				for(int yy = -32; yy <= 32; yy++)
				{
					for(int xx = -32; xx <= 32; xx++)
					{
						for(int zz = -32; zz <= 32; zz++)
						{
							IBlockState b = world.getBlockState(pos.add(xx, yy, zz));
							if(b.getLightValue(world, pos) > 0 && b.getBlock() != Blocks.LAVA && !b.getBlock().hasTileEntity(b))
							{
								RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(xx, yy, zz));
								EntityCreeper creeper = new EntityCreeper(world);
								creeper.setLocationAndAngles(pos.getX() + xx + 0.5, pos.getY() + yy, pos.getZ() + zz + 0.5, 0, 0);
								world.spawnEntity(creeper);
							}
						}
					}
				}
				player.sendMessage(new TextComponentString("Those lights seem a little weird.... O.o"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":traveller", 15)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				int x = RewardsUtil.rand.nextInt(1000) + 200;
				int z = RewardsUtil.rand.nextInt(1000) + 200;

				BlockPos newPos = pos.add(x, 0, z);
				RewardsUtil.placeBlock(Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST), world, newPos);
				TileEntityChest chest = (TileEntityChest) world.getTileEntity(newPos);
				for(int i = 0; i < 10; i++)
					chest.setInventorySlotContents(i, new ItemStack(RewardsUtil.getRandomItem()));

				RewardsUtil.sendMessageToNearPlayers(world, pos, 25, "" + newPos.getX() + ", " + newPos.getY() + ", " + newPos.getZ());
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":troll_hole", -20)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				final BlockPos worldPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY) - 1, Math.floor(player.posZ));
				final RewardBlockCache cache = new RewardBlockCache(world, worldPos, new BlockPos(worldPos.getX(), worldPos.getY() + 1, worldPos.getZ()));

				for(int y = 0; y > -75; y--)
					for(int x = -2; x < 3; x++)
						for(int z = -2; z < 3; z++)
							cache.cacheBlock(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());

				Scheduler.scheduleTask(new Task("TrollHole", 35)
				{
					@Override
					public void callback()
					{
						cache.restoreBlocks(player);
						player.motionY = 0;
						player.fallDistance = 0;
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":saw_nothing_diamond", 0)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				EntityItem itemEnt = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.DIAMOND, 1));
				itemEnt.setInfinitePickupDelay();
				world.spawnEntity(itemEnt);

				Scheduler.scheduleTask(new Task("Saw_Nothing_Diamond", 100)
				{
					@Override
					public void callback()
					{
						itemEnt.setDead();
						player.sendMessage(new TextComponentString("You didn't see anything......"));
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":hand_enchant", 20)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				ItemStack toEnchant;
				if(!player.getHeldItemMainhand().isEmpty())
				{
					toEnchant = player.getHeldItemMainhand();
				}
				else
				{
					List<ItemStack> stacks = new ArrayList<>();
					for(ItemStack stack : player.inventory.mainInventory)
						if(!stack.isEmpty())
							stacks.add(stack);

					for(ItemStack stack : player.inventory.armorInventory)
						if(!stack.isEmpty())
							stacks.add(stack);

					if(stacks.size() == 0)
					{
						ItemStack dirt = new ItemStack(Blocks.DIRT);
						dirt.setStackDisplayName("A lonley piece of dirt");
						player.inventory.addItemStackToInventory(dirt);
						RewardsUtil.executeCommand(world, player, "/advancement grant @p only chancecubes:lonely_dirt");
						return;
					}

					toEnchant = stacks.get(RewardsUtil.rand.nextInt(stacks.size()));
				}

				CustomEntry<Enchantment, Integer> enchantment = RewardsUtil.getRandomEnchantmentAndLevel();
				toEnchant.addEnchantment(enchantment.getKey(), enchantment.getValue());
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new AnvilRain());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new HerobrineReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new SurroundedReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new CreeperSurroundedReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new WitherReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new TrollTNTReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new WaitForItReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new ClearInventoryReward());
		// GlobalCCRewardRegistry.DEFAULT.registerReward(new ZombieCopyCatReward());
		// GlobalCCRewardRegistry.DEFAULT.registerReward(new InventoryChestReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new ItemOfDestinyReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new JukeBoxReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BookOfMemesReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new TableFlipReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new MazeReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new OneIsLuckyReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new SkyblockReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new CakeIsALieReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new ItemRenamer());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new DoubleRainbow());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new WolvesToCreepersReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new DidYouKnowReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new ArmorStandArmorReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new RainingCatsAndCogsReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new ItemChestReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new MagicFeetReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new DigBuildReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new ChanceCubeRenameReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new CountDownReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new MobTowerReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new MontyHallReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new MatchingReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new TicTacToeReward());
		// GlobalCCRewardRegistry.DEFAULT.registerReward(new MobEffectsReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BossMimicReward());
		// GlobalCCRewardRegistry.DEFAULT.registerReward(new BossSlimeQueenReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BossWitchReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BossBlazeReward());

		MathReward math = new MathReward();
		MinecraftForge.EVENT_BUS.register(math);
		GlobalCCRewardRegistry.DEFAULT.registerReward(math);

		QuestionsReward question = new QuestionsReward();
		MinecraftForge.EVENT_BUS.register(question);
		GlobalCCRewardRegistry.DEFAULT.registerReward(question);

		CoinFlipReward coinFlip = new CoinFlipReward();
		MinecraftForge.EVENT_BUS.register(coinFlip);
		GlobalCCRewardRegistry.DEFAULT.registerReward(coinFlip);
	}
}
