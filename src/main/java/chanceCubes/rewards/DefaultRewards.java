package chanceCubes.rewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.parsers.RewardParser;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.defaultRewards.*;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.rewards.rewardparts.EntityPart;
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Have_Another", -10, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.CHANCE_CUBE, 3))), new MessageRewardType("I hear you like Chance Cubes.", "So I put some Chance Cubes in your Chance Cubes!")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Icsahedron", 0, new ItemRewardType(new ItemPart(new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Farmer", 35, new MessageRewardType("Time to farm!"), new ItemRewardType(new ItemPart(new ItemStack(Items.IRON_HOE)), new ItemPart(new ItemStack(Items.BUCKET)), new ItemPart(new ItemStack(Items.WHEAT_SEEDS, 16)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Rancher", 60, new ItemRewardType(new ItemPart(new ItemStack(Blocks.OAK_FENCE, 32)), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("Pig"))), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("Cow"))), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("Sheep"))))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Fighter", 25, new MessageRewardType("SPARTAAA!!!"), new ItemRewardType(RewardsUtil.generateItemParts(Items.IRON_SWORD, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Pssst", -5, new MessageRewardType("Pssssst.... Over here!"), new EntityRewardType(new EntityPart("Creeper"))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Explorer", 30, new MessageRewardType("Lets go on a journey!"), new ItemRewardType(RewardsUtil.generateItemParts(new ItemStack(Items.COMPASS), new ItemStack(Items.CLOCK), new ItemStack(Blocks.TORCH, 64), new ItemStack(Items.IRON_PICKAXE)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Mitas", 50, new ItemRewardType(RewardsUtil.generateItemParts(new ItemStack(Items.GOLD_NUGGET, 32), new ItemStack(Items.GOLD_INGOT, 8), new ItemStack(Items.GOLDEN_CARROT, 16), new ItemStack(Items.GOLDEN_HELMET)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Horde", -25, new MessageRewardType("Release the horde!"), new EntityRewardType(RewardsUtil.spawnXEntities(EntityRewardType.getBasicNBTForEntity("Zombie"), 15))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Lava_Ring", -40, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 0, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, -1, Blocks.LAVA, false).setRelativeToPlayer(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Rain", -5, new CommandRewardType("/weather thunder 20000")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Silverfish_Surround", -20, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 0, Blocks.MONSTER_EGG, false).setRelativeToPlayer(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Fish_Dog", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.FISH, 5)), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("wolf"))))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Bone_Cat", 20, new ItemRewardType(new ItemPart(new ItemStack(Items.BONE, 5)), new ItemPart(RewardsUtil.getSpawnEggForEntity(new ResourceLocation("ocelot"))))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":XP_Crystal", -60, new CommandRewardType("/summon xp_orb ~ ~1 ~ {Value:1,Passengers:[{id:\"ender_crystal\"}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Cat", -25, new CommandRewardType("/summon ocelot ~ ~1 ~ {CatType:0,Sitting:0,Passengers:[{id:\"tnt\",Fuse:80}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Slime_Man", 10, new CommandRewardType("/summon slime ~ ~1 ~ {Size:3,Glowing:1b,Passengers:[{id:\"Slime\",Size:2,Glowing:1b,Passengers:[{id:\"Slime\",CustomName:\"Slime Man\",CustomNameVisible:1,Size:1,Glowing:1b}]}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Sail_Away", 5, new BlockRewardType(new OffsetBlock(0, -1, 0, Blocks.WATER, false)), new CommandRewardType("/summon Boat %x %y %z"), new MessageRewardType("Come sail away!")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Witch", -15, new CommandRewardType("/summon witch %x %y %z ")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Cluckington", 25, new CommandRewardType("/summon Chicken ~ ~1 ~ {CustomName:\"Cluckington\",CustomNameVisible:1,ActiveEffects:[{Id:1,Amplifier:3,Duration:199980}],Passengers:[{id:\"Zombie\",CustomName:\"wyld\",CustomNameVisible:1,IsVillager:0,IsBaby:1,ArmorItems:[{},{},{},{id:\"minecraft:skull\",Damage:3,tag:{SkullOwner:\"wyld\"},Count:1}]}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Jerry", 25, new CommandRewardType("/summon slime %x %y %z {Size:1,CustomName:\"Jerry\",CustomNameVisible:1}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Glenn", 25, new CommandRewardType("/summon zombie %x %y %z {CustomName:\"Glenn\",CustomNameVisible:1}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Dr_Trayaurus", 25, new CommandRewardType("/summon villager %x %y %z {CustomName:\"Dr Trayaurus\",CustomNameVisible:1,Profession:1}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Pickles", 25, new CommandRewardType("/summon mooshroom ~ ~1 ~ {Age:-10000,CustomName:\"Pickles\"}"), new MessageRewardType("Why is his name pickles? The world may never know")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Want_To_Build_A_Snowman", 45, new MessageRewardType("Do you want to build a snowman?"), new ItemRewardType(new ItemPart(new ItemStack(Blocks.SNOW, 2)), new ItemPart(new ItemStack(Blocks.PUMPKIN)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Diamond_Block", 85, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.DIAMOND_BLOCK, true, 200))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Diamond", -35, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.DIAMOND_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.DIAMOND_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Fake_TNT", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_TNT_PRIMED), new SoundPart(SoundEvents.ENTITY_GENERIC_EXPLODE, 120).setAtPlayersLocation(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Ghasts", 0, new SoundRewardType(new SoundPart(SoundEvents.ENTITY_GHAST_SCREAM).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true), new SoundPart(SoundEvents.ENTITY_GHAST_WARN).setServerWide(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":No", 0, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.CHANCE_CUBE, false)), new MessageRewardType("No")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Creeper", -30, new CommandRewardType("/summon Creeper %x %y %z {ActiveEffects:[{Id:14,Amplifier:0,Duration:200,ShowParticles:0b}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Knockback_Zombie", -35, new CommandRewardType("/summon Zombie ~ ~1 ~ {CustomName:\"Leonidas\",CustomNameVisible:1,IsVillager:0,IsBaby:1,HandItems:[{id:stick,Count:1,tag:{AttributeModifiers:[{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:100,Operation:0,UUIDLeast:724513,UUIDMost:715230}],ench:[{id:19,lvl:100}],display:{Name:\"The Spartan Kick\"}}},{}],HandDropChances:[0.0F,0.085F],ActiveEffects:[{Id:1,Amplifier:5,Duration:199980,ShowParticles:0b},{Id:8,Amplifier:2,Duration:199980}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Actual_Invisible_Ghast", -80, new CommandRewardType("/summon Ghast ~ ~10 ~ {ActiveEffects:[{Id:14,Amplifier:0,Duration:2000,ShowParticles:0b}]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Fireworks", 5, new CommandRewardType(RewardsUtil.executeXCommands("/summon fireworks_rocket ~ ~1 ~ {LifeTime:15,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Colors:[I;16711680],FadeColors:[I;16711680]}]}}}}", 4))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Bats", -50, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~1 ~ {Passengers:[{id:\"tnt\",Fuse:80}]}", 10))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Jelly_Fish", -40, new CommandRewardType(RewardsUtil.executeXCommands("/summon bat ~ ~1 ~ {Passengers:[{id:\"magma_cube\",CustomName:\"Nether Jelly Fish\",CustomNameVisible:1,Size:3}]}", 10))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Pig_Of_Destiny", 15, new CommandRewardType("/summon Pig ~ ~1 ~ {CustomName:\"The Pig of Destiny\",CustomNameVisible:1,ArmorItems:[{},{},{id:diamond_chestplate,Count:1,tag:{ench:[{id:7,lvl:1000}]}},{}],ArmorDropChances:[0.085F,0.085F,0.0F,0.085F]}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":DIY_Pie", 5, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.PUMPKIN, false), new OffsetBlock(1, 1, 0, Blocks.REEDS, false)), new CommandRewardType("/summon Chicken ~ ~1 ~ {CustomName:\"Zeeth_Kyrah\",CustomNameVisible:1}"), new MessageRewardType("Do it yourself Pumpkin Pie!")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Coal_To_Diamonds", 10, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, -1, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(-1, 0, 0, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, 1, Blocks.COAL_BLOCK, false), new OffsetBlock(0, 0, -1, Blocks.COAL_BLOCK, false)), new CommandRewardType(RewardsUtil.executeXCommands("/summon tnt %x %y %z {Fuse:40}", 3, 5)), new ItemRewardType(new ItemPart(new ItemStack(Items.DIAMOND, 5), 50))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":SpongeBob_SquarePants", 15, new CommandRewardType("/summon Item ~ ~ ~ {Item:{id:sponge,Count:1,tag:{display:{Name:\"SpongeBob\"}}}}", "/summon Item ~ ~ ~ {Item:{id:leather_leggings,Count:1,tag:{display:{Name:\"SquarePants\"}}}}")));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Quidditch", -30, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~ ~ {Passengers:[{id:\"Witch\"}]}", 7)), new MessageRewardType(new MessagePart("Quidditch anyone?").setRange(32))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":One_Man_Army", -10, new EntityRewardType("zombie_pigman"), new CommandRewardType(RewardsUtil.executeXCommands("/summon zombie_pigman ~ ~ ~ {Silent:1,ActiveEffects:[{Id:14,Amplifier:0,Duration:19980,ShowParticles:1b}]}", 9)), new MessageRewardType(new MessagePart("One man army").setRange(32))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Cuteness", 10, new CommandRewardType(RewardsUtil.executeXCommands("/summon rabbit ~ ~1 ~ {CustomName:\"Fluffy\",CustomNameVisible:1,RabbitType:5}", 20)), new MessageRewardType(new MessagePart("Cuteness overload!").setRange(32))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Silvermite_Stacks", -35, new CommandRewardType(RewardsUtil.executeXCommands("/summon Silverfish ~ ~1 ~ {Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\",Passengers:[{id:\"Endermite\",Passengers:[{id:\"Silverfish\"}]}]}]}]}]}]}]}]}]}]}", 5))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Invizible_Silverfish", -55, new CommandRewardType(RewardsUtil.executeXCommands("/summon Silverfish ~ ~1 ~ {Glowing:1b,ActiveEffects:[{Id:1,Amplifier:1,Duration:200000},{Id:14,Amplifier:0,Duration:20000}],Passengers:[{id:\"Silverfish\",ActiveEffects:[{Id:14,Amplifier:0,Duration:20000}]}]}", 5))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Arrow_Trap", -25, new SchematicRewardType(new SchematicPart("/assets/chancecubes/schematics/arrow_trap.ccs", true, new IntVar(1), new IntVar(-1), new IntVar(1), new FloatVar(0), new BoolVar(false), new BoolVar(true), new BoolVar(true), new BoolVar(true), new IntVar(0)))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Trampoline", 15, new MessageRewardType("Time to bounce!"), new SchematicRewardType(new SchematicPart("/assets/chancecubes/schematics/trampoline.ccs", true, new IntVar(1), new IntVar(-3), new IntVar(1), new FloatVar(0), new BoolVar(false), new BoolVar(true), new BoolVar(true), new BoolVar(true), new IntVar(0))), new BlockRewardType(new OffsetBlock(2, -2, -2, Blocks.REDSTONE_BLOCK, false, 3).setRelativeToPlayer(true).setCausesBlockUpdate(true), new OffsetBlock(2, -2, -2, Blocks.REDSTONE_WIRE, false, 5).setRelativeToPlayer(true).setCausesBlockUpdate(true))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Skeleton_Bats", -40, new CommandRewardType(RewardsUtil.executeXCommands("/summon Bat ~ ~1 ~ {Passengers:[{id:\"Skeleton\",ArmorItems:[{},{},{},{id:leather_helmet,Count:1}],HandItems:[{id:bow,Count:1},{}]}]}", 10))));
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Cookie_Monster", -5, new MessageRewardType(new MessagePart("Here have some cookies!").setRange(32), new MessagePart("[Cookie Monster] Hey! Those are mine!", 30).setRange(32)), new CommandRewardType(new CommandPart("/summon item ~ ~1 ~ {Item:{id:\"minecraft:cookie\",Count:8b}}"), new CommandPart("/summon zombie ~ ~1 ~ {CustomName:\"Cookie Monster\",CustomNameVisible:1,IsVillager:0,IsBaby:1}", 30))));

		NBTTagCompound nbt;

		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = new TextComponentString("The broken path");
		sign.signText[1] = new TextComponentString("to succeed");
		nbt = new NBTTagCompound();
		((TileEntity) sign).writeToNBT(nbt);
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Path_To_Succeed", 0, new BlockRewardType(new OffsetTileEntity(0, 0, -5, Blocks.STANDING_SIGN, nbt, true, 20), new OffsetBlock(0, -1, 0, Blocks.COBBLESTONE, true, 0), new OffsetBlock(0, -1, -1, Blocks.COBBLESTONE, true, 4), new OffsetBlock(0, -1, -2, Blocks.COBBLESTONE, true, 8), new OffsetBlock(0, -1, -3, Blocks.COBBLESTONE, true, 12), new OffsetBlock(0, -1, -4, Blocks.COBBLESTONE, true, 16), new OffsetBlock(0, -1, -5, Blocks.COBBLESTONE, true, 20))));


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
		GlobalCCRewardRegistry.DEFAULT.registerReward(new BasicReward(CCubesCore.MODID + ":Beacon_Build", 100, new BlockRewardType(blocks)));

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Half_Heart", -30)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				player.setHealth(1f);
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":No_Exp", -40)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Smite", -10)
		{
			@Override
			public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
			{
				world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ, false));
				player.sendMessage(new TextComponentString("Thou has been smitten!"));
			}
		});

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Cookie-splosion", 35)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Random_Status_Effect", 0)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Arrow_Spray", -15)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Lingering_Potions_Ring", -10)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Charged_Creeper", -40)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Disco", 40)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Ender_Crystal_Timer", -90)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":5_Prongs", -10)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Inventory_Bomb", -55)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Nuke", -75)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Random_Teleport", -15)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Rotten_Food", -30)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Thrown_In_Air", -35)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Torches_To_Creepers", -40)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Traveller", 15)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Troll_Hole", -20)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Saw_Nothing_Diamond", 0)
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

		GlobalCCRewardRegistry.DEFAULT.registerReward(new BaseCustomReward(CCubesCore.MODID + ":Hand_Enchant", 20)
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
