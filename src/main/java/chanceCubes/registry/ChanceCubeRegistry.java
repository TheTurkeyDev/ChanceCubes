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
import chanceCubes.rewards.CookieMonsterReward;
import chanceCubes.rewards.CreeperSurroundedReward;
import chanceCubes.rewards.FiveProngReward;
import chanceCubes.rewards.HerobrineReward;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.MathReward;
import chanceCubes.rewards.NukeReward;
import chanceCubes.rewards.RandomTeleportReward;
import chanceCubes.rewards.SurroundedReward;
import chanceCubes.rewards.TrollHoleReward;
import chanceCubes.rewards.TrollTNTReward;
import chanceCubes.rewards.WitherReward;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.PotionRewardType;
import chanceCubes.rewards.type.SoundRewardType;
import chanceCubes.util.OffsetBlock;

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
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":BedRock", -80, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.bedrock, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Redstone_Diamond", 10, new ItemRewardType(new ItemStack(Items.redstone), new ItemStack(Items.diamond))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sethbling_Reward", 40, new MessageRewardType("Welcome back, SethBling here :)"), new ItemRewardType(new ItemStack(Items.redstone, 32), new ItemStack(Items.repeater, 3), new ItemStack(Items.comparator, 3), new ItemStack(Blocks.redstone_lamp, 3), new ItemStack(Blocks.redstone_torch, 3))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Creeper", -10, new EntityRewardType(EntityRewardType.getBasicNBTForEntity("Creeper"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP", 75, new ExperienceRewardType(100)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Poison", -30, new PotionRewardType(new PotionEffect(Potion.poison.id, 16 * 20))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":ChatMessage", 0, new MessageRewardType("You have escaped the wrath of the Chance Cubes.........", "For now......")));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Command", 15, new CommandRewardType(" /give %player minecraft:painting 1 0 {display:{Name:\"Wylds Bestest friend\",Lore:[\"You know you love me, \"]}}")));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Particles", 0, new ParticleEffectRewardType("smoke", "smoke", "smoke", "smoke", "smoke", "smoke", "smoke", "smoke", "smoke")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Wool", 15, new ItemRewardType(new ItemStack(Blocks.wool, 4, 0), new ItemStack(Blocks.wool, 4, 1), new ItemStack(Blocks.wool, 4, 2), new ItemStack(Blocks.wool, 4, 3), new ItemStack(Blocks.wool, 4, 4), new ItemStack(Blocks.wool, 4, 5), new ItemStack(Blocks.wool, 4, 6), new ItemStack(Blocks.wool, 4, 7), new ItemStack(Blocks.wool, 4, 8), new ItemStack(Blocks.wool, 4, 9), new ItemStack(Blocks.wool, 4, 10), new ItemStack(Blocks.wool, 4, 11), new ItemStack(Blocks.wool, 4, 12), new ItemStack(Blocks.wool, 4, 13), new ItemStack(Blocks.wool, 4, 14), new ItemStack(Blocks.wool, 4, 15))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Beacon", 100, new ItemRewardType(new ItemStack(Blocks.beacon))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cake", 45, new ItemRewardType(new ItemStack(Items.cake, 1)), new MessageRewardType("But is it a lie?")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Enchanting", 80, new ItemRewardType(new ItemStack(Blocks.enchanting_table))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bookshelves", 80, new ItemRewardType(new ItemStack(Blocks.bookshelf, 64))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Ores_Galore", 65, new ItemRewardType(new ItemStack(Items.coal), new ItemStack(Items.redstone), new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond), new ItemStack(Items.emerald))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Have_Another", 0, new ItemRewardType(new ItemStack(CCubesBlocks.chanceCube, 3)), new MessageRewardType("Here, have more!")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Icsahedron", 0, new ItemRewardType(new ItemStack(CCubesBlocks.chanceIcosahedron))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Saplings", 10, new ItemRewardType(new ItemStack(Blocks.sapling, 16, 0), new ItemStack(Blocks.sapling, 16, 0), new ItemStack(Blocks.sapling, 16, 1), new ItemStack(Blocks.sapling, 16, 2), new ItemStack(Blocks.sapling, 16, 3), new ItemStack(Blocks.sapling, 16, 4), new ItemStack(Blocks.sapling, 16, 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Farmer", 35, new MessageRewardType("Time to farm!"), new ItemRewardType(new ItemStack(Items.iron_hoe), new ItemStack(Items.bucket), new ItemStack(Items.wheat_seeds, 16))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rancher", 55, new ItemRewardType(new ItemStack(Blocks.fence, 32), new ItemStack(Items.spawn_egg, 1, 90), new ItemStack(Items.spawn_egg, 1, 91), new ItemStack(Items.spawn_egg, 1, 92))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fighter", 20, new MessageRewardType("SPARTAAA!!!"), new ItemRewardType(new ItemStack(Items.iron_sword), new ItemStack(Items.iron_helmet), new ItemStack(Items.iron_chestplate), new ItemStack(Items.iron_leggings), new ItemStack(Items.iron_boots))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":pssst", 0, new MessageRewardType("Pssssst.... Over here!"), new EntityRewardType(EntityRewardType.getBasicNBTForEntity("Creeper"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explorer", 25, new MessageRewardType("Lets go on a journey!"), new ItemRewardType(new ItemStack(Items.compass), new ItemStack(Items.clock), new ItemStack(Blocks.torch, 64), new ItemStack(Items.iron_pickaxe))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Mitas", 60, new ItemRewardType(new ItemStack(Items.gold_nugget, 32), new ItemStack(Items.gold_ingot, 8), new ItemStack(Items.golden_carrot, 16), new ItemStack(Items.golden_helmet))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Horde", -40, new MessageRewardType("Release the horde!"), new EntityRewardType(EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Insta-Damage", -50, new PotionRewardType(new PotionEffect(Potion.harm.id, 2), new PotionEffect(Potion.harm.id, 2), new PotionEffect(Potion.harm.id, 2), new PotionEffect(Potion.harm.id, 2))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rain", -15, new CommandRewardType("/weather thunder 20000")));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":House", 75, new SchematicRewardType("house.schematic", 3, true, false)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Lava_Ring", -20, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, 0, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(-1, -1, -1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(0, -1, -1, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, -1, Blocks.lava, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Silverfish_Surround", -15, new BlockRewardType(new OffsetBlock(1, 0, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(1, 1, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, 1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(-1, 0, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(-1, 1, 0, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 0, -1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, -1, Blocks.monster_egg, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.monster_egg, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fish_Dog", 35, new ItemRewardType(new ItemStack(Items.fish, 5), new ItemStack(Items.spawn_egg, 1, 95))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bone_Cat", 35, new ItemRewardType(new ItemStack(Items.bone, 5), new ItemStack(Items.spawn_egg, 1, 98))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":XP_Crystal", -60, new CommandRewardType("/summon EnderCrystal %x %y %z {Value:1,Riding:{id:\"XPOrb\"}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Cat", -30, new CommandRewardType("/summon PrimedTnt %x %y %z {Fuse:80,Riding:{id:\"Ozelot\",CatType:0,Sitting:0}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":SlimeMan", 65, new CommandRewardType("/summon Slime %x %y %z {CustomName:\"SlimeMan\",CustomNameVisible:1,Size:1,Riding:{id:\"Slime\",Size:2,Riding:{id:\"Slime\",Size:3}}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Sail_Away", 0, new BlockRewardType(new OffsetBlock(0, -1, 0, Blocks.water, false)), new CommandRewardType("/summon Boat %x %y %z"), new MessageRewardType("Come sail away!")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Witch", -75, new CommandRewardType("/summon Witch %x %y %z ")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Cluckington", 60, new CommandRewardType("/summon Zombie ~ ~1 ~ {CustomName:\"Wyld\",CustomNameVisible:1,IsBaby:1,Riding:{id:\"Chicken\",CustomName:\"Cluckinton\",CustomNameVisible:1,IsChickenJockey:1}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Jerry", 60, new CommandRewardType("/summon Slime %x %y %z {Size:1,CustomName:\"Jerry\",CustomNameVisible:1}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Spawn_Glenn", 60, new CommandRewardType("/summon Zombie %x %y %z {CustomName:\"Glenn\",CustomNameVisible:1}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Want_To_Build_A_Snowman", 50, new MessageRewardType("Do you want to build a snowman?"), new ItemRewardType(new ItemStack(Blocks.snow, 2), new ItemStack(Blocks.pumpkin))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Frozen", -5, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.ice, false).setRelativeToPlayer(true), new OffsetBlock(0, 1, 0, Blocks.ice, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":You_saw_nothing", 0, new MessageRewardType("You didn't see anything......")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Diamond_Block", 80, new BlockRewardType(new OffsetBlock(0, 0, 0, Blocks.diamond_block, true, 200))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":TNT_Diamond", 75, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.diamond_block, false), new OffsetBlock(0, -1, 0, Blocks.diamond_block, false), new OffsetBlock(1, 0, 0, Blocks.diamond_block, false), new OffsetBlock(-1, 0, 0, Blocks.diamond_block, false), new OffsetBlock(0, 0, 1, Blocks.diamond_block, false), new OffsetBlock(0, 0, -1, Blocks.diamond_block, false)), new CommandRewardType("/summon PrimedTnt %x %y %z {Fuse:40}", "/summon PrimedTnt %x %y %z {Fuse:40}", "/summon PrimedTnt %x %y %z {Fuse:40}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Anti_Slab", 30, new BlockRewardType(new OffsetBlock(-1, 2, -1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, -1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(1, 2, -1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(-1, 2, 0, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 0, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(1, 2, 0, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(-1, 2, 1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(0, 2, 1, Blocks.obsidian, false).setRelativeToPlayer(true), new OffsetBlock(1, 2, 1, Blocks.obsidian, false).setRelativeToPlayer(true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Chance_Cube_Cube", 15, new BlockRewardType(new OffsetBlock(-1, 0, -1, CCubesBlocks.chanceCube, false), new OffsetBlock(-1, 0, -2, CCubesBlocks.chanceCube, false), new OffsetBlock(-2, 0, -1, CCubesBlocks.chanceCube, false), new OffsetBlock(-2, 0, -2, CCubesBlocks.chanceCube, false), new OffsetBlock(-1, 1, -1, CCubesBlocks.chanceCube, false), new OffsetBlock(-1, 1, -2, CCubesBlocks.chanceCube, false), new OffsetBlock(-2, 1, -1, CCubesBlocks.chanceCube, false), new OffsetBlock(-2, 1, -2, CCubesBlocks.chanceCube, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fake_TNT", 5, new SoundRewardType("game.tnt.primed","game.tnt.primed","game.tnt.primed","game.tnt.primed")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invisible_Ghasts", 5, new SoundRewardType("mob.ghast.scream", "mob.ghast.moan", "mob.ghast.moan")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":No", -35, new BlockRewardType(new OffsetBlock(0, 0, 0, CCubesBlocks.chanceCube, false)), new MessageRewardType("No")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Invizible_Creeper", -10, new CommandRewardType("/summon Creeper %x %y %z {ActiveEffects:[{Id:14,Amplifier:0,Duration:200,ShowParticles:0b}]}")));

		ItemStack stack;

		stack = new ItemStack(Items.stick);
		stack.addEnchantment(Enchantment.sharpness, 5);
		stack.setStackDisplayName("A Big Stick");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Roosevelt's_Stick", 60, new ItemRewardType(stack)));

		stack = new ItemStack(Items.fishing_rod);
		stack.setItemDamage(stack.getMaxDamage() / 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Half_Fishingrod", -10, new ItemRewardType(stack)));

		stack = new ItemStack(Items.golden_apple, 1, 1);
		stack.setStackDisplayName("Notch");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Notch", 100, new ItemRewardType(stack)));

		stack = new ItemStack(Items.nether_star);
		stack.setStackDisplayName("North Star");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Nether_Star", 100, new ItemRewardType(stack)));

		stack = new ItemStack(Items.diamond_sword);
		stack.addEnchantment(Enchantment.sharpness, 10);
		stack.addEnchantment(Enchantment.unbreaking, 10);
		stack.setItemDamage(stack.getMaxDamage() - 2);
		stack.setStackDisplayName("The Divine Sword");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Divine", 90, new ItemRewardType(stack)));

		stack = new ItemStack(Items.bow);
		stack.setItemDamage(stack.getMaxDamage() - 1);
		stack.addEnchantment(Enchantment.power, 5);
		stack.addEnchantment(Enchantment.punch, 3);
		stack.addEnchantment(Enchantment.flame, 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":One_Shot", 85, new ItemRewardType(stack, new ItemStack(Items.arrow, 1))));

		stack = new ItemStack(Items.fish, 1, 2);
		stack.setStackDisplayName("Nemo");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Nemo", 10, new ItemRewardType(stack)));

		stack = new ItemStack(Items.fish, 1, 2);
		stack.setStackDisplayName("Marlin");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Finding_Marlin", 10, new ItemRewardType(stack)));
		
		stack = new ItemStack(Blocks.fire, 1);
		stack.addEnchantment(Enchantment.fireAspect, 2);
		stack.setStackDisplayName("Why not?");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fire_Aspect_Fire", 20, new ItemRewardType(stack)));

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
		//INSTANCE.registerReward(new EnderCrystalTimerReward());
		
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
		if(ConfigLoader.config.getBoolean(reward.getName(), ConfigLoader.rewardCat, true, "Set to false to disable this reward"))
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
		if(player != null)
		{
			for(int i = 0; i < player.inventory.mainInventory.length; i++)
			{
				ItemStack stack = player.inventory.mainInventory[i];
				if(stack != null && stack.getItem() instanceof ItemChancePendant)
				{
					ItemChancePendant pendant = (ItemChancePendant) stack.getItem();
					pendant.damage(stack);
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

		while(sortedRewards.get(lowerIndex).getChanceValue() <= lowerRange)
		{
			lowerIndex++;
			if(lowerIndex >= sortedRewards.size())
			{
				lowerIndex--;
				break;
			}
		}
		while(sortedRewards.get(upperIndex).getChanceValue() >= upperRange)
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
