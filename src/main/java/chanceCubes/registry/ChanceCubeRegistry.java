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
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.defaultRewards.AnvilRain;
import chanceCubes.rewards.defaultRewards.ArmorStandArmorReward;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.defaultRewards.BookOfMemesReward;
import chanceCubes.rewards.defaultRewards.CakeIsALieReward;
import chanceCubes.rewards.defaultRewards.ChargedCreeperReward;
import chanceCubes.rewards.defaultRewards.ClearInventoryReward;
import chanceCubes.rewards.defaultRewards.CookieMonsterReward;
import chanceCubes.rewards.defaultRewards.CreeperSurroundedReward;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import chanceCubes.rewards.defaultRewards.DidYouKnowReward;
import chanceCubes.rewards.defaultRewards.DiscoReward;
import chanceCubes.rewards.defaultRewards.DoubleRainbow;
import chanceCubes.rewards.defaultRewards.EnderCrystalTimerReward;
import chanceCubes.rewards.defaultRewards.FiveProngReward;
import chanceCubes.rewards.defaultRewards.HerobrineReward;
import chanceCubes.rewards.defaultRewards.InventoryBombReward;
import chanceCubes.rewards.defaultRewards.ItemOfDestinyReward;
import chanceCubes.rewards.defaultRewards.ItemRenamer;
import chanceCubes.rewards.defaultRewards.JukeBoxReward;
import chanceCubes.rewards.defaultRewards.MathReward;
import chanceCubes.rewards.defaultRewards.MazeReward;
import chanceCubes.rewards.defaultRewards.NukeReward;
import chanceCubes.rewards.defaultRewards.OneIsLuckyReward;
import chanceCubes.rewards.defaultRewards.QuestionsReward;
import chanceCubes.rewards.defaultRewards.RainingCatsAndCogsReward;
import chanceCubes.rewards.defaultRewards.RandomTeleportReward;
import chanceCubes.rewards.defaultRewards.RottenFoodReward;
import chanceCubes.rewards.defaultRewards.SkyblockReward;
import chanceCubes.rewards.defaultRewards.SurroundedReward;
import chanceCubes.rewards.defaultRewards.TableFlipReward;
import chanceCubes.rewards.defaultRewards.ThrownInAirReward;
import chanceCubes.rewards.defaultRewards.TorchesToCreepers;
import chanceCubes.rewards.defaultRewards.TrollHoleReward;
import chanceCubes.rewards.defaultRewards.TrollTNTReward;
import chanceCubes.rewards.defaultRewards.WaitForItReward;
import chanceCubes.rewards.defaultRewards.WitherReward;
import chanceCubes.rewards.defaultRewards.WolvesToCreepersReward;
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
import net.minecraft.block.BlockWallSign;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ChanceCubeRegistry implements IRewardRegistry
{
	public static ChanceCubeRegistry INSTANCE = new ChanceCubeRegistry();
	private static Random random = new Random();

	private List<IChanceCubeReward> customRewards = Lists.newArrayList();
	private Map<String, IChanceCubeReward> nameToReward = Maps.newHashMap();
	private List<IChanceCubeReward> sortedRewards = Lists.newArrayList();
	private Map<String, IChanceCubeReward> disabledNameToReward = Maps.newHashMap();

	private static IChanceCubeReward lastReward = null;

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public static void loadDefaultRewards()
	{
		RewardsUtil.initData();

		if(!CCubesSettings.enableHardCodedRewards)
			return;

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Tnt_Structure", -30, new BlockRewardType(RewardsUtil.addBlocksLists(RewardsUtil.fillArea(3, 1, 3, Blocks.TNT, -1, 0, -1, true, 0, false, false), RewardsUtil.fillArea(3, 1, 3, Blocks.REDSTONE_BLOCK, -1, 1, -1, true, 40, true, false)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":BedRock", -20, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.BEDROCK, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Redstone_Diamond", 10, new ItemRewardType(new ItemPart(new ItemStack(Items.REDSTONE)), new ItemPart(new ItemStack(Items.DIAMOND)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sethbling_Reward", 30, new MessageRewardType(new MessagePart("Welcome back, SethBling here :)")), new ItemRewardType(new ItemPart(new ItemStack(Items.REDSTONE, 32)), new ItemPart(new ItemStack(Items.REPEATER, 3)), new ItemPart(new ItemStack(Items.COMPARATOR, 3)), new ItemPart(new ItemStack(Blocks.REDSTONE_LAMP, 3)), new ItemPart(new ItemStack(Blocks.REDSTONE_TORCH, 3)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP", 35, new ExperienceRewardType(new ExpirencePart(100).setNumberofOrbs(10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP_Shower", 35, new ExperienceRewardType(new ExpirencePart(10), new ExpirencePart(10, 10), new ExpirencePart(10, 10), new ExpirencePart(10, 20), new ExpirencePart(10, 30), new ExpirencePart(10, 40), new ExpirencePart(10, 50), new ExpirencePart(10, 60), new ExpirencePart(10, 70), new ExpirencePart(10, 80), new ExpirencePart(10, 90), new ExpirencePart(10, 100), new ExpirencePart(10, 110), new ExpirencePart(10, 120), new ExpirencePart(10, 130), new ExpirencePart(10, 140), new ExpirencePart(10, 150))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Poison", -25, new PotionRewardType(new PotionPart(new PotionEffect(MobEffects.POISON, 500)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":ChatMessage", 0, new MessageRewardType(new MessagePart("You have escaped the wrath of the Chance Cubes........."), new MessagePart("For now......"))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Command", 15, new CommandRewardType(" /give %player minecraft:painting 1 0 {display:{Name:\"Wylds Bestest friend\",Lore:[\"You know you love me, \"]}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Hearts", 0, new ParticleEffectRewardType(RewardsUtil.spawnXParticles(34, 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explosion", 0, new ParticleEffectRewardType(new ParticlePart(2)), new SoundRewardType(new SoundPart(SoundEvents.ENTITY_GENERIC_EXPLODE))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Wool", 25, new ItemRewardType(new ItemPart(new ItemStack(Blocks.WOOL, 4, 0)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 1)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 2)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 3)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 4)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 5)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 6)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 7)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 8)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 9)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 10)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 11)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 12)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 13)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 14)), new ItemPart(new ItemStack(Blocks.WOOL, 4, 15)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Enchanting", 80, new ItemRewardType(new ItemPart(new ItemStack(Blocks.ENCHANTING_TABLE)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bookshelves", 60, new ItemRewardType(new ItemPart(new ItemStack(Blocks.BOOKSHELF, 8)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Ores_Galore", 50, new ItemRewardType(new ItemPart(new ItemStack(Items.COAL)), new ItemPart(new ItemStack(Items.REDSTONE)), new ItemPart(new ItemStack(Items.IRON_INGOT)), new ItemPart(new ItemStack(Items.GOLD_INGOT)), new ItemPart(new ItemStack(Items.DIAMOND)), new ItemPart(new ItemStack(Items.EMERALD)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Have_Another", 0, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.CHANCE_CUBE, 3))), new MessageRewardType(new MessagePart("I hear you like Chance Cubes."), new MessagePart("So I put some Chance Cubes in your Chance Cubes!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Icsahedron", 0, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Saplings", 35, new MessageRewardType(new MessagePart("Seems you have purchased the saplings DLC")), new ItemRewardType(new ItemPart(new ItemStack(Blocks.SAPLING, 4, 0)), new ItemPart(new ItemStack(Blocks.SAPLING, 4, 1)), new ItemPart(new ItemStack(Blocks.SAPLING, 4, 2)), new ItemPart(new ItemStack(Blocks.SAPLING, 4, 3)), new ItemPart(new ItemStack(Blocks.SAPLING, 4, 4)), new ItemPart(new ItemStack(Blocks.SAPLING, 4, 5)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Farmer", 35, new MessageRewardType(new MessagePart("Time to farm!")), new ItemRewardType(new ItemPart(new ItemStack(Items.IRON_HOE)), new ItemPart(new ItemStack(Items.BUCKET)), new ItemPart(new ItemStack(Items.WHEAT_SEEDS, 16)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rancher", 60, new ItemRewardType(new ItemPart(new ItemStack(Blocks.OAK_FENCE, 32)), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("Pig"))), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("Cow"))), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("Sheep"))))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fighter", 25, new MessageRewardType(new MessagePart("SPARTAAA!!!")), new ItemRewardType(new ItemPart(new ItemStack(Items.IRON_SWORD)), new ItemPart(new ItemStack(Items.IRON_HELMET)), new ItemPart(new ItemStack(Items.IRON_CHESTPLATE)), new ItemPart(new ItemStack(Items.IRON_LEGGINGS)), new ItemPart(new ItemStack(Items.IRON_BOOTS)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":pssst", -5, new MessageRewardType(new MessagePart("Pssssst.... Over here!")), new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("Creeper")))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explorer", 30, new MessageRewardType(new MessagePart("Lets go on a journey!")), new ItemRewardType(new ItemPart(new ItemStack(Items.COMPASS)), new ItemPart(new ItemStack(Items.CLOCK)), new ItemPart(new ItemStack(Blocks.TORCH, 64)), new ItemPart(new ItemStack(Items.IRON_PICKAXE)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Mitas", 50, new ItemRewardType(new ItemPart(new ItemStack(Items.GOLD_NUGGET, 32)), new ItemPart(new ItemStack(Items.GOLD_INGOT, 8)), new ItemPart(new ItemStack(Items.GOLDEN_CARROT, 16)), new ItemPart(new ItemStack(Items.GOLDEN_HELMET)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Horde", -25, new MessageRewardType(new MessagePart("Release the horde!")), new EntityRewardType(RewardsUtil.spawnXEntities(EntityRewardType.getBasicNBTForEntity("Zombie"), 15))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Lava_Ring", -40, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rain", -5, new CommandRewardType(new CommandPart("/weather thunder 20000"))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":House", 75, new SchematicRewardType("house.schematic", 3, true, false)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Silverfish_Surround", -20, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fish_Dog", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.FISH, 5)), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("wolf"))))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bone_Cat", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.BONE, 5)), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("ocelot"))))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":XP_Crystal", -60, new CommandRewardType(new CommandPart("/summon xp_orb ~ ~1 ~ {Value:1,Passengers:[{id:\"ender_crystal\"}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Cat", -25, new CommandRewardType(new CommandPart("/summon ocelot ~ ~1 ~ {CatType:0,Sitting:0,Passengers:[{id:\"tnt\",Fuse:80}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":SlimeMan", 10, new CommandRewardType(new CommandPart("/summon slime ~ ~1 ~ {Size:3,Glowing:1b,Passengers:[{id:\"Slime\",Size:2,Glowing:1b,Passengers:[{id:\"Slime\",CustomName:\"Slime Man\",CustomNameVisible:1,Size:1,Glowing:1b}]}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sail_Away", 5, new BlockRewardType(new OffsetBlock(0, -1, 0, Blocks.WATER, false)), new CommandRewardType(new CommandPart("/summon Boat %x %y %z")), new MessageRewardType(new MessagePart("Come sail away!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Witch", -15, new CommandRewardType(new CommandPart("/summon witch %x %y %z "))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Cluckington", 40, new CommandRewardType(new CommandPart("/summon Chicken ~ ~1 ~ {CustomName:\"Cluckington\",CustomNameVisible:1,ActiveEffects:[{Id:1,Amplifier:3,Duration:199980}],Passengers:[{id:\"Zombie\",CustomName:\"wyld\",CustomNameVisible:1,IsVillager:0,IsBaby:1,ArmorItems:[{},{},{},{id:\"minecraft:skull\",Damage:3,tag:{SkullOwner:\"wyld\"},Count:1}]}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Jerry", 40, new CommandRewardType(new CommandPart("/summon slime %x %y %z {Size:1,CustomName:\"Jerry\",CustomNameVisible:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Glenn", 40, new CommandRewardType(new CommandPart("/summon zombie %x %y %z {CustomName:\"Glenn\",CustomNameVisible:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Dr_Trayaurus", 40, new CommandRewardType(new CommandPart("/summon villager %x %y %z {CustomName:\"Dr Trayaurus\",CustomNameVisible:1,Profession:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Pickles", 40, new CommandRewardType(new CommandPart("/summon mooshroom ~ ~1 ~ {Age:-10000,CustomName:\"Pickles\"}")), new MessageRewardType(new MessagePart("Why is his name pickles? The world may never know"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Want_To_Build_A_Snowman", 45, new MessageRewardType(new MessagePart("Do you want to build a snowman?")), new ItemRewardType(new ItemPart(new ItemStack(Blocks.SNOW, 2)), new ItemPart(new ItemStack(Blocks.PUMPKIN)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Diamond_Block", 85, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.DIAMOND_BLOCK, true, 200))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Diamond", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.DIAMOND_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Anti_Slab", -15, new BlockRewardType(RewardsUtil.fillArea(3, 1, 3, Blocks.OBSIDIAN, -1, 2, -1, false, 0, false, true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Chance_Cube_Cube", -10, new MessageRewardType(new MessagePart("Hey, at least it isn't a Giant Chance Cube >:)")), new BlockRewardType(new OffsetBlock(-1, 0, -1, CCubesBlocks.CHANCE_CUBE, false), new OffsetBlock(-1, 0, -2, CCubesBlocks.CHANCE_CUBE, false), new OffsetBlock(-2, 0, -1, CCubesBlocks.CHANCE_CUBE, false), new OffsetBlock(-2, 0, -2, CCubesBlocks.CHANCE_CUBE, false), new OffsetBlock(-1, 1, -1, CCubesBlocks.CHANCE_CUBE, false), new OffsetBlock(-1, 1, -2, CCubesBlocks.CHANCE_CUBE, false), new OffsetBlock(-2, 1, -1, CCubesBlocks.CHANCE_CUBE, false), new OffsetBlock(-2, 1, -2, CCubesBlocks.CHANCE_CUBE, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fake_TNT", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_GENERIC_EXPLODE).setDelay(120).setAtPlayersLocation(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Ghasts", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_GHAST_SCREAM).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":No", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.CHANCE_CUBE, false)), new MessageRewardType(new MessagePart("No"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Creeper", -30, new CommandRewardType(new CommandPart("/summon Creeper %x %y %z {ActiveEffects:[{Id:14,Amplifier:0,Duration:200,ShowParticles:0b}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Knockback_Zombie", -35, new CommandRewardType(new CommandPart("/summon Zombie ~ ~1 ~ {CustomName:\"Leonidas\",CustomNameVisible:1,IsVillager:0,IsBaby:1,HandItems:[{id:stick,Count:1,tag:{AttributeModifiers:[{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:100,Operation:0,UUIDLeast:724513,UUIDMost:715230}],ench:[{id:19,lvl:100}],display:{Name:\"The Spartan Kick\"}}},{}],HandDropChances:[0.0F,0.085F],ActiveEffects:[{Id:1,Amplifier:5,Duration:199980,ShowParticles:0b},{Id:8,Amplifier:2,Duration:199980}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Actual_Invisible_Ghast", -80, new CommandRewardType(new CommandPart("/summon Ghast ~ ~10 ~ {ActiveEffects:[{Id:14,Amplifier:0,Duration:2000,ShowParticles:0b}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Giant_Chance_Cube", -45, new BlockRewardType(RewardsUtil.fillArea(3, 3, 3, CCubesBlocks.CHANCE_CUBE, -1, 0, -1, false, 0, true, false))), false);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fireworks", 0, new CommandRewardType(RewardsUtil.executeXCommands("/summon fireworks_rocket ~ ~1 ~ {FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Colors:[16711680],FadeColors:[16711680]}]}}}}", 4))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":STRING!", 7, new BlockRewardType(RewardsUtil.fillArea(11, 5, 11, Blocks.TRIPWIRE, -5, 0, -5, false, 0, false, true)), new MessageRewardType(new MessagePart("STRING!!!!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Bats", -50, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~1 ~ {Passengers:[{id:\"tnt\",Fuse:80}]}", 10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Jelly_Fish", -40, new CommandRewardType(RewardsUtil.executeXCommands("/summon bat ~ ~1 ~ {Passengers:[{id:\"magma_cube\",CustomName:\"Nether Jelly Fish\",CustomNameVisible:1,Size:3}]}", 10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Pig_Of_Destiny", 15, new CommandRewardType(new CommandPart("/summon Pig ~ ~1 ~ {CustomName:\"The Pig of Destiny\",CustomNameVisible:1,ArmorItems:[{},{},{id:diamond_chestplate,Count:1,tag:{ench:[{id:7,lvl:1000}]}},{}],ArmorDropChances:[0.085F,0.085F,0.0F,0.085F]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Squid_Horde", 5, new MessageRewardType(new MessagePart("Release the horde!").setRange(32), new MessagePart("Of squids!!").setDelay(20).setRange(32)), new EntityRewardType(RewardsUtil.spawnXEntities(EntityRewardType.getBasicNBTForEntity("Squid"), 15)), new BlockRewardType(RewardsUtil.fillArea(3, 2, 3, Blocks.WATER, -1, 0, -1, false, 5, true, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":D-rude_SandStorm", -10, new BlockRewardType(RewardsUtil.fillArea(5, 3, 5, Blocks.SAND, -2, 0, -2, true, 0, false, true)), new MessageRewardType(new MessagePart("Well that was D-rude").setDelay(40))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":DIY_Pie", 5, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.PUMPKIN, false), new OffsetBlock(1, 1, 0, Blocks.REEDS, false)), new CommandRewardType(new CommandPart("/summon Chicken ~ ~1 ~ {CustomName:\"Zeeth_Kyrah\",CustomNameVisible:1}")), new MessageRewardType(new MessagePart("Do it yourself Pumpkin Pie!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Watch_World_Burn", -5, new BlockRewardType(RewardsUtil.fillArea(7, 1, 7, Blocks.FIRE, -3, 0, -3, false, 0, true, true)), new MessageRewardType(new MessagePart("Some people just want to watch the world burn."))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Coal_To_Diamonds", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.COAL_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5)), new ItemRewardType(new ItemPart(new ItemStack(Items.DIAMOND, 5), 50))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Glitch", 0, new CommandRewardType(new CommandPart("/summon Item ~ ~ ~ {Item:{id:dirt,Damage:1,Count:1,tag:{display:{Name:\"Glitch\",Lore:[Doesn't actually do anything...]}}}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":SpongeBob_SquarePants", 15, new CommandRewardType(new CommandPart("/summon Item ~ ~ ~ {Item:{id:sponge,Count:1,tag:{display:{Name:\"SpongeBob\"}}}}"), new CommandPart("/summon Item ~ ~ ~ {Item:{id:leather_leggings,Count:1,tag:{display:{Name:\"SquarePants\"}}}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Hot_Tub", -15, new BlockRewardType(RewardsUtil.addBlocksLists(RewardsUtil.fillArea(7, 1, 7, Blocks.WATER, -3, -1, -3, false, 0, true, true), RewardsUtil.fillArea(7, 1, 7, Blocks.AIR, -3, -1, -3, false, 98, true, true), RewardsUtil.fillArea(7, 1, 7, Blocks.LAVA, -3, -1, -3, false, 100, true, true))), new MessageRewardType(new MessagePart("No no no. I wanted a hot tub!").setDelay(40))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Quidditch", 0, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~ ~ {Passengers:[{id:\"Witch\"}]}", 7)), new MessageRewardType(new MessagePart("Quidditch anyone?").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":One_Man_Army", -10, new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("zombie_pigman"))), new CommandRewardType(RewardsUtil.executeXCommands("/summon zombie_pigman ~ ~ ~ {Silent:1,ActiveEffects:[{Id:14,Amplifier:0,Duration:19980,ShowParticles:1b}]}", 9)), new MessageRewardType(new MessagePart("One man army").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cuteness", 10, new CommandRewardType(RewardsUtil.executeXCommands("/summon rabbit ~ ~1 ~ {CustomName:\"Fluffy\",CustomNameVisible:1,RabbitType:5}", 25)), new MessageRewardType(new MessagePart("Cuteness overload!").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Silvermite_Stacks", -15, new CommandRewardType(RewardsUtil.executeXCommands("/summon Silverfish ~ ~1 ~ {Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\"}]}]}]}]}]}]}]}]}]}]}", 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Take_This", 55, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.BRICK_BLOCK, false), new OffsetBlock(0, 1, 0, Blocks.BRICK_BLOCK, false), new OffsetBlock(0, 2, 0, Blocks.BRICK_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.AIR, false), new OffsetBlock(0, 1, 1, Blocks.AIR, false), new OffsetBlock(0, 2, 1, Blocks.AIR, false)), new CommandRewardType(new CommandPart("/summon item_frame ~ ~ ~1 {Item:{id:stick,Count:1b},Facing:0,ItemRotation:7}", 2), new CommandPart("/summon item_frame ~ ~1 ~1 {Item:{id:diamond,Count:1b},Facing:0,ItemRotation:0}", 2), new CommandPart("/summon item_frame ~ ~2 ~1 {Item:{id:diamond,Count:1b},Facing:0,ItemRotation:0}", 2)), new MessageRewardType(new MessagePart("It's dangerous to go alone, here take this!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invizible_Silverfish", -45, new CommandRewardType(RewardsUtil.executeXCommands("/summon Silverfish ~ ~1 ~ {Glowing:1b,ActiveEffects:[{Id:1,Amplifier:1,Duration:200000},{Id:14,Amplifier:0,Duration:20000}],Passengers:[{id:\"Silverfish\",ActiveEffects:[{Id:14,Amplifier:0,Duration:20000}]}]}", 5))));

		ItemStack stack;
		NBTTagCompound nbt = new NBTTagCompound();

		stack = new ItemStack(Items.STICK);
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("sharpness"), 5);
		stack.setStackDisplayName("A Big Stick");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Roosevelt's_Stick", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.FISHING_ROD);
		stack.setItemDamage(stack.getMaxDamage() / 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Half_Fishingrod", 5, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.GOLDEN_APPLE, 1, 1);
		stack.setStackDisplayName("Notch");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Notch", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.NETHER_STAR);
		stack.setStackDisplayName("North Star");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Star", 100, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("sharpness"), 10);
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("unbreaking"), 10);
		stack.setItemDamage(stack.getMaxDamage() - 2);
		stack.setStackDisplayName("The Divine Sword");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Divine", 85, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.WOODEN_PICKAXE);
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("efficiency"), 10);
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("fortune"), 3);
		stack.setStackDisplayName("Giga Breaker");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Giga_Breaker", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.BOW);
		stack.setItemDamage(stack.getMaxDamage());
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("power"), 5);
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("punch"), 3);
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("flame"), 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":One_Shot", 75, new ItemRewardType(new ItemPart(stack), new ItemPart(new ItemStack(Items.ARROW, 1)))));

		stack = new ItemStack(Items.FISH, 1, 2);
		stack.setStackDisplayName("Nemo");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Nemo", 10, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.FISH, 1, 2);
		stack.setStackDisplayName("Marlin");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Marlin", 10, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.FIRE_CHARGE, 1);
		stack.addEnchantment(Enchantment.getEnchantmentByLocation("fire_aspect"), 2);
		stack.setStackDisplayName("Why not?");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fire_Aspect_Fire", 60, new ItemRewardType(new ItemPart(stack))));

		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = new TextComponentString("The broken path");
		sign.signText[1] = new TextComponentString("to succeed");
		nbt = new NBTTagCompound();
		((TileEntity) sign).writeToNBT(nbt);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Path_To_Succeed", 0, new BlockRewardType(new OffsetTileEntity(0, 0, -5, Blocks.STANDING_SIGN, nbt, true, 20), new OffsetBlock(0, -1, 0, Blocks.COBBLESTONE, true, 0), new OffsetBlock(0, -1, -1, Blocks.COBBLESTONE, true, 4), new OffsetBlock(0, -1, -2, Blocks.COBBLESTONE, true, 8), new OffsetBlock(0, -1, -3, Blocks.COBBLESTONE, true, 12), new OffsetBlock(0, -1, -4, Blocks.COBBLESTONE, true, 16), new OffsetBlock(0, -1, -5, Blocks.COBBLESTONE, true, 20))));

		OffsetTileEntity[] signs = new OffsetTileEntity[4];
		OffsetTileEntity temp;
		for(int i = 0; i < signs.length; i++)
		{
			sign = new TileEntitySign();
			sign.signText[0] = new TextComponentString("Help Me!");
			nbt = new NBTTagCompound();
			((TileEntity) sign).writeToNBT(nbt);
			temp = new OffsetTileEntity(i == 2 ? -2 : i == 3 ? 2 : 0, 1, i == 0 ? -2 : i == 1 ? 2 : 0, Blocks.WALL_SIGN, nbt, false, 5);
			temp.setBlockState(Blocks.WALL_SIGN.getDefaultState().withProperty(BlockWallSign.FACING, EnumFacing.getFront(i + 2)));
			signs[i] = temp;
		}

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Help_Me", 0, new BlockRewardType(RewardsUtil.addBlocksLists(RewardsUtil.fillArea(3, 1, 3, Blocks.STONEBRICK, -1, -1, -1, false, 0, true, false), RewardsUtil.fillArea(3, 3, 3, Blocks.IRON_BARS, -1, 0, -1, false, 0, true, false), RewardsUtil.fillArea(1, 3, 1, Blocks.AIR, 0, 0, 0, false, 1, true, false), signs)), new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("Villager")).setRemovedBlocks(false).setDelay(5)), new CommandRewardType(new CommandPart("/summon tnt %x %y %z {Fuse:80}", 5))));

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
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Beacon_Build", 100, new BlockRewardType(blocks)));

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":No_Exp", -40)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
			{
				player.experienceLevel = 0;
				player.experienceTotal = 0;
				player.experience = 0;
				player.addChatMessage(new TextComponentString("Rip EXP"));
			}
		});

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Smite", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
			{
				world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ, false));
				player.addChatMessage(new TextComponentString("Thou has been smitten!"));
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
		INSTANCE.registerReward(new WitherReward());
		INSTANCE.registerReward(new TrollTNTReward());
		INSTANCE.registerReward(new EnderCrystalTimerReward());
		INSTANCE.registerReward(new WaitForItReward());
		INSTANCE.registerReward(new ChargedCreeperReward());
		INSTANCE.registerReward(new ClearInventoryReward(), false);
		// INSTANCE.registerReward(new ZombieCopyCatReward());
		// INSTANCE.registerReward(new InventoryChestReward());
		INSTANCE.registerReward(new ItemOfDestinyReward());
		INSTANCE.registerReward(new ThrownInAirReward());
		INSTANCE.registerReward(new DiscoReward());
		INSTANCE.registerReward(new InventoryBombReward());
		INSTANCE.registerReward(new JukeBoxReward());
		INSTANCE.registerReward(new BookOfMemesReward());
		INSTANCE.registerReward(new TableFlipReward());
		INSTANCE.registerReward(new TorchesToCreepers());
		INSTANCE.registerReward(new MazeReward());
		INSTANCE.registerReward(new RottenFoodReward());
		INSTANCE.registerReward(new OneIsLuckyReward());
		INSTANCE.registerReward(new SkyblockReward());
		INSTANCE.registerReward(new CakeIsALieReward());
		INSTANCE.registerReward(new ItemRenamer());
		INSTANCE.registerReward(new DoubleRainbow());
		INSTANCE.registerReward(new WolvesToCreepersReward());
		INSTANCE.registerReward(new DidYouKnowReward());
		INSTANCE.registerReward(new ArmorStandArmorReward());
		INSTANCE.registerReward(new RainingCatsAndCogsReward());

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Half_Heart", -30)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
			{
				player.setHealth(1f);
			}
		});

		MathReward math = new MathReward();
		MinecraftForge.EVENT_BUS.register(math);
		INSTANCE.registerReward(math);

		QuestionsReward question = new QuestionsReward();
		MinecraftForge.EVENT_BUS.register(question);
		INSTANCE.registerReward(question);
	}

	public static void loadCustomUserRewards(MinecraftServer server)
	{
		ArrayList<EntityPlayerMP> allp = new ArrayList<EntityPlayerMP>();
		Iterator<?> iterator;

		for(int i = 0; i < server.worldServers.length; i++)
		{
			iterator = server.worldServers[i].playerEntities.listIterator();
			while(iterator.hasNext())
				allp.add((EntityPlayerMP) iterator.next());
		}

		for(EntityPlayerMP player : allp)
			new CustomUserReward(player.getName(), player.getUniqueID());
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

	public void addCustomReward(IChanceCubeReward reward)
	{
		this.customRewards.add(reward);
	}

	@Override
	public IChanceCubeReward getRewardByName(String name)
	{
		return nameToReward.get(name);
	}

	@Override
	public void triggerRandomReward(World world, BlockPos pos, EntityPlayer player, int chance)
	{
		if(CCubesSettings.testRewards)
		{
			IChanceCubeReward pickedReward = this.sortedRewards.get(CCubesSettings.testingRewardIndex);
			pickedReward.trigger(world, pos, player);
			CCubesSettings.testingRewardIndex++;
			if(CCubesSettings.testingRewardIndex >= this.sortedRewards.size())
				CCubesSettings.testingRewardIndex = 0;
			CCubesCore.logger.log(Level.INFO, "Testing the reward with the name of: " + pickedReward.getName());
			return;
		}
		else if(CCubesSettings.testCustomRewards)
		{
			IChanceCubeReward pickedReward = this.customRewards.get(CCubesSettings.testingRewardIndex);
			pickedReward.trigger(world, pos, player);
			CCubesSettings.testingRewardIndex++;
			if(CCubesSettings.testingRewardIndex >= this.customRewards.size())
				CCubesSettings.testingRewardIndex = 0;
			CCubesCore.logger.log(Level.INFO, "Testing the reward with the name of: " + pickedReward.getName());
			return;
		}

		if(this.sortedRewards.size() == 0)
		{
			CCubesCore.logger.log(Level.WARN, "There are no registered rewards with ChanceCubes and no reward was able to be given");
			return;
		}

		if(CCubesSettings.doesHolidayRewardTrigger && CCubesSettings.holidayReward != null)
		{
			CCubesSettings.holidayReward.trigger(world, pos, player);
			CCubesCore.logger.log(Level.INFO, "The " + CCubesSettings.holidayReward.getName() + " holiday reward has been triggered!!!!");
			CCubesSettings.doesHolidayRewardTrigger = false;
			CCubesSettings.holidayRewardTriggered = true;
			ConfigLoader.config.get(ConfigLoader.genCat, "HolidayRewardTriggered", false, "Don't touch! Well I mean you can touch it, if you want. I can't stop you. I'm only text.").setValue(true);
			ConfigLoader.config.save();
			return;
		}

		if(player != null)
		{
			for(int i = 0; i < player.inventory.mainInventory.size(); i++)
			{
				ItemStack stack = player.inventory.mainInventory.get(i);
				if(stack != null && stack.getItem() instanceof ItemChancePendant)
				{
					ItemChancePendant pendant = (ItemChancePendant) stack.getItem();
					pendant.damage(stack);
					if(stack.getItemDamage() >= CCubesSettings.pendantUses)
						player.inventory.setInventorySlotContents(i, null);
					chance += pendant.getChanceIncrease();
					if(chance > 100)
						chance = 100;
					break;
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
		pickedReward.trigger(world, pos, player);
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

	public int getNumberOfLoadedRewards()
	{
		return this.sortedRewards.size();
	}

	public int getNumberOfDisabledRewards()
	{
		return this.disabledNameToReward.size();
	}

	public void ClearRewards()
	{
		this.sortedRewards.clear();
		this.nameToReward.clear();
		this.disabledNameToReward.clear();
	}
}