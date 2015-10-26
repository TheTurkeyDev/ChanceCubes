package chanceCubes.registry;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.items.ItemChancePendant;
import chanceCubes.rewards.AnvilRain;
import chanceCubes.rewards.BasicReward;
import chanceCubes.rewards.BlindnessFightReward;
import chanceCubes.rewards.ChargedCreeperReward;
import chanceCubes.rewards.ClearInventoryReward;
import chanceCubes.rewards.CookieMonsterReward;
import chanceCubes.rewards.CreeperSurroundedReward;
import chanceCubes.rewards.EnderCrystalTimerReward;
import chanceCubes.rewards.FiveProngReward;
import chanceCubes.rewards.HerobrineReward;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.InventoryChestReward;
import chanceCubes.rewards.ItemOfDestinyReward;
import chanceCubes.rewards.MathReward;
import chanceCubes.rewards.NukeReward;
import chanceCubes.rewards.RandomTeleportReward;
import chanceCubes.rewards.SurroundedReward;
import chanceCubes.rewards.ThrownInAirReward;
import chanceCubes.rewards.TrollHoleReward;
import chanceCubes.rewards.TrollTNTReward;
import chanceCubes.rewards.WaitForItReward;
import chanceCubes.rewards.WitherReward;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ExpirencePart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.PotionPart;
import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.PotionRewardType;
import chanceCubes.rewards.type.SoundRewardType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ChanceCubeRegistry implements IRewardRegistry
{
	public static ChanceCubeRegistry INSTANCE = new ChanceCubeRegistry();

	private Map<String, IChanceCubeReward> nameToReward = Maps.newHashMap();
	private List<IChanceCubeReward> sortedRewards = Lists.newArrayList();

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public static void loadDefaultRewards()
	{
		if(!CCubesSettings.enableHardCodedRewards)
			return;

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Tnt_Structure", -40, new BlockRewardType(new OffsetBlock(-1, 0, -1, Blocks.tnt, true), new OffsetBlock(-1, 0, 0, Blocks.tnt, true), new OffsetBlock(-1, 0, 1, Blocks.tnt, true), new OffsetBlock(0, 0, -1, Blocks.tnt, true), new OffsetBlock(0, 0, 0, Blocks.tnt, true), new OffsetBlock(0, 0, 1, Blocks.tnt, true), new OffsetBlock(1, 0, -1, Blocks.tnt, true), new OffsetBlock(1, 0, 0, Blocks.tnt, true), new OffsetBlock(1, 0, 1, Blocks.tnt, true), new OffsetBlock(-1, 1, -1, Blocks.redstone_block, true, 60), new OffsetBlock(-1, 1, 0, Blocks.redstone_block, true, 60), new OffsetBlock(-1, 1, 1, Blocks.redstone_block, true, 60), new OffsetBlock(0, 1, -1, Blocks.redstone_block, true, 60), new OffsetBlock(0, 1, 0, Blocks.redstone_block, true, 60), new OffsetBlock(0, 1, 1, Blocks.redstone_block, true, 60), new OffsetBlock(1, 1, -1, Blocks.redstone_block, true, 60), new OffsetBlock(1, 1, 0, Blocks.redstone_block, true, 60), new OffsetBlock(1, 1, 1, Blocks.redstone_block, true, 60))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":BedRock", -50, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.bedrock, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Redstone_Diamond", 50, new ItemRewardType(new ItemPart(new ItemStack(Items.redstone)), new ItemPart(new ItemStack(Items.diamond)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sethbling_Reward", 55, new MessageRewardType(new MessagePart("Welcome back, SethBling here :)")), new ItemRewardType(new ItemPart(new ItemStack(Items.redstone, 32)), new ItemPart(new ItemStack(Items.repeater, 3)), new ItemPart(new ItemStack(Items.comparator, 3)), new ItemPart(new ItemStack(Blocks.redstone_lamp, 3)), new ItemPart(new ItemStack(Blocks.redstone_torch, 3)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Creeper", -20, new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("Creeper")))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP", 35, new ExperienceRewardType(new ExpirencePart(100).setNumberofOrbs(10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP_Shower", 35, new ExperienceRewardType(new ExpirencePart(10), new ExpirencePart(10, 10), new ExpirencePart(10, 10), new ExpirencePart(10, 20), new ExpirencePart(10, 30), new ExpirencePart(10, 40), new ExpirencePart(10, 50), new ExpirencePart(10, 60), new ExpirencePart(10, 70), new ExpirencePart(10, 80), new ExpirencePart(10, 90), new ExpirencePart(10, 100), new ExpirencePart(10, 110), new ExpirencePart(10, 120), new ExpirencePart(10, 130), new ExpirencePart(10, 140), new ExpirencePart(10, 150))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Poison", -25, new PotionRewardType(new PotionPart(new PotionEffect(Potion.poison.id, 16 * 20)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":ChatMessage", 0, new MessageRewardType(new MessagePart("You have escaped the wrath of the Chance Cubes........."), new MessagePart("For now......"))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Command", 15, new CommandRewardType(" /give %player minecraft:painting 1 0 {display:{Name:\"Wylds Bestest friend\",Lore:[\"You know you love me, \"]}}")));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Particles", 0, new ParticleEffectRewardType("smoke", "smoke", "smoke", "smoke", "smoke", "smoke", "smoke", "smoke", "smoke")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Wool", 25, new ItemRewardType(new ItemPart(new ItemStack(Blocks.wool, 4, 0)), new ItemPart(new ItemStack(Blocks.wool, 4, 1)), new ItemPart(new ItemStack(Blocks.wool, 4, 2)), new ItemPart(new ItemStack(Blocks.wool, 4, 3)), new ItemPart(new ItemStack(Blocks.wool, 4, 4)), new ItemPart(new ItemStack(Blocks.wool, 4, 5)), new ItemPart(new ItemStack(Blocks.wool, 4, 6)), new ItemPart(new ItemStack(Blocks.wool, 4, 7)), new ItemPart(new ItemStack(Blocks.wool, 4, 8)), new ItemPart(new ItemStack(Blocks.wool, 4, 9)), new ItemPart(new ItemStack(Blocks.wool, 4, 10)), new ItemPart(new ItemStack(Blocks.wool, 4, 11)), new ItemPart(new ItemStack(Blocks.wool, 4, 12)), new ItemPart(new ItemStack(Blocks.wool, 4, 13)), new ItemPart(new ItemStack(Blocks.wool, 4, 14)), new ItemPart(new ItemStack(Blocks.wool, 4, 15)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Beacon", 100, new ItemRewardType(new ItemPart(new ItemStack(Blocks.beacon)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cake", 80, new ItemRewardType(new ItemPart(new ItemStack(Items.cake, 1))), new MessageRewardType(new MessagePart("But is it a lie?"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Enchanting", 95, new ItemRewardType(new ItemPart(new ItemStack(Blocks.enchanting_table)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bookshelves", 50, new ItemRewardType(new ItemPart(new ItemStack(Blocks.bookshelf, 8)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Ores_Galore", 65, new ItemRewardType(new ItemPart(new ItemStack(Items.coal)), new ItemPart(new ItemStack(Items.redstone)), new ItemPart(new ItemStack(Items.iron_ingot)), new ItemPart(new ItemStack(Items.gold_ingot)), new ItemPart(new ItemStack(Items.diamond)), new ItemPart(new ItemStack(Items.emerald)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Have_Another", 0, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.chanceCube, 3))), new MessageRewardType(new MessagePart("Here, have more!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Icsahedron", 0, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.chanceIcosahedron)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Saplings", 35, new ItemRewardType(new ItemPart(new ItemStack(Blocks.sapling, 4, 0)), new ItemPart(new ItemStack(Blocks.sapling, 4, 1)), new ItemPart(new ItemStack(Blocks.sapling, 4, 2)), new ItemPart(new ItemStack(Blocks.sapling, 4, 3)), new ItemPart(new ItemStack(Blocks.sapling, 4, 4)), new ItemPart(new ItemStack(Blocks.sapling, 4, 5)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Farmer", 35, new MessageRewardType(new MessagePart("Time to farm!")), new ItemRewardType(new ItemPart(new ItemStack(Items.iron_hoe)), new ItemPart(new ItemStack(Items.bucket)), new ItemPart(new ItemStack(Items.wheat_seeds, 16)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rancher", 60, new ItemRewardType(new ItemPart(new ItemStack(Blocks.fence, 32)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 90)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 91)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 92)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fighter", 30, new MessageRewardType(new MessagePart("SPARTAAA!!!")), new ItemRewardType(new ItemPart(new ItemStack(Items.iron_sword)), new ItemPart(new ItemStack(Items.iron_helmet)), new ItemPart(new ItemStack(Items.iron_chestplate)), new ItemPart(new ItemStack(Items.iron_leggings)), new ItemPart(new ItemStack(Items.iron_boots)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":pssst", -5, new MessageRewardType(new MessagePart("Pssssst.... Over here!")), new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("Creeper")))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explorer", 45, new MessageRewardType(new MessagePart("Lets go on a journey!")), new ItemRewardType(new ItemPart(new ItemStack(Items.compass)), new ItemPart(new ItemStack(Items.clock)), new ItemPart(new ItemStack(Blocks.torch, 64)), new ItemPart(new ItemStack(Items.iron_pickaxe)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Mitas", 75, new ItemRewardType(new ItemPart(new ItemStack(Items.gold_nugget, 32)), new ItemPart(new ItemStack(Items.gold_ingot, 8)), new ItemPart(new ItemStack(Items.golden_carrot, 16)), new ItemPart(new ItemStack(Items.golden_helmet)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Horde", -65, new MessageRewardType(new MessagePart("Release the horde!")), new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")), new EntityPart(EntityRewardType.getBasicNBTForEntity("Zombie")))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Insta-Damage", -25, new PotionRewardType(new PotionPart(new PotionEffect(Potion.harm.id, 2)), new PotionPart(new PotionEffect(Potion.harm.id, 2)), new PotionPart(new PotionEffect(Potion.harm.id, 2)), new PotionPart(new PotionEffect(Potion.harm.id, 2)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Lava_Ring", -70, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 0, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, -1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, -1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, -1, Blocks.lava, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rain", 0, new CommandRewardType(new CommandPart("/weather thunder 20000"))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":House", 75, new SchematicRewardType("house.schematic", 3, true, false)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Silverfish_Surround", -35, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.monster_egg, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fish_Dog", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.fish, 5)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 95)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bone_Cat", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.bone, 5)), new ItemPart(new ItemStack(Items.spawn_egg, 1, 98)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":XP_Crystal", -75, new CommandRewardType(new CommandPart("/summon EnderCrystal %x %y %z {Value:1,Riding:{id:\"XPOrb\"}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Cat", -40, new CommandRewardType(new CommandPart("/summon PrimedTnt %x %y %z {Fuse:80,Riding:{id:\"Ozelot\",CatType:0,Sitting:0}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":SlimeMan", 10, new CommandRewardType(new CommandPart("/summon Slime %x %y %z {CustomName:\"SlimeMan\",CustomNameVisible:1,Size:1,Riding:{id:\"Slime\",Size:2,Riding:{id:\"Slime\",Size:3}}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sail_Away", 5, new BlockRewardType(new OffsetBlock(0, -1, 0, Blocks.water, false)), new CommandRewardType(new CommandPart("/summon Boat %x %y %z")), new MessageRewardType(new MessagePart("Come sail away!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Witch", -40, new CommandRewardType(new CommandPart("/summon Witch %x %y %z "))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Cluckington", 40, new CommandRewardType(new CommandPart("/summon Zombie ~ ~1 ~ {CustomName:\"Wyld\",CustomNameVisible:1,IsBaby:1,Riding:{id:\"Chicken\",CustomName:\"Cluckinton\",CustomNameVisible:1,IsChickenJockey:1}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Jerry", 40, new CommandRewardType(new CommandPart("/summon Slime %x %y %z {Size:1,CustomName:\"Jerry\",CustomNameVisible:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Glenn", 40, new CommandRewardType(new CommandPart("/summon Zombie %x %y %z {CustomName:\"Glenn\",CustomNameVisible:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Want_To_Build_A_Snowman", 45, new MessageRewardType(new MessagePart("Do you want to build a snowman?")), new ItemRewardType(new ItemPart(new ItemStack(Blocks.snow, 2)), new ItemPart(new ItemStack(Blocks.pumpkin)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":You_saw_nothing", 0, new MessageRewardType(new MessagePart("You didn't see anything......"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Diamond_Block", 85, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.diamond_block, true, 200))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Diamond", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.diamond_block, false), new OffsetBlock(0, -1, 0, Blocks.diamond_block, false), new OffsetBlock(1, 0, 0, Blocks.diamond_block, false), new OffsetBlock(-1, 0, 0, Blocks.diamond_block, false), new OffsetBlock(0, 0, 1, Blocks.diamond_block, false), new OffsetBlock(0, 0, -1, Blocks.diamond_block, false)), new CommandRewardType(new CommandPart("/summon PrimedTnt %x %y %z {Fuse:40}"), new CommandPart("/summon PrimedTnt %x %y %z {Fuse:40}"), new CommandPart("/summon PrimedTnt %x %y %z {Fuse:40}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Anti_Slab", -15, new BlockRewardType(new OffsetBlock(-1, 2, -1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, -1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(1, 2, -1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(-1, 2, 0, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(1, 2, 0, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(-1, 2, 1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(1, 2, 1, Blocks.obsidian, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Chance_Cube_Cube", -10, new BlockRewardType(new OffsetBlock(-1, 0, -1, CCubesBlocks.chanceCube, false), new OffsetBlock(-1, 0, -2, CCubesBlocks.chanceCube, false), new OffsetBlock(-2, 0, -1, CCubesBlocks.chanceCube, false), new OffsetBlock(-2, 0, -2, CCubesBlocks.chanceCube, false), new OffsetBlock(-1, 1, -1, CCubesBlocks.chanceCube, false), new OffsetBlock(-1, 1, -2, CCubesBlocks.chanceCube, false), new OffsetBlock(-2, 1, -1, CCubesBlocks.chanceCube, false), new OffsetBlock(-2, 1, -2, CCubesBlocks.chanceCube, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fake_TNT", 0, new SoundRewardType(new SoundPart("game.tnt.primed"), new SoundPart("game.tnt.primed"), new SoundPart("game.tnt.primed"), new SoundPart("game.tnt.primed"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Ghasts", 0, new SoundRewardType(new SoundPart("mob.ghast.scream"), new SoundPart("mob.ghast.moan"), new SoundPart("mob.ghast.moan"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":No", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.chanceCube, false)), new MessageRewardType(new MessagePart("No"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invizible_Creeper", -75, new CommandRewardType(new CommandPart("/summon Creeper %x %y %z {ActiveEffects:[{Id:14,Amplifier:0,Duration:200,ShowParticles:0b}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Knockback_Zobmie", -55, new CommandRewardType(new CommandPart("/summon Zombie ~ ~1 ~ {CustomName:\"Leonidas\",IsBaby:1,Equipment:[{id:280,Count:1,tag:{ench:[{id:19,lvl:10}]}},{},{},{},{}],DropChances:[0.0F,0.085F,0.085F,0.085F,0.085F]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Dr_Trayaurus", 40, new CommandRewardType(new CommandPart("/summon Villager %x %y %z {CustomName:\"Dr Trayaurus\",CustomNameVisible:1,Profession:1}"))));

		ItemStack stack;

		stack = new ItemStack(Items.stick);
		stack.addEnchantment(Enchantment.sharpness, 5);
		stack.setStackDisplayName("A Big Stick");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Roosevelt's_Stick", 90, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.fishing_rod);
		stack.setItemDamage(stack.getMaxDamage() / 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Half_Fishingrod", 5, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.golden_apple, 1, 1);
		stack.setStackDisplayName("Notch");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Notch", 100, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.nether_star);
		stack.setStackDisplayName("North Star");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Star", 100, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.diamond_sword);
		stack.addEnchantment(Enchantment.sharpness, 10);
		stack.addEnchantment(Enchantment.unbreaking, 10);
		stack.setItemDamage(stack.getMaxDamage() - 2);
		stack.setStackDisplayName("The Divine Sword");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Divine", 100, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.bow);
		stack.setItemDamage(stack.getMaxDamage() - 1);
		stack.addEnchantment(Enchantment.power, 5);
		stack.addEnchantment(Enchantment.punch, 3);
		stack.addEnchantment(Enchantment.flame, 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":One_Shot", 100, new ItemRewardType(new ItemPart(stack), new ItemPart(new ItemStack(Items.arrow, 1)))));

		stack = new ItemStack(Items.fish, 1, 2);
		stack.setStackDisplayName("Nemo");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Nemo", 15, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.fish, 1, 2);
		stack.setStackDisplayName("Marlin");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Marlin", 15, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Blocks.fire, 1);
		stack.addEnchantment(Enchantment.fireAspect, 2);
		stack.setStackDisplayName("Why not?");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fire_Aspect_Fire", 70, new ItemRewardType(new ItemPart(stack))));

		OffsetBlock[] blocks = new OffsetBlock[35];
		int i = 0;
		for(int y = 0; y < 2; y++)
		{
			for(int x = 0; x < 5; x++)
			{
				for(int z = 0; z < 5; z++)
				{
					if(y == 1 && (x == 0 || x == 4 || z == 0 || z == 4))
					{
						continue;
					}
					else
					{
						blocks[i] = new OffsetBlock(x - 2, y, z - 2, Blocks.iron_block, true, ((x * 5) + z) * (y + 1) * 5);
						i++;
					}
				}
			}
		}
		blocks[i] = new OffsetBlock(0, 2, 0, Blocks.beacon, true, 250);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Beacon_Build", 100, new BlockRewardType(blocks)));

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
		INSTANCE.registerReward(new ClearInventoryReward());
		INSTANCE.registerReward(new InventoryChestReward());
		INSTANCE.registerReward(new ItemOfDestinyReward());
		INSTANCE.registerReward(new ThrownInAirReward());
		//INSTANCE.registerReward(new PandorasBoxReward());

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

	@Override
	public void registerReward(IChanceCubeReward reward)
	{
		if(ConfigLoader.config.getBoolean(reward.getName(), ConfigLoader.rewardCat, true, "") && !this.nameToReward.containsKey(reward.getName()))
		{
			nameToReward.put(reward.getName(), reward);
			redoSort(reward);
		}
	}

	@Override
	public boolean unregisterReward(String name)
	{
		Object o = nameToReward.remove(name);
		if(o != null)
			return sortedRewards.remove(o);
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
		int pick = world.rand.nextInt(range) + lowerIndex;
		CCubesCore.logger.log(Level.INFO, "Triggered the reward with the name of: " + sortedRewards.get(pick).getName());
		sortedRewards.get(pick).trigger(world, x, y, z, player);
	}

	private void redoSort(@Nullable IChanceCubeReward newReward)
	{
		if(newReward != null)
		{
			sortedRewards.add(newReward);
		}

		Collections.sort(sortedRewards, new Comparator<IChanceCubeReward>()
		{
			public int compare(IChanceCubeReward o1, IChanceCubeReward o2)
			{
				return o1.getChanceValue() - o2.getChanceValue();
			};
		});
	}
}