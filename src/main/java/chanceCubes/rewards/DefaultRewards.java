package chanceCubes.rewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.mcwrapper.BlockWrapper;
import chanceCubes.mcwrapper.EntityWrapper;
import chanceCubes.parsers.RewardParser;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.defaultRewards.*;
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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

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

		if(!CCubesSettings.enableHardCodedRewards.get())
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
					CCubesCore.logger.error("A hard coded reward failed to parse! Please report this to the mod dev! " + reward.getKey() + " for the file " + fileName);
					continue;
				}

				boolean defaultEnable = !basicReward.getName().equalsIgnoreCase("chancecubes:giant_chance_cube");
				if(parsedReward.getValue())
					GlobalCCRewardRegistry.GIANT.registerReward(basicReward);
				else
					GlobalCCRewardRegistry.DEFAULT.registerReward(basicReward, defaultEnable);
			}
		}

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":lava_ring", -40, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":rain", -5, new CommandRewardType("/weather thunder 20000")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":silverfish_surround", -20, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":fish_dog", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.COD, 5)), new ItemPart(new ItemStack(Items.WOLF_SPAWN_EGG)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":bone_cat", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.BONE, 5)), new ItemPart(new ItemStack(Items.OCELOT_SPAWN_EGG)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":tnt_cat", -25, new CommandRewardType("/summon ocelot ~ ~1 ~ {CatType:0,Sitting:0,Passengers:[{id:\"tnt\",Fuse:80}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":diamond_block", 85, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.DIAMOND_BLOCK, true, 200))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":tnt_diamond", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.DIAMOND_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":fake_tnt", 0, new SoundRewardType(new SoundPart(SoundEvents.TNT_PRIMED), new SoundPart(SoundEvents.TNT_PRIMED), new SoundPart(SoundEvents.TNT_PRIMED), new SoundPart(SoundEvents.GENERIC_EXPLODE, 120).setAtPlayersLocation(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":invisible_ghasts", 0, new SoundRewardType(new SoundPart(SoundEvents.GHAST_SCREAM).setServerWide(true), new SoundPart(SoundEvents.GHAST_WARN).setServerWide(true), new SoundPart(SoundEvents.GHAST_WARN).setServerWide(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":no", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.CHANCE_CUBE, false)), new MessageRewardType("No")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":coal_to_diamonds", 10, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.COAL_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5)), new ItemRewardType(new ItemPart(new ItemStack(Items.DIAMOND, 5), 50))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":one_man_army", -10, new EntityRewardType("zombified_piglin"), new CommandRewardType(RewardsUtil.executeXCommands("/summon zombified_piglin ~ ~ ~ {Silent:1,ActiveEffects:[{Id:14,Amplifier:0,Duration:19980,ShowParticles:1b}]}", 9)), new MessageRewardType(new MessagePart("One man army").setRange(32))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":arrow_trap", -25, new SchematicRewardType(new SchematicPart("/data/chancecubes/schematics/arrow_trap.ccs", true, new IntVar(1), new IntVar(-1), new IntVar(1), new FloatVar(0), new BoolVar(false), new BoolVar(true), new BoolVar(true), new BoolVar(true), new IntVar(0)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":trampoline", 15, new MessageRewardType("Time to bounce!"), new SchematicRewardType(new SchematicPart("/data/chancecubes/schematics/trampoline.ccs", true, new IntVar(1), new IntVar(-3), new IntVar(1), new FloatVar(0), new BoolVar(false), new BoolVar(true), new BoolVar(true), new BoolVar(true), new IntVar(0))), new BlockRewardType(new OffsetBlock(2, -2, -2, Blocks.REDSTONE_BLOCK, false, 3).setRelativeToPlayer(true).setCausesBlockUpdate(true), new OffsetBlock(2, -2, -2, Blocks.REDSTONE_WIRE, false, 5).setRelativeToPlayer(true).setCausesBlockUpdate(true))));

		CompoundTag nbt;

		SignBlockEntity sign = BlockWrapper.createSign(new BlockPos(0, 0, 0), new String[]{"The broken path", "to succeed"});
		nbt = sign.saveWithFullMetadata();
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":path_to_succeed", 0, new BlockRewardType(new OffsetTileEntity(0, 0, -5, Blocks.OAK_SIGN, nbt, true, 20), new OffsetBlock(0, -1, 0, Blocks.COBBLESTONE, true, 0), new OffsetBlock(0, -1, -1, Blocks.COBBLESTONE, true, 4), new OffsetBlock(0, -1, -2, Blocks.COBBLESTONE, true, 8), new OffsetBlock(0, -1, -3, Blocks.COBBLESTONE, true, 12), new OffsetBlock(0, -1, -4, Blocks.COBBLESTONE, true, 16), new OffsetBlock(0, -1, -5, Blocks.COBBLESTONE, true, 20))));


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
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				player.setHealth(1f);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":no_exp", -40)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				player.experienceLevel = 0;
				player.totalExperience = 0;
				player.experienceProgress = 0;
				RewardsUtil.sendMessageToPlayer(player, "Rip EXP");
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":smite", -10)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				EntityWrapper.spawnLightning(level, player.getOnPos());
				RewardsUtil.sendMessageToPlayer(player, "Thou has been smitten!");
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":cookie-splosion", 35)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				ItemEntity cookie;
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						cookie = new ItemEntity(level, pos.getX(), pos.getY() + 1D, pos.getZ(), new ItemStack(Items.COOKIE));
						level.addFreshEntity(cookie);
						cookie.setDeltaMovement(xx, Math.random(), zz);
					}
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":random_status_effect", 0)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				RewardsUtil.sendMessageToPlayer(player, "Selecting random potion effect to apply...");

				Scheduler.scheduleTask(new Task("Cookie Monster", 30)
				{
					@Override
					public void callback()
					{
						MobEffectInstance effect = RewardsUtil.getRandomPotionEffectInstance();
						RewardsUtil.sendMessageToPlayer(player, "You have been given: ");
						RewardsUtil.sendMessageToPlayer(player, new TranslatableComponent(effect.getDescriptionId()));
						RewardsUtil.sendMessageToPlayer(player, "for " + (effect.getDuration() / 20) + " seconds!");
						player.addEffect(effect);
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":arrow_spray", -15)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				Arrow arrow;
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						arrow = new Arrow(level, player);
						arrow.moveTo(pos.getX(), pos.getY() + 0.5f, pos.getZ(), 0, 0);
						arrow.setDeltaMovement(xx, .3, zz);
						level.addFreshEntity(arrow);
					}
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":lingering_potions_ring", -10)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				ThrownPotion pot;
				for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 10))
				{
					pot = new ThrownPotion(level, player);
					pot.setItem(PotionUtils.setCustomEffects(new ItemStack(Items.LINGERING_POTION), List.of(RewardsUtil.getRandomPotionEffectInstance())));
					pot.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.setDeltaMovement(Math.cos(rad) * (0.1 + (0.05 * 3)), 1, Math.sin(rad) * (0.1 + (0.05 * 3)));
					level.addFreshEntity(pot);
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":charged_creeper", -40)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				if(level.isClientSide())
					return;

				RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(0, 1, 0));
				Creeper ent = EntityType.CREEPER.create(level);
				ent.moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
				ent.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 99, true, false));
				ent.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 99, true, false));
				level.addFreshEntity(ent);

				Scheduler.scheduleTask(new Task("Charged Creeper Reward", 2)
				{
					@Override
					public void callback()
					{
						EntityWrapper.spawnLightning(level, ent.getOnPos());
						ent.clearFire();
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":disco", 40)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				for(int xx = -4; xx < 5; xx++)
					for(int zz = -4; zz < 5; zz++)
						RewardsUtil.placeBlock(RewardsUtil.getRandomWool(), level, pos.offset(xx, -1, zz));

				for(int i = 0; i < 10; i++)
				{
					Sheep sheep = EntityType.SHEEP.create(level);
					sheep.setCustomName(new TextComponent("jeb_"));
					sheep.moveTo(pos.getX(), pos.getY() + 1, pos.getZ(), 0, 0);
					level.addFreshEntity(sheep);
				}

				//RewardsUtil.placeBlock(CCubesBlocks.CHANCE_ICOSAHEDRON.defaultBlockState(), level, pos.offset(0, 3, 0));

				RewardsUtil.sendMessageToNearPlayers(level, pos, 32, "Disco Party!!!!");
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":ender_crystal_timer", -90)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				for(int i = 30; i > 0; i--)
					RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(0, i, 0));

				EndCrystal ent = EntityType.END_CRYSTAL.create(level);
				ent.moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
				level.addFreshEntity(ent);

				Arrow arrow = new Arrow(level, pos.getX() + 0.5, pos.getY() + 29, pos.getZ() + 0.5);
				arrow.setDeltaMovement(0, -0.25, 0);
				level.addFreshEntity(arrow);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":5_prongs", -10)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				for(int xx = pos.getX() - 3; xx <= pos.getX() + 3; xx++)
					for(int zz = pos.getZ() - 3; zz <= pos.getZ() + 3; zz++)
						for(int yy = pos.getY(); yy <= pos.getY() + 4; yy++)
							RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, new BlockPos(xx, yy, zz));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.defaultBlockState(), level, pos);
				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.defaultBlockState(), level, pos.offset(0, 1, 0));
				//RewardsUtil.placeBlock(CCubesBlocks.CHANCE_ICOSAHEDRON.defaultBlockState(), level, pos.offset(0, 2, 0));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.defaultBlockState(), level, pos.offset(-3, 0, -3));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.defaultBlockState(), level, pos.offset(-3, 1, -3));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.defaultBlockState(), level, pos.offset(-3, 0, 3));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.defaultBlockState(), level, pos.offset(-3, 1, 3));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.defaultBlockState(), level, pos.offset(3, 0, -3));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.defaultBlockState(), level, pos.offset(3, 1, -3));

				RewardsUtil.placeBlock(Blocks.QUARTZ_BLOCK.defaultBlockState(), level, pos.offset(3, 0, 3));
				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.defaultBlockState(), level, pos.offset(3, 1, 3));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":inventory_bomb", -55)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				player.getInventory().dropAll();

				player.getInventory().items.replaceAll(ignored -> new ItemStack(Blocks.DEAD_BUSH, 64));

				for(int i = 0; i < player.getInventory().armor.size(); i++)
				{
					ItemStack stack = new ItemStack(Blocks.DEAD_BUSH, 64);
					if(i == 0)
					{
						stack.setHoverName(new TextComponent("Button").setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE))));
						stack.setCount(13);
					}
					else if(i == 1)
					{
						stack.setHoverName(new TextComponent("TheBlackswordsman"));
						stack.setCount(13);
					}
					player.getInventory().armor.set(i, stack);
				}

				RewardsUtil.sendMessageToPlayer(player, "Inventory Bomb!!!!");
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":nuke", -75)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				RewardsUtil.sendMessageToNearPlayers(level, pos, 32, "May death rain upon them");
				level.addFreshEntity(new PrimedTnt(level, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 6, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 6, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 6, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 6, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 2, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 2, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 2, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 2, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 2, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 2, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 2, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 2, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 6, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 6, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 6, player));
				level.addFreshEntity(new PrimedTnt(level, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 6, player));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":random_teleport", -15)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				int xChange = ((level.random.nextInt(50) + 20) + pos.getX()) - 35;
				int zChange = ((level.random.nextInt(50) + 20) + pos.getZ()) - 35;

				int yChange = -1;

				for(int yy = 0; yy <= level.getHeight(); yy++)
				{
					if(level.getBlockState(new BlockPos(xChange, yy, zChange)).isAir() && level.getBlockState(new BlockPos(xChange, yy + 1, zChange)).isAir())
					{
						yChange = yy;
						break;
					}
				}
				if(yChange == -1)
					return;

				player.moveTo(xChange, yChange, zChange);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":rotten_food", -30)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				for(int i = 0; i < player.getInventory().items.size(); i++)
				{
					ItemStack stack = player.getInventory().items.get(i);
					if(!stack.isEmpty() && stack.getItem().isEdible())
						player.getInventory().items.set(i, new ItemStack(Items.ROTTEN_FLESH, stack.getCount()));
				}

				RewardsUtil.sendMessageToPlayer(player, "Ewwww it's all rotten");
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":thrown_in_air", -35)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				int px = (int) Math.floor(player.getX());
				int py = (int) Math.floor(player.getY()) + 1;
				int pz = (int) Math.floor(player.getZ());

				for(int y = 0; y < 40; y++)
					for(int x = -1; x < 2; x++)
						for(int z = -1; z < 2; z++)
							RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(px + x, py + y, pz + z));

				Scheduler.scheduleTask(new Task("Thrown_In_Air_Reward", 5)
				{
					@Override
					public void callback()
					{
						player.setJumping(true);
						player.setOnGround(false);
						player.setDeltaMovement(0, 20, 0);
						((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":torches_to_creepers", -40)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				int xRadius = super.getSettingAsInt(settings, "xRadius", 64, 0, 100) / 2;
				int yRadius = super.getSettingAsInt(settings, "yRadius", 64, 0, 100) / 2;
				int zRadius = super.getSettingAsInt(settings, "zRadius", 64, 0, 100) / 2;
				int maxCreepers = super.getSettingAsInt(settings, "maxCreepers", 50, -1, Integer.MAX_VALUE) / 2;
				int spawned = 0;
				for(int yy = -yRadius; yy <= yRadius; yy++)
				{
					for(int xx = -xRadius; xx <= xRadius; xx++)
					{
						for(int zz = -zRadius; zz <= zRadius; zz++)
						{
							BlockState b = level.getBlockState(pos.offset(xx, yy, zz));
							if(b.getLightEmission(level, pos) > 0 && b.getBlock() != Blocks.LAVA && !b.hasBlockEntity())
							{
								RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(xx, yy, zz));
								Creeper creeper = EntityType.CREEPER.create(level);
								creeper.moveTo(pos.getX() + xx + 0.5, pos.getY() + yy, pos.getZ() + zz + 0.5, 0, 0);
								level.addFreshEntity(creeper);
								spawned++;
								if(spawned >= maxCreepers && maxCreepers != -1)
									return;
							}
						}
					}
				}
				RewardsUtil.sendMessageToPlayer(player, "Those lights seem a little weird.... O.o");
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":traveller", 15)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				int x = RewardsUtil.rand.nextInt(1000) + 200;
				int z = RewardsUtil.rand.nextInt(1000) + 200;

				BlockPos newPos = pos.offset(x, 0, z);
				RewardsUtil.placeBlock(Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST), level, newPos);
				ChestBlockEntity chest = (ChestBlockEntity) level.getBlockEntity(newPos);
				for(int i = 0; i < 10; i++)
					chest.setItem(i, new ItemStack(RewardsUtil.getRandomItem()));

				RewardsUtil.sendMessageToNearPlayers(level, pos, 25, "" + newPos.getX() + ", " + newPos.getY() + ", " + newPos.getZ());
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":troll_hole", -20)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				final BlockPos worldPos = new BlockPos(Math.floor(player.getX()), Math.floor(player.getY()) - 1, Math.floor(player.getZ()));
				final RewardBlockCache cache = new RewardBlockCache(level, worldPos, new BlockPos(worldPos.getX(), worldPos.getY() + 1, worldPos.getZ()));

				for(int y = 0; y > -75; y--)
					for(int x = -2; x < 3; x++)
						for(int z = -2; z < 3; z++)
							cache.cacheBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());

				Scheduler.scheduleTask(new Task("TrollHole", 35)
				{
					@Override
					public void callback()
					{
						cache.restoreBlocks(player);
						player.fallDistance = 0;
						player.setDeltaMovement(0, 0, 0);
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":saw_nothing_diamond", 0)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				ItemEntity itemEnt = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.DIAMOND, 1));
				itemEnt.setNeverPickUp();
				level.addFreshEntity(itemEnt);

				Scheduler.scheduleTask(new Task("Saw_Nothing_Diamond", 100)
				{
					@Override
					public void callback()
					{
						itemEnt.remove(Entity.RemovalReason.DISCARDED);
						RewardsUtil.sendMessageToPlayer(player, "You didn't see anything......");
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":hand_enchant", 20)
		{
			@Override
			public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
			{
				ItemStack toEnchant;
				if(!player.getMainHandItem().isEmpty())
				{
					toEnchant = player.getMainHandItem();
				}
				else
				{
					List<ItemStack> stacks = new ArrayList<>();
					for(ItemStack stack : player.getInventory().items)
						if(!stack.isEmpty())
							stacks.add(stack);

					for(ItemStack stack : player.getInventory().armor)
						if(!stack.isEmpty())
							stacks.add(stack);

					if(stacks.size() == 0)
					{
						ItemStack dirt = new ItemStack(Blocks.DIRT);
						dirt.setHoverName(new TextComponent("A lonley piece of dirt"));
						player.getInventory().add(dirt);
						RewardsUtil.executeCommand(level, player, player.getOnPos(), "/advancement grant @p only chancecubes:lonely_dirt");
						return;
					}

					toEnchant = stacks.get(RewardsUtil.rand.nextInt(stacks.size()));
				}

				CustomEntry<Enchantment, Integer> enchantment = RewardsUtil.getRandomEnchantmentAndLevel();
				toEnchant.enchant(enchantment.getKey(), enchantment.getValue());
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new AnvilRain());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new HerobrineReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new SurroundedReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new CreeperSurroundedReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new WitherReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new TrollTNTReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new WaitForItReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new ClearInventoryReward(), false);
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
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BossRavagerReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new CursedHeadReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new WheelSpinReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new Connect4Reward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new LootBoxReward());

		MathReward math = new MathReward();
		MinecraftForge.EVENT_BUS.register(math);
		GlobalCCRewardRegistry.DEFAULT.registerReward(math);

		QuestionsReward question = new QuestionsReward();
		MinecraftForge.EVENT_BUS.register(question);
		GlobalCCRewardRegistry.DEFAULT.registerReward(question);

		CoinFlipReward coinFlip = new CoinFlipReward();
		MinecraftForge.EVENT_BUS.register(coinFlip);
		GlobalCCRewardRegistry.DEFAULT.registerReward(coinFlip);

		JarGuessReward jarGuess = new JarGuessReward();
		MinecraftForge.EVENT_BUS.register(jarGuess);
		GlobalCCRewardRegistry.DEFAULT.registerReward(jarGuess);
	}
}
