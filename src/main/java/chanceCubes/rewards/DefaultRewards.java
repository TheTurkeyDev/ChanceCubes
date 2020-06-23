package chanceCubes.rewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":silverfish_surround", -20, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 0, Blocks.INFESTED_COBBLESTONE, false).setRelativeToPlayer(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":fish_dog", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.COD, 5)), new ItemPart(new ItemStack(Items.WOLF_SPAWN_EGG)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":bone_cat", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.BONE, 5)), new ItemPart(new ItemStack(Items.OCELOT_SPAWN_EGG)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":tnt_cat", -25, new CommandRewardType("/summon ocelot ~ ~1 ~ {CatType:0,Sitting:0,Passengers:[{id:\"tnt\",Fuse:80}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":diamond_block", 85, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.DIAMOND_BLOCK, true, 200))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":tnt_diamond", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.DIAMOND_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":fake_tnt", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_GENERIC_EXPLODE, 120).setAtPlayersLocation(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":invisible_ghasts", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_GHAST_SCREAM).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":no", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.CHANCE_CUBE, false)), new MessageRewardType("No")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":coal_to_diamonds", 10, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.COAL_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5)), new ItemRewardType(new ItemPart(new ItemStack(Items.DIAMOND, 5), 50))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":one_man_army", -10, new EntityRewardType("zombie_pigman"), new CommandRewardType(RewardsUtil.executeXCommands("/summon zombie_pigman ~ ~ ~ {Silent:1,ActiveEffects:[{Id:14,Amplifier:0,Duration:19980,ShowParticles:1b}]}", 9)), new MessageRewardType(new MessagePart("One man army").setRange(32))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":arrow_trap", -25, new SchematicRewardType(new SchematicPart("/data/chancecubes/schematics/arrow_trap.ccs", true, new IntVar(1), new IntVar(-1), new IntVar(1), new FloatVar(0), new BoolVar(false), new BoolVar(true), new BoolVar(true), new BoolVar(true), new IntVar(0)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":trampoline", 15, new MessageRewardType("Time to bounce!"), new SchematicRewardType(new SchematicPart("/data/chancecubes/schematics/trampoline.ccs", true, new IntVar(1), new IntVar(-3), new IntVar(1), new FloatVar(0), new BoolVar(false), new BoolVar(true), new BoolVar(true), new BoolVar(true), new IntVar(0))), new BlockRewardType(new OffsetBlock(2, -2, -2, Blocks.REDSTONE_BLOCK, false, 3).setRelativeToPlayer(true).setCausesBlockUpdate(true), new OffsetBlock(2, -2, -2, Blocks.REDSTONE_WIRE, false, 5).setRelativeToPlayer(true).setCausesBlockUpdate(true))));

		CompoundNBT nbt;

		SignTileEntity sign = new SignTileEntity();
		sign.signText[0] = new StringTextComponent("The broken path");
		sign.signText[1] = new StringTextComponent("to succeed");
		nbt = new CompoundNBT();
		((TileEntity) sign).write(nbt);
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
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				player.setHealth(1f);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":no_exp", -40)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				player.experienceLevel = 0;
				player.experienceTotal = 0;
				player.experience = 0;
				player.sendMessage(new StringTextComponent("Rip EXP"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":smite", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				if(world.isRemote)
					return;
				((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, player.posX, player.posY, player.posZ, false));
				player.sendMessage(new StringTextComponent("Thou has been smitten!"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":cookie-splosion", 35)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				ItemEntity cookie;
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						cookie = new ItemEntity(world, pos.getX(), pos.getY() + 1D, pos.getZ(), new ItemStack(Items.COOKIE));
						world.addEntity(cookie);
						cookie.setMotion(xx, Math.random(), zz);
					}
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":random_status_effect", 0)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				player.sendMessage(new StringTextComponent("Selecting random potion effect to apply..."));

				Scheduler.scheduleTask(new Task("Cookie Monster", 30)
				{
					@Override
					public void callback()
					{
						EffectInstance effect = RewardsUtil.getRandomPotionEffectInstance();
						player.sendMessage(new StringTextComponent("You have been given: "));
						player.sendMessage(new TranslationTextComponent(effect.getEffectName()));
						player.sendMessage(new StringTextComponent("for " + (effect.getDuration() / 20) + " seconds!"));
						player.addPotionEffect(effect);
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":arrow_spray", -15)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				ArrowEntity arrow;
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						arrow = new ArrowEntity(world, player);
						arrow.setLocationAndAngles(pos.getX(), pos.getY() + 0.5f, pos.getZ(), 0, 0);
						arrow.setMotion(xx, .3, zz);
						world.addEntity(arrow);
					}
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":lingering_potions_ring", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				PotionEntity pot;
				for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 10))
				{
					pot = new PotionEntity(world, player);
					pot.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), RewardsUtil.getRandomPotionType()));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.setMotion(Math.cos(rad) * (0.1 + (0.05 * 3)), 1, Math.sin(rad) * (0.1 + (0.05 * 3)));
					world.addEntity(pot);
				}
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":charged_creeper", -40)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				if(world.isRemote)
					return;

				RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, 1, 0));
				CreeperEntity ent = EntityType.CREEPER.create(world);
				ent.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
				ent.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 300, 99, true, false));
				ent.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 300, 99, true, false));
				world.addEntity(ent);

				Scheduler.scheduleTask(new Task("Charged Creeper Reward", 2)
				{
					@Override
					public void callback()
					{
						((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, pos.getX(), pos.getY(), pos.getZ(), false));
						world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 1f, 1f);
						ent.setFire(0);
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":disco", 40)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				for(int xx = -4; xx < 5; xx++)
					for(int zz = -4; zz < 5; zz++)
						RewardsUtil.placeBlock(RewardsUtil.getRandomWool(), world, pos.add(xx, -1, zz));

				for(int i = 0; i < 10; i++)
				{
					SheepEntity sheep = EntityType.SHEEP.create(world);
					sheep.setCustomName(new StringTextComponent("jeb_"));
					sheep.setLocationAndAngles(pos.getX(), pos.getY() + 1, pos.getZ(), 0, 0);
					world.addEntity(sheep);
				}

				RewardsUtil.placeBlock(CCubesBlocks.CHANCE_ICOSAHEDRON.getDefaultState(), world, pos.add(0, 3, 0));

				RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "Disco Party!!!!");
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":ender_crystal_timer", -90)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				for(int i = 30; i > 0; i--)
					RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(0, i, 0));

				EnderCrystalEntity ent = EntityType.END_CRYSTAL.create(world);
				ent.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
				world.addEntity(ent);

				ArrowEntity arrow = new ArrowEntity(world, pos.getX() + 0.5, pos.getY() + 29, pos.getZ() + 0.5);
				arrow.setMotion(0, -0.25, 0);
				world.addEntity(arrow);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":5_prongs", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
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
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				player.inventory.dropAllItems();

				for(int i = 0; i < player.inventory.mainInventory.size(); i++)
					player.inventory.mainInventory.set(i, new ItemStack(Blocks.DEAD_BUSH, 64));

				for(int i = 0; i < player.inventory.armorInventory.size(); i++)
				{
					ItemStack stack = new ItemStack(Blocks.DEAD_BUSH, 64);
					if(i == 0)
					{
						stack.setDisplayName(new StringTextComponent("Button").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
						stack.setCount(13);
					}
					else if(i == 1)
					{
						stack.setDisplayName(new StringTextComponent("TheBlackswordsman"));
						stack.setCount(13);
					}
					player.inventory.armorInventory.set(i, stack);
				}

				player.sendMessage(new StringTextComponent("Inventory Bomb!!!!"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":nuke", -75)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "May death rain upon them");
				world.addEntity(new TNTEntity(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 6, player));
				world.addEntity(new TNTEntity(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 6, player));
				world.addEntity(new TNTEntity(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 6, player));
				world.addEntity(new TNTEntity(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 6, player));
				world.addEntity(new TNTEntity(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() - 2, player));
				world.addEntity(new TNTEntity(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() - 2, player));
				world.addEntity(new TNTEntity(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() - 2, player));
				world.addEntity(new TNTEntity(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() - 2, player));
				world.addEntity(new TNTEntity(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 2, player));
				world.addEntity(new TNTEntity(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 2, player));
				world.addEntity(new TNTEntity(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 2, player));
				world.addEntity(new TNTEntity(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 2, player));
				world.addEntity(new TNTEntity(world, pos.getX() - 6, pos.getY() + 65, pos.getZ() + 6, player));
				world.addEntity(new TNTEntity(world, pos.getX() - 2, pos.getY() + 65, pos.getZ() + 6, player));
				world.addEntity(new TNTEntity(world, pos.getX() + 2, pos.getY() + 65, pos.getZ() + 6, player));
				world.addEntity(new TNTEntity(world, pos.getX() + 6, pos.getY() + 65, pos.getZ() + 6, player));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":random_teleport", -15)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
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
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				for(int i = 0; i < player.inventory.mainInventory.size(); i++)
				{
					ItemStack stack = player.inventory.mainInventory.get(i);
					if(!stack.isEmpty() && stack.getItem().isFood())
						player.inventory.mainInventory.set(i, new ItemStack(Items.ROTTEN_FLESH, stack.getCount()));
				}

				player.sendMessage(new StringTextComponent("Ewwww it's all rotten"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":thrown_in_air", -35)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
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
						player.setMotion(0, 20, 0);
						((ServerPlayerEntity) player).connection.sendPacket(new SEntityVelocityPacket(player));
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":torches_to_creepers", -40)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				for(int yy = -32; yy <= 32; yy++)
				{
					for(int xx = -32; xx <= 32; xx++)
					{
						for(int zz = -32; zz <= 32; zz++)
						{
							BlockState b = world.getBlockState(pos.add(xx, yy, zz));
							if(b.getLightValue(world, pos) > 0 && b.getBlock() != Blocks.LAVA && !b.getBlock().hasTileEntity(b))
							{
								RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(xx, yy, zz));
								CreeperEntity creeper = EntityType.CREEPER.create(world);
								creeper.setLocationAndAngles(pos.getX() + xx + 0.5, pos.getY() + yy, pos.getZ() + zz + 0.5, 0, 0);
								world.addEntity(creeper);
							}
						}
					}
				}
				player.sendMessage(new StringTextComponent("Those lights seem a little weird.... O.o"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":traveller", 15)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				int x = RewardsUtil.rand.nextInt(1000) + 200;
				int z = RewardsUtil.rand.nextInt(1000) + 200;

				BlockPos newPos = pos.add(x, 0, z);
				RewardsUtil.placeBlock(Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.WEST), world, newPos);
				ChestTileEntity chest = (ChestTileEntity) world.getTileEntity(newPos);
				for(int i = 0; i < 10; i++)
					chest.setInventorySlotContents(i, new ItemStack(RewardsUtil.getRandomItem()));

				RewardsUtil.sendMessageToNearPlayers(world, pos, 25, "" + newPos.getX() + ", " + newPos.getY() + ", " + newPos.getZ());
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":troll_hole", -20)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
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
						player.fallDistance = 0;
						player.setMotion(0, 0, 0);
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":saw_nothing_diamond", 0)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
			{
				ItemEntity itemEnt = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.DIAMOND, 1));
				itemEnt.setInfinitePickupDelay();
				world.addEntity(itemEnt);

				Scheduler.scheduleTask(new Task("Saw_Nothing_Diamond", 100)
				{
					@Override
					public void callback()
					{
						itemEnt.remove();
						player.sendMessage(new StringTextComponent("You didn't see anything......"));
					}
				});
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":hand_enchant", 20)
		{
			@Override
			public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
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
						dirt.setDisplayName(new StringTextComponent("A lonley piece of dirt"));
						player.inventory.addItemStackToInventory(dirt);
						RewardsUtil.executeCommand(world, player, player.getPosition(), "/advancement grant @p only chancecubes:lonely_dirt");
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
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BossRavagerReward());
		GlobalCCRewardRegistry.DEFAULT.registerReward(new CursedHeadReward());

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
