package chanceCubes.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.items.ItemChancePendant;
import chanceCubes.rewards.defaultRewards.AnvilRain;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.defaultRewards.BlindnessFightReward;
import chanceCubes.rewards.defaultRewards.BookOfMemesReward;
import chanceCubes.rewards.defaultRewards.ChargedCreeperReward;
import chanceCubes.rewards.defaultRewards.ClearInventoryReward;
import chanceCubes.rewards.defaultRewards.CookieMonsterReward;
import chanceCubes.rewards.defaultRewards.CreeperSurroundedReward;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import chanceCubes.rewards.defaultRewards.DiscoReward;
import chanceCubes.rewards.defaultRewards.EnderCrystalTimerReward;
import chanceCubes.rewards.defaultRewards.FiveProngReward;
import chanceCubes.rewards.defaultRewards.HerobrineReward;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.defaultRewards.InventoryBombReward;
import chanceCubes.rewards.defaultRewards.InventoryChestReward;
import chanceCubes.rewards.defaultRewards.ItemOfDestinyReward;
import chanceCubes.rewards.defaultRewards.JukeBoxReward;
import chanceCubes.rewards.defaultRewards.MathReward;
import chanceCubes.rewards.defaultRewards.NukeReward;
import chanceCubes.rewards.defaultRewards.RandomTeleportReward;
import chanceCubes.rewards.defaultRewards.RemoveUsefulThingsReward;
import chanceCubes.rewards.defaultRewards.SurroundedReward;
import chanceCubes.rewards.defaultRewards.TableFlipReward;
import chanceCubes.rewards.defaultRewards.ThrownInAirReward;
import chanceCubes.rewards.defaultRewards.TrollHoleReward;
import chanceCubes.rewards.defaultRewards.TrollTNTReward;
import chanceCubes.rewards.defaultRewards.WaitForItReward;
import chanceCubes.rewards.defaultRewards.WitherReward;
import chanceCubes.rewards.defaultRewards.ZombieCopyCatReward;
import chanceCubes.rewards.giantRewards.PotionsReward;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ExpirencePart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.OffsetTileEntity;
import chanceCubes.rewards.rewardparts.ParticlePart;
import chanceCubes.rewards.rewardparts.PotionPart;
import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.ParticleEffectRewardType;
import chanceCubes.rewards.type.PotionRewardType;
import chanceCubes.rewards.type.SoundRewardType;
import chanceCubes.util.RewardsUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ChanceCubeRegistry implements IRewardRegistry
{
	private static Random random = new Random();
	public static ChanceCubeRegistry INSTANCE = new ChanceCubeRegistry();

	private Map<String, IChanceCubeReward> nameToReward = Maps.newHashMap();
	private List<IChanceCubeReward> sortedRewards = Lists.newArrayList();
	private Map<String, IChanceCubeReward> disabledNameToReward = Maps.newHashMap();

	private static IChanceCubeReward lastReward = null;

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public static void loadDefaultRewards()
	{
		if(!CCubesSettings.enableHardCodedRewards)
			return;

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Tnt_Structure", -30, new BlockRewardType(RewardsUtil.addBlocksLists(RewardsUtil.fillArea(3, 1, 3, Blocks.tnt, -1, 0, -1, true, 0, false, false), RewardsUtil.fillArea(3, 1, 3, Blocks.redstone_block, -1, 1, -1, true, 30, false, false)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":BedRock", -20, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.bedrock, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Redstone_Diamond", 10, new ItemRewardType(new ItemPart(new ItemStack(Items.redstone)), new ItemPart(new ItemStack(Items.diamond)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sethbling_Reward", 30, new MessageRewardType(new MessagePart("Welcome back, SethBling here :)").setRange(32)), new ItemRewardType(new ItemPart(new ItemStack(Items.redstone, 32)), new ItemPart(new ItemStack(Items.repeater, 3)), new ItemPart(new ItemStack(Items.comparator, 3)), new ItemPart(new ItemStack(Blocks.redstone_lamp, 3)), new ItemPart(new ItemStack(Blocks.redstone_torch, 3)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP", 40, new ExperienceRewardType(new ExpirencePart(100).setNumberofOrbs(10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP_Shower", 40, new ExperienceRewardType(new ExpirencePart(10), new ExpirencePart(10, 10), new ExpirencePart(10, 10), new ExpirencePart(10, 20), new ExpirencePart(10, 30), new ExpirencePart(10, 40), new ExpirencePart(10, 50), new ExpirencePart(10, 60), new ExpirencePart(10, 70), new ExpirencePart(10, 80), new ExpirencePart(10, 90), new ExpirencePart(10, 100), new ExpirencePart(10, 110), new ExpirencePart(10, 120), new ExpirencePart(10, 130), new ExpirencePart(10, 140), new ExpirencePart(10, 150))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Poison", -15, new PotionRewardType(new PotionPart(new PotionEffect(Potion.poison.id, 500)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":ChatMessage", 0, new MessageRewardType(new MessagePart("You have escaped the wrath of the Chance Cubes.........").setRange(32), new MessagePart("For now......").setRange(32))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Command", 15, new CommandRewardType(" /give %player minecraft:painting 1 0 {display:{Name:\"Wylds Bestest friend\",Lore:[\"You know you love me, \"]}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Hearts", 0, new ParticleEffectRewardType(RewardsUtil.spawnXParticles("heart", 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explosion", 0, new ParticleEffectRewardType(new ParticlePart("hugeexplosion")), new SoundRewardType(new SoundPart("random.explode"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Wool", 25, new ItemRewardType(new ItemPart(new ItemStack(Blocks.wool, 4, 0)), new ItemPart(new ItemStack(Blocks.wool, 4, 1)), new ItemPart(new ItemStack(Blocks.wool, 4, 2)), new ItemPart(new ItemStack(Blocks.wool, 4, 3)), new ItemPart(new ItemStack(Blocks.wool, 4, 4)), new ItemPart(new ItemStack(Blocks.wool, 4, 5)), new ItemPart(new ItemStack(Blocks.wool, 4, 6)), new ItemPart(new ItemStack(Blocks.wool, 4, 7)), new ItemPart(new ItemStack(Blocks.wool, 4, 8)), new ItemPart(new ItemStack(Blocks.wool, 4, 9)), new ItemPart(new ItemStack(Blocks.wool, 4, 10)), new ItemPart(new ItemStack(Blocks.wool, 4, 11)), new ItemPart(new ItemStack(Blocks.wool, 4, 12)), new ItemPart(new ItemStack(Blocks.wool, 4, 13)), new ItemPart(new ItemStack(Blocks.wool, 4, 14)), new ItemPart(new ItemStack(Blocks.wool, 4, 15)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Beacon", 90, new ItemRewardType(new ItemPart(new ItemStack(Blocks.beacon)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cake", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.cake, 1))), new MessageRewardType(new MessagePart("But is it a lie?").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Enchanting", 80, new ItemRewardType(new ItemPart(new ItemStack(Blocks.enchanting_table)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bookshelves", 60, new ItemRewardType(new ItemPart(new ItemStack(Blocks.bookshelf, 8)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Ores_Galore", 50, new ItemRewardType(new ItemPart(new ItemStack(Items.coal)), new ItemPart(new ItemStack(Items.redstone)), new ItemPart(new ItemStack(Items.iron_ingot)), new ItemPart(new ItemStack(Items.gold_ingot)), new ItemPart(new ItemStack(Items.diamond)), new ItemPart(new ItemStack(Items.emerald)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Have_Another", 0, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.chanceCube, 3))), new MessageRewardType(new MessagePart("I hear you like Chance Cubes.").setRange(32), new MessagePart("So I put some Chance Cubes in your Chance Cubes!").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Icsahedron", 0, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.chanceIcosahedron)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Saplings", 35, new MessageRewardType(new MessagePart("Seems you have purchased the saplings DLC").setRange(32)), new ItemRewardType(new ItemPart(new ItemStack(Blocks.sapling, 4, 0)), new ItemPart(new ItemStack(Blocks.sapling, 4, 1)), new ItemPart(new ItemStack(Blocks.sapling, 4, 2)), new ItemPart(new ItemStack(Blocks.sapling, 4, 3)), new ItemPart(new ItemStack(Blocks.sapling, 4, 4)), new ItemPart(new ItemStack(Blocks.sapling, 4, 5)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Farmer", 35, new MessageRewardType(new MessagePart("Time to farm!").setRange(32)), new ItemRewardType(new ItemPart(new ItemStack(Items.iron_hoe)), new ItemPart(new ItemStack(Items.bucket)), new ItemPart(new ItemStack(Items.wheat_seeds, 16)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rancher", 60, new ItemRewardType(new ItemPart(new ItemStack(Blocks.fence, 32)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 90)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 91)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 92)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fighter", 25, new MessageRewardType(new MessagePart("SPARTAAA!!!").setRange(32)), new ItemRewardType(new ItemPart(new ItemStack(Items.iron_sword)), new ItemPart(new ItemStack(Items.iron_helmet)), new ItemPart(new ItemStack(Items.iron_chestplate)), new ItemPart(new ItemStack(Items.iron_leggings)), new ItemPart(new ItemStack(Items.iron_boots)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":pssst", -5, new MessageRewardType(new MessagePart("Pssssst.... Over here!").setRange(32)), new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("Creeper")))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explorer", 30, new MessageRewardType(new MessagePart("Lets go on a journey!").setRange(32)), new ItemRewardType(new ItemPart(new ItemStack(Items.compass)), new ItemPart(new ItemStack(Items.clock)), new ItemPart(new ItemStack(Blocks.torch, 64)), new ItemPart(new ItemStack(Items.iron_pickaxe)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Mitas", 50, new ItemRewardType(new ItemPart(new ItemStack(Items.gold_nugget, 32)), new ItemPart(new ItemStack(Items.gold_ingot, 8)), new ItemPart(new ItemStack(Items.golden_carrot, 16)), new ItemPart(new ItemStack(Items.golden_helmet)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Horde", -25, new MessageRewardType(new MessagePart("Release the horde!").setRange(32)), new EntityRewardType(RewardsUtil.spawnXEntities(EntityRewardType.getBasicNBTForEntity("Zombie"), 15))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Insta-Damage", -15, new PotionRewardType(new PotionPart(new PotionEffect(Potion.harm.id, 2, 7)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Lava_Ring", -40, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 0, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, -1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, -1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, -1, Blocks.lava, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rain", -5, new CommandRewardType(new CommandPart("/weather thunder 20000"), new CommandPart("/time set day", 40))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":House", 75, new SchematicRewardType("house.schematic", 3, true, false)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Silverfish_Surround", -20, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.monster_egg, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fish_Dog", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.fish, 5)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 95)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bone_Cat", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.bone, 5)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 98)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":XP_Crystal", -60, new CommandRewardType(new CommandPart("/summon EnderCrystal %x %y %z {Value:1,Riding:{id:\"XPOrb\"}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Cat", -25, new CommandRewardType(new CommandPart("/summon PrimedTnt %x %y %z {Fuse:80,Riding:{id:\"Ozelot\",CatType:0,Sitting:0}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":SlimeMan", 10, new CommandRewardType(new CommandPart("/summon Slime %x %y %z {CustomName:\"SlimeMan\",CustomNameVisible:1,Size:1,Riding:{id:\"Slime\",Size:2,Riding:{id:\"Slime\",Size:3}}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sail_Away", 5, new BlockRewardType(new OffsetBlock(0, -1, 0, Blocks.water, false)), new CommandRewardType(new CommandPart("/summon Boat %x %y %z")), new MessageRewardType(new MessagePart("Come sail away!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Witch", -15, new CommandRewardType(new CommandPart("/summon Witch %x %y %z "))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Cluckington", 40, new CommandRewardType(new CommandPart("/summon Zombie ~ ~1 ~ {CustomName:\"Wyld\",CustomNameVisible:1,IsVillager:0,IsBaby:1,Equipment:[{id:267,Count:1,tag:{ench:[{id:16,lvl:3}]}},{id:301,Count:1,tag:{ench:[{id:0,lvl:3}],display:{color:16711867}}},{id:300,Count:1,tag:{ench:[{id:0,lvl:1}],display:{color:16711867}}},{id:299,Count:1,tag:{ench:[{id:0,lvl:3}],display:{color:16711867}}},{id:298,Count:1,tag:{ench:[{id:0,lvl:3}],display:{color:16711867}}}],DropChances:[0.0F,0.0F,0.0F,0.0F,0.0F],Riding:{id:\"Chicken\",CustomName:\"Cluckington\",CustomNameVisible:1,IsChickenJockey:1,ActiveEffects:[{Id:1,Amplifier:3,Duration:199980,ShowParticles:0b}]}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Jerry", 40, new CommandRewardType(new CommandPart("/summon Slime %x %y %z {Size:1,CustomName:\"Jerry\",CustomNameVisible:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Glenn", 40, new CommandRewardType(new CommandPart("/summon Zombie %x %y %z {CustomName:\"Glenn\",CustomNameVisible:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Dr_Trayaurus", 40, new CommandRewardType(new CommandPart("/summon Villager %x %y %z {CustomName:\"Dr Trayaurus\",CustomNameVisible:1,Profession:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Pickles", 40, new CommandRewardType(new CommandPart("/summon MushroomCow ~ ~1 ~ {Age:-10000,CustomName:\"pickles\"}")), new MessageRewardType(new MessagePart("Why is his name pickles? The world may never know").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Want_To_Build_A_Snowman", 20, new MessageRewardType(new MessagePart("Do you want to build a snowman?")), new ItemRewardType(new ItemPart(new ItemStack(Blocks.snow, 2)), new ItemPart(new ItemStack(Blocks.pumpkin)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Diamond_Block", 75, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.diamond_block, true, 200))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Diamond", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.diamond_block, false), new OffsetBlock(0, -1, 0, Blocks.diamond_block, false), new OffsetBlock(1, 0, 0, Blocks.diamond_block, false), new OffsetBlock(-1, 0, 0, Blocks.diamond_block, false), new OffsetBlock(0, 0, 1, Blocks.diamond_block, false), new OffsetBlock(0, 0, -1, Blocks.diamond_block, false)), new CommandRewardType(new CommandPart("/summon PrimedTnt %x %y %z {Fuse:40}", 5), new CommandPart("/summon PrimedTnt %x %y %z {Fuse:40}", 5), new CommandPart("/summon PrimedTnt %x %y %z {Fuse:40}", 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Anti_Slab", -15, new BlockRewardType(RewardsUtil.fillArea(3, 1, 3, Blocks.obsidian, -1, 2, -1, false, 0, false, true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fake_TNT", 0, new SoundRewardType(new SoundPart("game.tnt.primed"), new SoundPart("game.tnt.primed"), new SoundPart("game.tnt.primed"), new SoundPart("random.explode").setDelay(120))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Ghasts", 0, new SoundRewardType(new SoundPart("mob.ghast.scream").setServerWide(true), new SoundPart("mob.ghast.moan").setServerWide(true), new SoundPart("mob.ghast.moan").setServerWide(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":No", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.chanceCube, false)), new MessageRewardType(new MessagePart("No").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Creeper", -30, new CommandRewardType(new CommandPart("/summon Creeper %x %y %z {ActiveEffects:[{Id:14,Amplifier:0,Duration:200,ShowParticles:0b}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Knockback_Zobmie", -35, new CommandRewardType(new CommandPart("/summon Zombie ~ ~1 ~ {CustomName:\"Leonidas\",IsBaby:1,Equipment:[{id:280,Count:1,tag:{ench:[{id:19,lvl:20}]}},{},{},{},{}],DropChances:[0.0F,0.085F,0.085F,0.085F,0.085F]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Actual_Invisible_Ghast", -80, new CommandRewardType(new CommandPart("/summon Ghast ~ ~10 ~ {ActiveEffects:[{Id:14,Amplifier:0,Duration:2000,ShowParticles:0b}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Giant_Chance_Cube", -45, new BlockRewardType(RewardsUtil.fillArea(3, 3, 3, CCubesBlocks.chanceCube, -1, 0, -1, false, 0, true, false))), false);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fireworks", 0, new CommandRewardType(RewardsUtil.executeXCommands("/summon FireworksRocketEntity ~0 ~0 ~0 {LifeTime:5,Motion:[0.00,0.10,0.00],FireworksItem:{id:401,Count:1,tag:{Fireworks:{Explosions:[{Type:1,Flicker:0,Trail:1,Colors:[16711680],FadeColors:[16777215]}]}}}}", 4))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":STRING!", 7, new BlockRewardType(RewardsUtil.fillArea(11, 5, 11, Blocks.tripwire, -5, 0, -5, false, 0, false, true)), new MessageRewardType(new MessagePart("STRING!!!!").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Bats", -50, new CommandRewardType(RewardsUtil.executeXCommands("/summon PrimedTnt %x %y %z {Fuse:80,Riding:{id:\"Bat\"}}", 10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Jelly_Fish", -40, new CommandRewardType(RewardsUtil.executeXCommands("/summon LavaSlime ~ ~1 ~ {CustomName:\"Nether Jelly Fish\",CustomNameVisible:1,Size:3,Riding:{id:\"Bat\"}}", 10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Pig_Of_Destiny", 15, new CommandRewardType(new CommandPart("/summon Pig ~ ~1 ~ {CustomName:\"The Pig of Destiny\",CustomNameVisible:1,Equipment:[{},{id:301,Count:1,tag:{ench:[{id:7,lvl:100}]}},{id:300,Count:1,tag:{ench:[{id:7,lvl:100}]}},{id:299,Count:1,tag:{ench:[{id:7,lvl:100}]}},{id:298,Count:1,tag:{ench:[{id:7,lvl:100}]}}],DropChances:[0.085F,0.0F,0.0F,0.0F,0.0F]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Squid_Horde", 5, new MessageRewardType(new MessagePart("Release the horde!").setRange(32), new MessagePart("Of squids!!").setDelay(20).setRange(32)), new EntityRewardType(RewardsUtil.spawnXEntities(EntityRewardType.getBasicNBTForEntity("Squid"), 15))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":D-rude_SandStorm", -10, new BlockRewardType(RewardsUtil.fillArea(5, 3, 5, Blocks.sand, -2, 0, -2, true, 0, false, true)), new MessageRewardType(new MessagePart("Well that was  D-rude").setDelay(40))));

		ItemStack stack;

		stack = new ItemStack(Items.stick);
		stack.addEnchantment(Enchantment.sharpness, 5);
		stack.setStackDisplayName("A Big Stick");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Roosevelt's_Stick", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.fishing_rod);
		stack.setItemDamage(stack.getMaxDamage() / 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Half_Fishingrod", 5, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.golden_apple, 1, 1);
		stack.setStackDisplayName("Notch");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Notch", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.nether_star);
		stack.setStackDisplayName("North Star");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Star", 100, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.diamond_sword);
		stack.addEnchantment(Enchantment.sharpness, 10);
		stack.addEnchantment(Enchantment.unbreaking, 10);
		stack.setItemDamage(stack.getMaxDamage() - 2);
		stack.setStackDisplayName("The Divine Sword");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Divine", 85, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.wooden_pickaxe);
		stack.addEnchantment(Enchantment.efficiency, 10);
		stack.addEnchantment(Enchantment.fortune, 3);
		stack.setStackDisplayName("Giga Breaker");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Giga_Breaker", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.bow);
		stack.setItemDamage(stack.getMaxDamage());
		stack.addEnchantment(Enchantment.power, 5);
		stack.addEnchantment(Enchantment.punch, 3);
		stack.addEnchantment(Enchantment.flame, 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":One_Shot", 75, new ItemRewardType(new ItemPart(stack), new ItemPart(new ItemStack(Items.arrow, 1)))));

		stack = new ItemStack(Items.fish, 1, 2);
		stack.setStackDisplayName("Nemo");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Nemo", 10, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.fish, 1, 2);
		stack.setStackDisplayName("Marlin");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Marlin", 10, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Blocks.fire, 1);
		stack.addEnchantment(Enchantment.fireAspect, 2);
		stack.setStackDisplayName("Why not?");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fire_Aspect_Fire", 60, new ItemRewardType(new ItemPart(stack))));

		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = "The broken path";
		sign.signText[1] = "to succ";
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Path_To_Succ", 0, new BlockRewardType(new OffsetTileEntity(0, 0, -5, Blocks.standing_sign, ((TileEntity) sign), true, 20), new OffsetBlock(0, -1, 0, Blocks.cobblestone, true, 0), new OffsetBlock(0, -1, -1, Blocks.cobblestone, true, 4), new OffsetBlock(0, -1, -2, Blocks.cobblestone, true, 8), new OffsetBlock(0, -1, -3, Blocks.cobblestone, true, 12), new OffsetBlock(0, -1, -4, Blocks.cobblestone, true, 16), new OffsetBlock(0, -1, -5, Blocks.cobblestone, true, 20))));

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
					blocks[i] = new OffsetBlock(x - 2, y, z - 2, Blocks.iron_block, true, i * 5);
					i++;
				}
			}
		}
		blocks[i] = new OffsetBlock(0, 2, 0, Blocks.beacon, true, 200);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Beacon_Build", 100, new BlockRewardType(blocks)));

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":No_Exp", -40)
		{
			@Override
			public void trigger(World world, int x, int y, int z, EntityPlayer player)
			{
				player.experienceLevel = 0;
				player.experienceTotal = 0;
				player.addChatMessage(new ChatComponentText("Screw you last stand"));
			}
		});

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Smite", -10)
		{
			@Override
			public void trigger(World world, int x, int y, int z, EntityPlayer player)
			{
				world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ));
				player.addChatMessage(new ChatComponentText("Thou has been smitten!"));
			}
		});

		INSTANCE.registerReward(new NukeReward());
		INSTANCE.registerReward(new FiveProngReward());
		INSTANCE.registerReward(new AnvilRain());
		INSTANCE.registerReward(new HerobrineReward());
		INSTANCE.registerReward(new SurroundedReward());
		INSTANCE.registerReward(new CreeperSurroundedReward());
		INSTANCE.registerReward(new RandomTeleportReward());
		INSTANCE.registerReward(new TrollHoleReward());
		INSTANCE.registerReward(new CookieMonsterReward());
		INSTANCE.registerReward(new BlindnessFightReward());
		INSTANCE.registerReward(new WitherReward());
		INSTANCE.registerReward(new TrollTNTReward());
		INSTANCE.registerReward(new EnderCrystalTimerReward());
		INSTANCE.registerReward(new WaitForItReward());
		INSTANCE.registerReward(new ChargedCreeperReward());
		INSTANCE.registerReward(new ZombieCopyCatReward());
		INSTANCE.registerReward(new InventoryChestReward());
		INSTANCE.registerReward(new ItemOfDestinyReward());
		INSTANCE.registerReward(new ThrownInAirReward());
		INSTANCE.registerReward(new DiscoReward());
		INSTANCE.registerReward(new InventoryBombReward());
		INSTANCE.registerReward(new ClearInventoryReward(), false);
		INSTANCE.registerReward(new JukeBoxReward());
		INSTANCE.registerReward(new BookOfMemesReward());
		INSTANCE.registerReward(new RemoveUsefulThingsReward());
		INSTANCE.registerReward(new TableFlipReward());
		INSTANCE.registerReward(new PotionsReward());

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Half_Heart", -30)
		{
			@Override
			public void trigger(World world, int x, int y, int z, EntityPlayer player)
			{
				player.setHealth(1f);
			}
		});

		MathReward math = new MathReward();
		MinecraftForge.EVENT_BUS.register(math);
		INSTANCE.registerReward(math);
	}

	public static void loadCustomUserRewards()
	{
		ArrayList<EntityPlayerMP> allp = new ArrayList<EntityPlayerMP>();
		Iterator<?> iterator;

		for(int i = 0; i < MinecraftServer.getServer().worldServers.length; i++)
		{
			iterator = MinecraftServer.getServer().worldServers[i].playerEntities.listIterator();
			while(iterator.hasNext())
				allp.add((EntityPlayerMP) iterator.next());
		}

		for(EntityPlayerMP player : allp)
			new CustomUserReward(player);
	}

	@Override
	public void registerReward(IChanceCubeReward reward)
	{
		this.registerReward(reward, true);
	}

	public void registerReward(IChanceCubeReward reward, boolean enabledDefault)
	{
		if(ConfigLoader.config.getBoolean(reward.getName(), ConfigLoader.rewardCat, enabledDefault, "") && !this.nameToReward.containsKey(reward.getName()))
		{
			nameToReward.put(reward.getName(), reward);
			redoSort(reward);
		}
		else
		{
			this.disabledNameToReward.put(reward.getName(), reward);
		}
	}

	public boolean enableReward(String reward)
	{
		if(this.disabledNameToReward.containsKey(reward))
			return this.enableReward(this.disabledNameToReward.get(reward));
		return false;
	}

	public boolean enableReward(IChanceCubeReward reward)
	{
		this.disabledNameToReward.remove(reward.getName());
		nameToReward.put(reward.getName(), reward);
		redoSort(reward);
		return true;
	}

	@Override
	public boolean unregisterReward(String name)
	{
		IChanceCubeReward reward = nameToReward.remove(name);
		if(reward != null)
		{
			this.disabledNameToReward.put(name, reward);
			return sortedRewards.remove(reward);
		}
		return false;
	}

	@Override
	public IChanceCubeReward getRewardByName(String name)
	{
		return nameToReward.get(name);
	}

	@Override
	public void triggerRandomReward(World world, int x, int y, int z, EntityPlayer player, int chance)
	{
		if(this.sortedRewards.size() == 0)
		{
			CCubesCore.logger.log(Level.WARN, "There are no registered rewards with ChanceCubes and no reward was able to be given");
			return;
		}

		if(CCubesSettings.doesHolidayRewardTrigger && CCubesSettings.holidayReward != null)
		{
			CCubesSettings.holidayReward.trigger(world, x, y, z, player);
			CCubesSettings.doesHolidayRewardTrigger = false;
			CCubesSettings.holidayRewardTriggered = true;
			ConfigLoader.config.get(ConfigLoader.genCat, "HolidayRewardTriggered", false, "Don't touch! Well I mean you can touch it, if you want. I can't stop you. I'm only text.").setValue(true);
			ConfigLoader.config.save();
			return;
		}

		if(player != null)
		{
			for(int i = 0; i < player.inventory.mainInventory.length; i++)
			{
				ItemStack stack = player.inventory.mainInventory[i];
				if(stack != null && stack.getItem() instanceof ItemChancePendant)
				{
					ItemChancePendant pendant = (ItemChancePendant) stack.getItem();
					pendant.damage(stack);
					if(stack.getItemDamage() >= CCubesSettings.pendantUses)
						player.inventory.setInventorySlotContents(i, null);
					chance += pendant.getChanceIncrease();
					if(chance > 100)
						chance = 100;
				}
			}
		}

		int lowerIndex = 0;
		int upperIndex = sortedRewards.size() - 1;
		int lowerRange = chance - CCubesSettings.rangeMin < -100 ? -100 : chance - CCubesSettings.rangeMin;
		int upperRange = chance + CCubesSettings.rangeMax > 100 ? 100 : chance + CCubesSettings.rangeMax;

		while(sortedRewards.get(lowerIndex).getChanceValue() < lowerRange)
		{
			lowerIndex++;
			if(lowerIndex >= sortedRewards.size())
			{
				lowerIndex--;
				break;
			}
		}
		while(sortedRewards.get(upperIndex).getChanceValue() > upperRange)
		{
			upperIndex--;
			if(upperIndex < 0)
			{
				upperIndex++;
				break;
			}
		}
		int range = upperIndex - lowerIndex > 0 ? upperIndex - lowerIndex : 1;
		int pick = random.nextInt(range) + lowerIndex;
		IChanceCubeReward pickedReward = sortedRewards.get(pick);
		if(lastReward != null)
		{
			byte atempts = 0;
			while(atempts < 5 && lastReward.getName().equals(pickedReward.getName()))
			{
				pick = random.nextInt(range) + lowerIndex;
				pickedReward = sortedRewards.get(pick);
				atempts++;
			}
		}
		CCubesCore.logger.log(Level.INFO, "Triggered the reward with the name of: " + pickedReward.getName());
		pickedReward.trigger(world, x, y, z, player);
		lastReward = pickedReward;
	}

	private void redoSort(@Nullable IChanceCubeReward newReward)
	{
		if(newReward != null)
			sortedRewards.add(newReward);

		Collections.sort(sortedRewards, new Comparator<IChanceCubeReward>()
		{
			public int compare(IChanceCubeReward o1, IChanceCubeReward o2)
			{
				return o1.getChanceValue() - o2.getChanceValue();
			};
		});
	}

	public void ClearRewards()
	{
		this.sortedRewards.clear();
		this.nameToReward.clear();
		this.disabledNameToReward.clear();
	}
}