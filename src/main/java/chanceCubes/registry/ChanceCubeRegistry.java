package chanceCubes.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
import chanceCubes.rewards.defaultRewards.ChanceCubeRenameReward;
import chanceCubes.rewards.defaultRewards.ChargedCreeperReward;
import chanceCubes.rewards.defaultRewards.ClearInventoryReward;
import chanceCubes.rewards.defaultRewards.CoinFlipReward;
import chanceCubes.rewards.defaultRewards.CountDownReward;
import chanceCubes.rewards.defaultRewards.CreeperSurroundedReward;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import chanceCubes.rewards.defaultRewards.DidYouKnowReward;
import chanceCubes.rewards.defaultRewards.DigBuildReward;
import chanceCubes.rewards.defaultRewards.DiscoReward;
import chanceCubes.rewards.defaultRewards.DoubleRainbow;
import chanceCubes.rewards.defaultRewards.EnderCrystalTimerReward;
import chanceCubes.rewards.defaultRewards.FiveProngReward;
import chanceCubes.rewards.defaultRewards.HerobrineReward;
import chanceCubes.rewards.defaultRewards.InventoryBombReward;
import chanceCubes.rewards.defaultRewards.ItemChestReward;
import chanceCubes.rewards.defaultRewards.ItemOfDestinyReward;
import chanceCubes.rewards.defaultRewards.ItemRenamer;
import chanceCubes.rewards.defaultRewards.JukeBoxReward;
import chanceCubes.rewards.defaultRewards.MagicFeetReward;
import chanceCubes.rewards.defaultRewards.MatchingReward;
import chanceCubes.rewards.defaultRewards.MathReward;
import chanceCubes.rewards.defaultRewards.MazeReward;
import chanceCubes.rewards.defaultRewards.MobEffectsReward;
import chanceCubes.rewards.defaultRewards.MobTowerReward;
import chanceCubes.rewards.defaultRewards.MontyHallReward;
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
import chanceCubes.rewards.defaultRewards.TicTacToeReward;
import chanceCubes.rewards.defaultRewards.TorchesToCreepers;
import chanceCubes.rewards.defaultRewards.TravellerReward;
import chanceCubes.rewards.defaultRewards.TrollHoleReward;
import chanceCubes.rewards.defaultRewards.TrollTNTReward;
import chanceCubes.rewards.defaultRewards.WaitForItReward;
import chanceCubes.rewards.defaultRewards.WitherReward;
import chanceCubes.rewards.defaultRewards.WolvesToCreepersReward;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EffectPart;
import chanceCubes.rewards.rewardparts.EntityPart;
import chanceCubes.rewards.rewardparts.ExpirencePart;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.rewardparts.OffsetTileEntity;
import chanceCubes.rewards.rewardparts.ParticlePart;
import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EffectRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.ParticleEffectRewardType;
import chanceCubes.rewards.type.SchematicRewardType;
import chanceCubes.rewards.type.SoundRewardType;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import chanceCubes.util.RewardData;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.SchematicUtil;
import chanceCubes.util.Task;
import net.minecraft.block.BlockWallSign;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

public class ChanceCubeRegistry implements IRewardRegistry
{
	public static ChanceCubeRegistry INSTANCE = new ChanceCubeRegistry();
	private static Random random = new Random();

	private List<IChanceCubeReward> customRewards = Lists.newArrayList();
	private Map<String, IChanceCubeReward> nameToReward = Maps.newHashMap();
	private List<IChanceCubeReward> sortedRewards = Lists.newArrayList();
	private Map<String, IChanceCubeReward> disabledNameToReward = Maps.newHashMap();

	private static IChanceCubeReward lastReward = null;
	private static List<IChanceCubeReward> cooldownList = new ArrayList<>();

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
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Redstone_Diamond", 10, new ItemRewardType(RewardsUtil.generateItemParts(Items.REDSTONE, Items.DIAMOND))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sethbling_Reward", 30, new MessageRewardType(new MessagePart("Welcome back, SethBling here :)")), new ItemRewardType(RewardsUtil.generateItemParts(new ItemStack(Items.REDSTONE, 32), new ItemStack(Blocks.REPEATER, 3), new ItemStack(Blocks.COMPARATOR, 3), new ItemStack(Blocks.REDSTONE_LAMP, 3), new ItemStack(Blocks.REDSTONE_TORCH, 3)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP", 35, new ExperienceRewardType(new ExpirencePart(100).setNumberofOrbs(10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP_Shower", 35, new ExperienceRewardType(new ExpirencePart(10), new ExpirencePart(10, 10), new ExpirencePart(10, 10), new ExpirencePart(10, 20), new ExpirencePart(10, 30), new ExpirencePart(10, 40), new ExpirencePart(10, 50), new ExpirencePart(10, 60), new ExpirencePart(10, 70), new ExpirencePart(10, 80), new ExpirencePart(10, 90), new ExpirencePart(10, 100), new ExpirencePart(10, 110), new ExpirencePart(10, 120), new ExpirencePart(10, 130), new ExpirencePart(10, 140), new ExpirencePart(10, 150))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Poison", -25, new EffectRewardType(new EffectPart(MobEffects.POISON, 25, 1).setRadius(30))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Wither_Status_Effect", -25, new EffectRewardType(new EffectPart(new StringVar(String.valueOf(Potion.getIdFromPotion(MobEffects.WITHER))), new IntVar(new Integer[] { 3, 5, 6, 8, 10 }), new IntVar(new Integer[] { 1, 2 })).setRadius(30))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":ChatMessage", 0, new MessageRewardType(new MessagePart("You have escaped the wrath of the Chance Cubes........."), new MessagePart("For now......"))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Command", 15, new CommandRewardType(" /give %player minecraft:painting 1 0 {display:{Name:\"Wylds Bestest friend\",Lore:[\"You know you love me, \"]}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Hearts", 0, new ParticleEffectRewardType(RewardsUtil.spawnXParticles(34, 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explosion", 0, new ParticleEffectRewardType(new ParticlePart(2)), new SoundRewardType(new SoundPart(SoundEvents.ENTITY_GENERIC_EXPLODE))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Wool", 25, new ItemRewardType(RewardsUtil.generateItemParts(new ItemStack(Blocks.WHITE_WOOL, 4), new ItemStack(Blocks.ORANGE_WOOL, 4), new ItemStack(Blocks.MAGENTA_WOOL, 4), new ItemStack(Blocks.LIGHT_BLUE_WOOL, 4), new ItemStack(Blocks.YELLOW_WOOL, 4), new ItemStack(Blocks.LIME_WOOL, 4), new ItemStack(Blocks.PINK_WOOL, 4), new ItemStack(Blocks.GRAY_WOOL, 4), new ItemStack(Blocks.LIGHT_GRAY_WOOL, 4), new ItemStack(Blocks.CYAN_WOOL, 4), new ItemStack(Blocks.PURPLE_WOOL, 4), new ItemStack(Blocks.BLUE_WOOL, 4), new ItemStack(Blocks.BROWN_WOOL, 4), new ItemStack(Blocks.GREEN_WOOL, 4), new ItemStack(Blocks.RED_WOOL, 4), new ItemStack(Blocks.BLACK_WOOL, 4)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Enchanting", 80, new ItemRewardType(new ItemPart(new ItemStack(Blocks.ENCHANTING_TABLE)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bookshelves", 60, new ItemRewardType(new ItemPart(new ItemStack(Blocks.BOOKSHELF, 8)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Ores_Galore", 50, new ItemRewardType(RewardsUtil.generateItemParts(Items.COAL, Items.REDSTONE, Items.IRON_INGOT, Items.GOLD_INGOT, Items.DIAMOND, Items.EMERALD))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Have_Another", -10, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.CHANCE_CUBE, 3))), new MessageRewardType(new MessagePart("I hear you like Chance Cubes."), new MessagePart("So I put some Chance Cubes in your Chance Cubes!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Icsahedron", 0, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Saplings", 35, new MessageRewardType(new MessagePart("Seems you have purchased the saplings DLC")), new ItemRewardType(RewardsUtil.generateItemParts(new ItemStack(Blocks.ACACIA_SAPLING, 4), new ItemStack(Blocks.BIRCH_SAPLING, 4), new ItemStack(Blocks.DARK_OAK_SAPLING, 4), new ItemStack(Blocks.JUNGLE_SAPLING, 4), new ItemStack(Blocks.OAK_SAPLING, 4), new ItemStack(Blocks.SPRUCE_SAPLING, 4)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Farmer", 35, new MessageRewardType(new MessagePart("Time to farm!")), new ItemRewardType(new ItemPart(new ItemStack(Items.IRON_HOE)), new ItemPart(new ItemStack(Items.BUCKET)), new ItemPart(new ItemStack(Items.WHEAT_SEEDS, 16)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rancher", 60, new ItemRewardType(new ItemPart(new ItemStack(Blocks.OAK_FENCE, 32)), new ItemPart(new ItemStack(Items.PIG_SPAWN_EGG)), new ItemPart(new ItemStack(Items.COW_SPAWN_EGG)), new ItemPart(new ItemStack(Items.SHEEP_SPAWN_EGG)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fighter", 25, new MessageRewardType(new MessagePart("SPARTAAA!!!")), new ItemRewardType(RewardsUtil.generateItemParts(Items.IRON_SWORD, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":pssst", -5, new MessageRewardType(new MessagePart("Pssssst.... Over here!")), new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("Creeper")))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explorer", 30, new MessageRewardType(new MessagePart("Lets go on a journey!")), new ItemRewardType(RewardsUtil.generateItemParts(new ItemStack(Items.COMPASS), new ItemStack(Items.CLOCK), new ItemStack(Blocks.TORCH, 64), new ItemStack(Items.IRON_PICKAXE)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Mitas", 50, new ItemRewardType(RewardsUtil.generateItemParts(new ItemStack(Items.GOLD_NUGGET, 32), new ItemStack(Items.GOLD_INGOT, 8), new ItemStack(Items.GOLDEN_CARROT, 16), new ItemStack(Items.GOLDEN_HELMET)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Horde", -25, new MessageRewardType(new MessagePart("Release the horde!")), new EntityRewardType(RewardsUtil.spawnXEntities(EntityRewardType.getBasicNBTForEntity("Zombie"), 15))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Lava_Ring", -40, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rain", -5, new CommandRewardType(new CommandPart("/weather thunder 20000"))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":House", 75, new SchematicRewardType("house.schematic", 3, true, false)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Silverfish_Surround", -20, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 0, Blocks.INFESTED_STONE, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fish_Dog", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.COD, 5)), new ItemPart(new ItemStack(Items.WOLF_SPAWN_EGG)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bone_Cat", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.BONE, 5)), new ItemPart(new ItemStack(Items.OCELOT_SPAWN_EGG)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":XP_Crystal", -60, new CommandRewardType(new CommandPart("/summon xp_orb ~ ~1 ~ {Value:1,Passengers:[{id:\"ender_crystal\"}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Cat", -25, new CommandRewardType(new CommandPart("/summon ocelot ~ ~1 ~ {CatType:0,Sitting:0,Passengers:[{id:\"tnt\",Fuse:80}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":SlimeMan", 10, new CommandRewardType(new CommandPart("/summon slime ~ ~1 ~ {Size:3,Glowing:1b,Passengers:[{id:\"Slime\",Size:2,Glowing:1b,Passengers:[{id:\"Slime\",CustomName:\"Slime Man\",CustomNameVisible:1,Size:1,Glowing:1b}]}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sail_Away", 5, new BlockRewardType(new OffsetBlock(0, -1, 0, Blocks.WATER, false)), new CommandRewardType(new CommandPart("/summon Boat %x %y %z")), new MessageRewardType(new MessagePart("Come sail away!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Witch", -15, new CommandRewardType(new CommandPart("/summon witch %x %y %z "))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Cluckington", 25, new CommandRewardType(new CommandPart("/summon Chicken ~ ~1 ~ {CustomName:\"Cluckington\",CustomNameVisible:1,ActiveEffects:[{Id:1,Amplifier:3,Duration:199980}],Passengers:[{id:\"Zombie\",CustomName:\"wyld\",CustomNameVisible:1,IsVillager:0,IsBaby:1,ArmorItems:[{},{},{},{id:\"minecraft:skull\",Damage:3,tag:{SkullOwner:\"wyld\"},Count:1}]}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Jerry", 25, new CommandRewardType(new CommandPart("/summon slime %x %y %z {Size:1,CustomName:\"Jerry\",CustomNameVisible:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Glenn", 25, new CommandRewardType(new CommandPart("/summon zombie %x %y %z {CustomName:\"Glenn\",CustomNameVisible:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Dr_Trayaurus", 25, new CommandRewardType(new CommandPart("/summon villager %x %y %z {CustomName:\"Dr Trayaurus\",CustomNameVisible:1,Profession:1}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Pickles", 25, new CommandRewardType(new CommandPart("/summon mooshroom ~ ~1 ~ {Age:-10000,CustomName:\"Pickles\"}")), new MessageRewardType(new MessagePart("Why is his name pickles? The world may never know"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Want_To_Build_A_Snowman", 45, new MessageRewardType(new MessagePart("Do you want to build a snowman?")), new ItemRewardType(new ItemPart(new ItemStack(Blocks.SNOW, 2)), new ItemPart(new ItemStack(Blocks.PUMPKIN)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Diamond_Block", 85, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.DIAMOND_BLOCK, true, 200))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Diamond", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.DIAMOND_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Anti_Slab", -15, new BlockRewardType(RewardsUtil.fillArea(3, 1, 3, Blocks.OBSIDIAN, -1, 2, -1, false, 0, false, true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Chance_Cube_Cube", -10, new MessageRewardType(new MessagePart("Hey, at least it isn't a Giant Chance Cube >:)")), new BlockRewardType((RewardsUtil.fillArea(2, 2, 2, CCubesBlocks.CHANCE_CUBE, -2, 0, -2, false, 1, false, false)))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fake_TNT", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_GENERIC_EXPLODE, 120).setAtPlayersLocation(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Ghasts", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_GHAST_SCREAM).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":No", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.CHANCE_CUBE, false)), new MessageRewardType(new MessagePart("No"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Creeper", -30, new CommandRewardType(new CommandPart("/summon Creeper %x %y %z {ActiveEffects:[{Id:14,Amplifier:0,Duration:200,ShowParticles:0b}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Knockback_Zombie", -35, new CommandRewardType(new CommandPart("/summon Zombie ~ ~1 ~ {CustomName:\"Leonidas\",CustomNameVisible:1,IsVillager:0,IsBaby:1,HandItems:[{id:stick,Count:1,tag:{AttributeModifiers:[{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:100,Operation:0,UUIDLeast:724513,UUIDMost:715230}],ench:[{id:19,lvl:100}],display:{Name:\"The Spartan Kick\"}}},{}],HandDropChances:[0.0F,0.085F],ActiveEffects:[{Id:1,Amplifier:5,Duration:199980,ShowParticles:0b},{Id:8,Amplifier:2,Duration:199980}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Actual_Invisible_Ghast", -80, new CommandRewardType(new CommandPart("/summon Ghast ~ ~10 ~ {ActiveEffects:[{Id:14,Amplifier:0,Duration:2000,ShowParticles:0b}]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Giant_Chance_Cube", -45, new BlockRewardType(RewardsUtil.fillArea(3, 3, 3, CCubesBlocks.CHANCE_CUBE, -1, 0, -1, false, 0, true, false))), false);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fireworks", 5, new CommandRewardType(RewardsUtil.executeXCommands("/summon fireworks_rocket ~ ~1 ~ {LifeTime:15,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Colors:[I;16711680],FadeColors:[I;16711680]}]}}}}", 4))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":STRING!", 7, new BlockRewardType(RewardsUtil.fillArea(11, 5, 11, Blocks.TRIPWIRE, -5, 0, -5, false, 0, false, true)), new MessageRewardType(new MessagePart("STRING!!!!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":CARPET!", 7, new BlockRewardType(RewardsUtil.fillArea(11, 5, 11, Blocks.WHITE_CARPET, -5, 0, -5, false, 0, false, true)), new MessageRewardType(new MessagePart("CARPET!!!!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Bats", -50, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~1 ~ {Passengers:[{id:\"tnt\",Fuse:80}]}", 10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Jelly_Fish", -40, new CommandRewardType(RewardsUtil.executeXCommands("/summon bat ~ ~1 ~ {Passengers:[{id:\"magma_cube\",CustomName:\"Nether Jelly Fish\",CustomNameVisible:1,Size:3}]}", 10))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Pig_Of_Destiny", 15, new CommandRewardType(new CommandPart("/summon Pig ~ ~1 ~ {CustomName:\"The Pig of Destiny\",CustomNameVisible:1,ArmorItems:[{},{},{id:diamond_chestplate,Count:1,tag:{ench:[{id:7,lvl:1000}]}},{}],ArmorDropChances:[0.085F,0.085F,0.0F,0.085F]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Squid_Horde", 5, new MessageRewardType(new MessagePart("Release the horde!").setRange(32), new MessagePart("Of squids!!", 20).setRange(32)), new EntityRewardType(RewardsUtil.spawnXEntities(EntityRewardType.getBasicNBTForEntity("Squid"), 15)), new BlockRewardType(RewardsUtil.fillArea(3, 2, 3, Blocks.WATER, -1, 0, -1, false, 5, true, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":D-rude_SandStorm", -10, new BlockRewardType(RewardsUtil.fillArea(5, 3, 5, Blocks.SAND, -2, 0, -2, true, 0, false, true)), new MessageRewardType(new MessagePart("Well that was D-rude", 40))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Ice_Cold", -10, new BlockRewardType(RewardsUtil.fillArea(5, 3, 5, Blocks.ICE, -2, 0, -2, false, 0, false, true)), new MessageRewardType(new MessagePart("[Shinauko]: You're as cold as ice"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":DIY_Pie", 5, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.PUMPKIN, false), new OffsetBlock(1, 1, 0, Blocks.SUGAR_CANE, false)), new CommandRewardType(new CommandPart("/summon Chicken ~ ~1 ~ {CustomName:\"Zeeth_Kyrah\",CustomNameVisible:1}")), new MessageRewardType(new MessagePart("Do it yourself Pumpkin Pie!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Watch_World_Burn", -5, new BlockRewardType(RewardsUtil.fillArea(7, 1, 7, Blocks.FIRE, -3, 0, -3, false, 0, true, true)), new MessageRewardType(new MessagePart("Some people just want to watch the world burn."))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Coal_To_Diamonds", 10, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.COAL_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5)), new ItemRewardType(new ItemPart(new ItemStack(Items.DIAMOND, 5), 50))));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Glitch", 0, new CommandRewardType(new CommandPart("/summon Item ~ ~ ~ {Item:{id:dirt,Damage:1,Count:1,tag:{display:{Name:\"Glitch\",Lore:[Doesn't actually do anything...]}}}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":SpongeBob_SquarePants", 15, new CommandRewardType(new CommandPart("/summon Item ~ ~ ~ {Item:{id:sponge,Count:1,tag:{display:{Name:\"SpongeBob\"}}}}"), new CommandPart("/summon Item ~ ~ ~ {Item:{id:leather_leggings,Count:1,tag:{display:{Name:\"SquarePants\"}}}}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Hot_Tub", -15, new BlockRewardType(RewardsUtil.addBlocksLists(RewardsUtil.fillArea(7, 1, 7, Blocks.WATER, -3, -1, -3, false, 0, true, true), RewardsUtil.fillArea(7, 1, 7, Blocks.AIR, -3, -1, -3, false, 98, true, true), RewardsUtil.fillArea(7, 1, 7, Blocks.LAVA, -3, -1, -3, false, 100, true, true))), new MessageRewardType(new MessagePart("No no no. I wanted a hot tub!", 40))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Quidditch", -30, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~ ~ {Passengers:[{id:\"Witch\"}]}", 7)), new MessageRewardType(new MessagePart("Quidditch anyone?").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":One_Man_Army", -10, new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("zombie_pigman"))), new CommandRewardType(RewardsUtil.executeXCommands("/summon zombie_pigman ~ ~ ~ {Silent:1,ActiveEffects:[{Id:14,Amplifier:0,Duration:19980,ShowParticles:1b}]}", 9)), new MessageRewardType(new MessagePart("One man army").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cuteness", 10, new CommandRewardType(RewardsUtil.executeXCommands("/summon rabbit ~ ~1 ~ {CustomName:\"Fluffy\",CustomNameVisible:1,RabbitType:5}", 20)), new MessageRewardType(new MessagePart("Cuteness overload!").setRange(32))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Silvermite_Stacks", -35, new CommandRewardType(RewardsUtil.executeXCommands("/summon Silverfish ~ ~1 ~ {Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\"}]}]}]}]}]}]}]}]}]}]}", 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Take_This", 55, new BlockRewardType(RewardsUtil.addBlocksLists(RewardsUtil.fillArea(1, 3, 1, Blocks.BRICKS, 0, 0, 0, false, 1, false, false), RewardsUtil.fillArea(1, 3, 1, Blocks.AIR, 0, 0, 0, false, 0, false, false))), new CommandRewardType(new CommandPart("/summon item_frame ~ ~ ~1 {Item:{id:\"minecraft:stick\", Count:1b},Facing:0,ItemRotation:7}", 2), new CommandPart("/summon item_frame ~ ~1 ~1 {Item:{id:\"minecraft:diamond\", Count:1b},Facing:0,ItemRotation:0}", 2), new CommandPart("/summon item_frame ~ ~2 ~1 {Item:{id:\"minecraft:diamond\", Count:1b},Facing:0,ItemRotation:0}", 2)), new MessageRewardType(new MessagePart("It's dangerous to go alone, here take this!"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invizible_Silverfish", -55, new CommandRewardType(RewardsUtil.executeXCommands("/summon Silverfish ~ ~1 ~ {Glowing:1b,ActiveEffects:[{Id:1,Amplifier:1,Duration:200000},{Id:14,Amplifier:0,Duration:20000}],Passengers:[{id:\"Silverfish\",ActiveEffects:[{Id:14,Amplifier:0,Duration:20000}]}]}", 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Arrow_Trap", -25, new SchematicRewardType(SchematicUtil.loadCustomSchematic(RewardData.getArrowTrapSchematic(), 1, -1, 1, 0, false, true, true, 0))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Trampoline", 15, new MessageRewardType(new MessagePart("Time to bounce!")), new SchematicRewardType(SchematicUtil.loadCustomSchematic(RewardData.getTrampolineSchematic(), 1, -3, 1, 0, false, true, true, 0)), new BlockRewardType(new OffsetBlock(2, -2, -2, Blocks.REDSTONE_BLOCK, false, 3).setRelativeToPlayer(true).setCausesBlockUpdate(true), new OffsetBlock(2, -2, -2, Blocks.REDSTONE_WIRE, false, 5).setRelativeToPlayer(true).setCausesBlockUpdate(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Skeleton_Bats", -40, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~1 ~ {Passengers:[{id:\"Skeleton\",ArmorItems:[{},{},{},{id:leather_helmet,Count:1}],HandItems:[{id:bow,Count:1},{}]}]}", 10))));
		//INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Death_Skeleton", -60, new CommandRewardType(new CommandPart("/summon Skeleton ~ ~1 ~ {CustomName:\"Death\",CustomNameVisible:1,SkeletonType:1,ArmorItems:[{id:leather_boots,Count:1,tag:{AttributeModifiers:[{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:1,Operation:0,UUIDLeast:490449,UUIDMost:374228}],ench:[{id:0,lvl:5}],display:{color:0}}},{id:leather_leggings,Count:1,tag:{ench:[{id:0,lvl:5}],display:{color:0}}},{id:leather_chestplate,Count:1,tag:{AttributeModifiers:[{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:5,Operation:0,UUIDLeast:114826,UUIDMost:869447}],ench:[{id:0,lvl:5}],display:{color:0}}},{id:leather_helmet,Count:1,tag:{ench:[{id:0,lvl:5}],display:{color:0}}}],HandItems:[{id:iron_sword,Count:1,tag:{display:{Name:\"Sword of Death\",Lore:[Courtesy of NekoSpiral]},ench:[{id:16,lvl:4},{id:19,lvl:4},{id:20,lvl:2}]}},{}],ArmorDropChances:[0.0F,0.0F,0.0F,0.0F],HandDropChances:[0.2F,0.085F]}"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cave_Spider_Web", -30, new BlockRewardType(RewardsUtil.fillArea(7, 4, 7, Blocks.COBWEB, -3, 0, -3, false, 0, false, true)), new CommandRewardType(RewardsUtil.executeXCommands("/summon cave_spider ~ ~1 ~ {CustomName:\"CascadingDongs\",CustomNameVisible:1}", 6))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Guardians", -35, new BlockRewardType(RewardsUtil.fillArea(5, 5, 5, Blocks.WATER, -2, 0, -2, false, 0, false, false)), new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("guardian"), 5), new EntityPart(EntityRewardType.getBasicNBTForEntity("guardian"), 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cookie_Monster", -5, new MessageRewardType(new MessagePart("Here have some cookies!").setRange(32), new MessagePart("[Cookie Monster] Hey! Those are mine!", 30).setRange(32)), new CommandRewardType(new CommandPart("/summon item ~ ~1 ~ {Item:{id:\"minecraft:cookie\",Count:8b}}"), new CommandPart("/summon zombie ~ ~1 ~ {CustomName:\"Cookie Monster\",CustomNameVisible:1,IsVillager:0,IsBaby:1}", 30))));

		ItemStack stack;
		NBTTagCompound nbt = new NBTTagCompound();

		stack = new ItemStack(Items.STICK);
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("sharpness")), 5);
		stack.setDisplayName(new TextComponentString("A Big Stick"));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Roosevelt's_Stick", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.FISHING_ROD);
		stack.setDamage(stack.getMaxDamage() / 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Half_Fishingrod", 5, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1);
		stack.setDisplayName(new TextComponentString("Notch"));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Notch", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.NETHER_STAR);
		stack.setDisplayName(new TextComponentString("North Star"));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Star", 100, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("sharpness")), 10);
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("unbreaking")), 10);
		stack.setDamage(stack.getMaxDamage() - 2);
		stack.setDisplayName(new TextComponentString("The Divine Sword"));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Divine", 95, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.WOODEN_PICKAXE);
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("efficiency")), 10);
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("fortune")), 3);
		stack.setDisplayName(new TextComponentString("Giga Breaker"));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Giga_Breaker", 70, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.BOW);
		stack.setDamage(stack.getMaxDamage());
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("power")), 5);
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("punch")), 3);
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("flame")), 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":One_Shot", 75, new ItemRewardType(new ItemPart(stack), new ItemPart(new ItemStack(Items.ARROW, 1)))));

		stack = new ItemStack(Items.TROPICAL_FISH, 1);
		stack.setDisplayName(new TextComponentString("Nemo"));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Nemo", 10, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.TROPICAL_FISH, 1);
		stack.setDisplayName(new TextComponentString("Marlin"));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Marlin", 10, new ItemRewardType(new ItemPart(stack))));

		stack = new ItemStack(Items.FIRE_CHARGE, 1);
		stack.addEnchantment(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("fire_aspect")), 2);
		stack.setDisplayName(new TextComponentString("Why not?"));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fire_Aspect_Fire", 60, new ItemRewardType(new ItemPart(stack))));

		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = new TextComponentString("The broken path");
		sign.signText[1] = new TextComponentString("to succeed");
		nbt = new NBTTagCompound();
		((TileEntity) sign).write(nbt);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Path_To_Succeed", 0, new BlockRewardType(new OffsetTileEntity(0, 0, -5, Blocks.SIGN, nbt, true, 20), new OffsetBlock(0, -1, 0, Blocks.COBBLESTONE, true, 0), new OffsetBlock(0, -1, -1, Blocks.COBBLESTONE, true, 4), new OffsetBlock(0, -1, -2, Blocks.COBBLESTONE, true, 8), new OffsetBlock(0, -1, -3, Blocks.COBBLESTONE, true, 12), new OffsetBlock(0, -1, -4, Blocks.COBBLESTONE, true, 16), new OffsetBlock(0, -1, -5, Blocks.COBBLESTONE, true, 20))));

		OffsetTileEntity[] signs = new OffsetTileEntity[4];
		OffsetTileEntity temp;
		for(int i = 0; i < signs.length; i++)
		{
			sign = new TileEntitySign();
			sign.signText[0] = new TextComponentString("Help Me!");
			nbt = new NBTTagCompound();
			((TileEntity) sign).write(nbt);
			temp = new OffsetTileEntity(i == 2 ? -2 : i == 3 ? 2 : 0, 1, i == 0 ? -2 : i == 1 ? 2 : 0, Blocks.WALL_SIGN, nbt, false, 5);
			temp.setBlockState(Blocks.WALL_SIGN.getDefaultState().with(BlockWallSign.FACING, EnumFacing.byHorizontalIndex(i + 2)));
			signs[i] = temp;
		}

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Help_Me", -10, new BlockRewardType(RewardsUtil.addBlocksLists(RewardsUtil.fillArea(3, 1, 3, Blocks.STONE_BRICKS, -1, -1, -1, false, 0, true, false), RewardsUtil.fillArea(3, 3, 3, Blocks.IRON_BARS, -1, 0, -1, false, 0, true, false), RewardsUtil.fillArea(1, 3, 1, Blocks.AIR, 0, 0, 0, false, 1, true, false), signs)), new EntityRewardType(new EntityPart(EntityRewardType.getBasicNBTForEntity("Villager"), 5).setRemovedBlocks(false)), new CommandRewardType(new CommandPart("/summon tnt %x %y %z {Fuse:80}", 5))));

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

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Half_Heart", -30)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
			{
				player.setHealth(1f);
			}
		});

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":No_Exp", -40)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
			{
				player.experienceLevel = 0;
				player.experienceTotal = 0;
				player.experience = 0;
				player.sendMessage(new TextComponentString("Rip EXP"));
			}
		});

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Smite", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
			{
				world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ, false));
				player.sendMessage(new TextComponentString("Thou has been smitten!"));
			}
		});

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cookie-splosion", 35)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
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

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Random_Status_Effect", 0)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
			{
				player.sendMessage(new TextComponentString("Selecting random potion effect to apply..."));

				Scheduler.scheduleTask(new Task("Random_Status_Effect", 30)
				{
					@Override
					public void callback()
					{
						PotionEffect effect = RewardsUtil.getRandomPotionEffect();
						player.sendMessage(new TextComponentString("You have been given " + effect.toString() + " seconds!"));
						player.addPotionEffect(effect);
					}
				});
			}
		});

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Arrow_Spray", -15)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
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

		ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Lingering_Potions_Ring", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player)
			{
				EntityPotion pot;
				for(double rad = -Math.PI; rad <= Math.PI; rad += (Math.PI / 10))
				{
					PotionType effect = RewardsUtil.getRandomPotionType();
					pot = new EntityPotion(world, player, PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), effect));
					pot.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					pot.motionX = Math.cos(rad) * (0.1 + (0.05 * 3));
					pot.motionY = 1;
					pot.motionZ = Math.sin(rad) * (0.1 + (0.05 * 3));
					world.spawnEntity(pot);
				}
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
		INSTANCE.registerReward(new ItemChestReward());
		INSTANCE.registerReward(new MagicFeetReward());
		INSTANCE.registerReward(new DigBuildReward());
		INSTANCE.registerReward(new ChanceCubeRenameReward());
		INSTANCE.registerReward(new CountDownReward());
		INSTANCE.registerReward(new TravellerReward());
		INSTANCE.registerReward(new MobTowerReward());
		INSTANCE.registerReward(new MontyHallReward());
		INSTANCE.registerReward(new MatchingReward());
		INSTANCE.registerReward(new TicTacToeReward());
		INSTANCE.registerReward(new MobEffectsReward());

		MathReward math = new MathReward();
		MinecraftForge.EVENT_BUS.register(math);
		INSTANCE.registerReward(math);

		QuestionsReward question = new QuestionsReward();
		MinecraftForge.EVENT_BUS.register(question);
		INSTANCE.registerReward(question);

		CoinFlipReward coinFlip = new CoinFlipReward();
		MinecraftForge.EVENT_BUS.register(coinFlip);
		INSTANCE.registerReward(coinFlip);
	}

	public static void loadCustomUserRewards(MinecraftServer server)
	{
		for(EntityPlayerMP player : server.getPlayerList().getPlayers())
			CustomUserReward.getCustomUserReward(player.getUniqueID());
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

	public Set<String> getRewardNames()
	{
		return nameToReward.keySet();
	}

	public Set<String> getDisabledRewardNames()
	{
		return disabledNameToReward.keySet();
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
				if(!stack.isEmpty() && stack.getItem() instanceof ItemChancePendant)
				{
					ItemChancePendant pendant = (ItemChancePendant) stack.getItem();
					pendant.damage(stack);
					if(stack.getDamage() >= CCubesSettings.pendantUses)
						player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
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
		if(lastReward != null || cooldownList.contains(pickedReward))
		{
			byte atempts = 0;
			while(atempts < 5 && cooldownList.contains(pickedReward))
			{
				pick = random.nextInt(range) + lowerIndex;
				pickedReward = sortedRewards.get(pick);
				atempts++;
			}
		}
		CCubesCore.logger.log(Level.INFO, "Triggered the reward with the name of: " + pickedReward.getName());
		pickedReward.trigger(world, pos, player);
		lastReward = pickedReward;
		cooldownList.add(lastReward);
		if(cooldownList.size() > 15)
		{
			cooldownList.remove(0);
		}
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