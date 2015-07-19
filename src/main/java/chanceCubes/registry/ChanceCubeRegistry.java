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
import chanceCubes.rewards.FiveProngReward;
import chanceCubes.rewards.HerobrineReward;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.MathReward;
import chanceCubes.rewards.NukeReward;
import chanceCubes.rewards.RandomTeleportReward;
import chanceCubes.rewards.SurroundedReward;
import chanceCubes.rewards.TrollHoleReward;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.ParticleEffectRewardType;
import chanceCubes.rewards.type.PotionRewardType;
import chanceCubes.rewards.type.SchematicRewardType;
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

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Tnt_Structure", -40, new BlockRewardType(new OffsetBlock(-1, 0, -1, Blocks.tnt, true), new OffsetBlock(-1, 0, 0, Blocks.tnt, true), new OffsetBlock(-1, 0, 1, Blocks.tnt, true), new OffsetBlock(0, 0, -1, Blocks.tnt, true), new OffsetBlock(0, 0, 0, Blocks.tnt, true), new OffsetBlock(0, 0, 1, Blocks.tnt, true), new OffsetBlock(1, 0, -1, Blocks.tnt, true), new OffsetBlock(1, 0, 0, Blocks.tnt, true), new OffsetBlock(1, 0, 1, Blocks.tnt, true), new OffsetBlock(-1, 1, -1, Blocks.redstone_block, true, 80), new OffsetBlock(-1, 1, 0, Blocks.redstone_block, true, 80), new OffsetBlock(-1, 1, 1, Blocks.redstone_block, true, 80), new OffsetBlock(0, 1, -1, Blocks.redstone_block, true, 80), new OffsetBlock(0, 1, 0, Blocks.redstone_block, true, 80), new OffsetBlock(0, 1, 1, Blocks.redstone_block, true, 80), new OffsetBlock(1, 1, -1, Blocks.redstone_block, true, 80), new OffsetBlock(1, 1, 0, Blocks.redstone_block, true, 80), new OffsetBlock(1, 1, 1, Blocks.redstone_block, true, 80))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":BedRock", -80, new BlockRewardType(new OffsetBlock(0, 1, 0, Blocks.bedrock, false))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Redstone_Diamond", 10, new ItemRewardType(new ItemStack(Items.redstone), new ItemStack(Items.diamond))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Creeper", -10, new EntityRewardType(EntityRewardType.getBasicNBTForEntity("Creeper"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Redstone_Zombie", 0, new ItemRewardType(new ItemStack(Items.redstone)), new EntityRewardType(EntityRewardType.getBasicNBTForEntity("Zombie"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":EXP", 75, new ExperienceRewardType(100)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Poison", -30, new PotionRewardType(new PotionEffect(Potion.poison.id, 16 * 20))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":ChatMessage", 0, new MessageRewardType("You have escaped the wrath of the Chance Cubes.........", "For now......")));
		// INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Command", 15, new CommandRewardType(" /give %player minecraft:painting 1 0 {display:{Name:\"Wylds Bestest friend\",Lore:[\"You know you love me, \"]}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Particles", 0, new ParticleEffectRewardType("largesmoke", "largesmoke", "largesmoke", "largesmoke", "largesmoke", "largesmoke", "largesmoke", "largesmoke", "largesmoke")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Wool", 15, new ItemRewardType(new ItemStack(Blocks.wool, 16, 0), new ItemStack(Blocks.wool, 16, 1), new ItemStack(Blocks.wool, 16, 2), new ItemStack(Blocks.wool, 16, 3), new ItemStack(Blocks.wool, 16, 4), new ItemStack(Blocks.wool, 16, 5), new ItemStack(Blocks.wool, 16, 6), new ItemStack(Blocks.wool, 16, 7), new ItemStack(Blocks.wool, 16, 8), new ItemStack(Blocks.wool, 16, 9), new ItemStack(Blocks.wool, 16, 10), new ItemStack(Blocks.wool, 16, 11), new ItemStack(Blocks.wool, 16, 12), new ItemStack(Blocks.wool, 16, 13), new ItemStack(Blocks.wool, 16, 14), new ItemStack(Blocks.wool, 16, 15))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Beacon", 100, new ItemRewardType(new ItemStack(Blocks.beacon))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Cake", 30, new ItemRewardType(new ItemStack(Items.cake, 1)), new MessageRewardType("But is it a lie?")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Enchanting", 80, new ItemRewardType(new ItemStack(Blocks.enchanting_table))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Bookshelves", 80, new ItemRewardType(new ItemStack(Blocks.bookshelf, 64))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Ores_Golore", 65, new ItemRewardType(new ItemStack(Items.coal), new ItemStack(Items.redstone), new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond), new ItemStack(Items.emerald))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Have_Another", 0, new ItemRewardType(new ItemStack(CCubesBlocks.chanceCube)), new MessageRewardType("Here, have another!")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Icsahedron", 21, new ItemRewardType(new ItemStack(CCubesBlocks.chanceIcosahedron))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Saplings", 10, new ItemRewardType(new ItemStack(Blocks.sapling, 16, 0), new ItemStack(Blocks.sapling, 16, 0), new ItemStack(Blocks.sapling, 16, 1), new ItemStack(Blocks.sapling, 16, 2), new ItemStack(Blocks.sapling, 16, 3), new ItemStack(Blocks.sapling, 16, 4), new ItemStack(Blocks.sapling, 16, 5))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Farmer", 35, new MessageRewardType("Time to farm!"), new ItemRewardType(new ItemStack(Items.iron_hoe), new ItemStack(Items.bucket), new ItemStack(Items.wheat_seeds, 16))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rancher", 55, new ItemRewardType(new ItemStack(Blocks.fence, 32), new ItemStack(Items.spawn_egg, 1, 90), new ItemStack(Items.spawn_egg, 1, 91), new ItemStack(Items.spawn_egg, 1, 92))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Fighter", 21, new MessageRewardType("SPARTAAA!!!"), new ItemRewardType(new ItemStack(Items.iron_sword), new ItemStack(Items.iron_helmet), new ItemStack(Items.iron_chestplate), new ItemStack(Items.iron_leggings), new ItemStack(Items.iron_boots))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Explorer", 21, new MessageRewardType("Lets go on a journey!"), new ItemRewardType(new ItemStack(Items.compass), new ItemStack(Items.clock), new ItemStack(Blocks.torch, 64), new ItemStack(Items.iron_pickaxe))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Mitas", 21, new ItemRewardType(new ItemStack(Items.gold_nugget, 32), new ItemStack(Items.gold_ingot, 8), new ItemStack(Items.golden_carrot, 16), new ItemStack(Items.golden_helmet))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Horde", -40, new MessageRewardType("Release the horde!"), new EntityRewardType(EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"), EntityRewardType.getBasicNBTForEntity("Zombie"))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Insta-Damage", -50, new PotionRewardType(new PotionEffect(16428, 2), new PotionEffect(16428, 2), new PotionEffect(16428, 2), new PotionEffect(16428, 2))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Rain", -15, new CommandRewardType("/weather thunder 20000")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":House", 75, new SchematicRewardType("house.schematic", 3, true, false), new MessageRewardType("House made by: xZiiRx4KiinGs")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Lava_Ring", -20, new BlockRewardType(new OffsetBlock(1, -1, 0, Blocks.lava, false).setRelativeToPlayer(true), new OffsetBlock(1, -1, 1, Blocks.lava, false).setRelativeToPlayer(true) , new OffsetBlock(0, -1, 1, Blocks.lava, false).setRelativeToPlayer(true) , new OffsetBlock(-1, -1, 1, Blocks.lava, false).setRelativeToPlayer(true) , new OffsetBlock(-1, -1, 0, Blocks.lava, false).setRelativeToPlayer(true) , new OffsetBlock(-1, -1, -1, Blocks.lava, false).setRelativeToPlayer(true) , new OffsetBlock(0, -1, -1, Blocks.lava, false).setRelativeToPlayer(true) , new OffsetBlock(1, -1, -1, Blocks.lava, false).setRelativeToPlayer(true))));

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
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Divine", 90, new ItemRewardType(stack)));

		stack = new ItemStack(Items.bow);
		stack.setItemDamage(stack.getMaxDamage() - 1);
		stack.addEnchantment(Enchantment.power, 5);
		stack.addEnchantment(Enchantment.punch, 3);
		stack.addEnchantment(Enchantment.flame, 2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":One_Shot", 85, new ItemRewardType(stack, new ItemStack(Items.arrow, 1))));

		INSTANCE.registerReward(new NukeReward());
		INSTANCE.registerReward(new FiveProngReward());
		INSTANCE.registerReward(new AnvilRain());
		INSTANCE.registerReward(new HerobrineReward());
		INSTANCE.registerReward(new SurroundedReward());
		INSTANCE.registerReward(new RandomTeleportReward());
		INSTANCE.registerReward(new TrollHoleReward());
		INSTANCE.registerReward(new CookieMonsterReward());
		INSTANCE.registerReward(new BlindnessFightReward());

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
		return nameToReward.remove(name) != null;
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
		int pick = world.rand.nextInt(upperIndex - lowerIndex + 1) + lowerIndex;
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
