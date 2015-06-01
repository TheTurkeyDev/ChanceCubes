package chanceCubes.registry;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.items.ItemChancePendant;
import chanceCubes.rewards.BasicReward;
import chanceCubes.rewards.FiveProngReward;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.NukeReward;
import chanceCubes.rewards.type.CommandRewardType;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.ParticleEffectRewardType;
import chanceCubes.rewards.type.PotionRewardType;

import com.enderio.core.common.util.Bound;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChanceCubeRegistry implements IRewardRegistry
{
	private static final Bound<Integer> CHANCE_BOUND = Bound.of(-100, 100);

	public static ChanceCubeRegistry INSTANCE = new ChanceCubeRegistry();

	private Map<String, IChanceCubeReward> nameToReward = Maps.newHashMap();
	private List<IChanceCubeReward> sortedRewards = Lists.newArrayList();

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public static void loadDefaultRewards()
	{
		ItemStack stack;

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":RedstoneDiamond", 10, new ItemRewardType(new ItemStack(Items.redstone), new ItemStack(Items.diamond))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Creeper", -10, new EntityRewardType("Creeper")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":RedstoneZombie", 0, new ItemRewardType(new ItemStack(Items.redstone)), new EntityRewardType("Zombie")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":EXP", 75, new ExperienceRewardType(100)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Potions", -30, new PotionRewardType(new PotionEffect(Potion.poison.id, 16 * 20))));		
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":ChatMessage", 0, new MessageRewardType("You have escaped the wrath of the Chance Cubes.........", "For now......")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Command", 15, new CommandRewardType(" /give %player minecraft:painting 1 0 {display:{Name:\"Wylds Bestest friend\",Lore:[\"You know you love me, \"]}}")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Particles", 0, new ParticleEffectRewardType("largesmoke","largesmoke","largesmoke","largesmoke","largesmoke","largesmoke","largesmoke","largesmoke","largesmoke")));	
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":wool", 15, new ItemRewardType(new ItemStack(Blocks.wool, 16, 0),new ItemStack(Blocks.wool, 16, 1),new ItemStack(Blocks.wool, 16, 2),new ItemStack(Blocks.wool, 16, 3),new ItemStack(Blocks.wool, 16, 4),new ItemStack(Blocks.wool, 16, 5),new ItemStack(Blocks.wool, 16, 6),new ItemStack(Blocks.wool, 16, 7),new ItemStack(Blocks.wool, 16, 8),new ItemStack(Blocks.wool, 16, 9),new ItemStack(Blocks.wool, 16, 10),new ItemStack(Blocks.wool, 16, 11),new ItemStack(Blocks.wool, 16, 12),new ItemStack(Blocks.wool, 16, 13),new ItemStack(Blocks.wool, 16, 14),new ItemStack(Blocks.wool, 16, 15))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Beacon", 100, new ItemRewardType(new ItemStack(Blocks.beacon))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":cake", 30, new ItemRewardType(new ItemStack(Blocks.cake)), new MessageRewardType("But is it a lie?")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":enchanting", 80, new ItemRewardType(new ItemStack(Blocks.enchanting_table))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":BookShelves", 80, new ItemRewardType(new ItemStack(Blocks.bookshelf, 64))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":oresGolore", 65, new ItemRewardType(new ItemStack(Items.coal), new ItemStack(Items.redstone), new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond), new ItemStack(Items.emerald))));
		
		
		stack = new ItemStack(Items.redstone);
		stack.addEnchantment(Enchantment.sharpness, 5);
		stack.setStackDisplayName("A Big Stick");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Roosevelt's_Stick", 60, new ItemRewardType(stack)));

		stack = new ItemStack(Items.fishing_rod);
		stack.setItemDamage(stack.getMaxDamage()/2);
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Half_Fishingrod", -10, new ItemRewardType(stack)));

		stack = new ItemStack(Items.golden_apple, 1, 1);
		stack.setStackDisplayName("Notch");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Notch", 100, new ItemRewardType(stack)));

		stack = new ItemStack(Items.nether_star);
		stack.setStackDisplayName("North Star");
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Nether_Star", 100, new ItemRewardType(stack)));
		
		INSTANCE.registerReward(new NukeReward());
		INSTANCE.registerReward(new FiveProngReward());
	}

	@Override
	public void registerReward(IChanceCubeReward reward)
	{
		nameToReward.put(reward.getName(), reward);
		redoSort(reward);
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
	public void triggerRandomReward(World world, int x, int y, int z, EntityPlayer player, int chance, Bound<Integer> chanceBounds)
	{
		if (player != null)
		{
			for (int i = 0; i < player.inventory.mainInventory.length; i++)
			{
				ItemStack stack = player.inventory.mainInventory[i];
				if (stack != null && stack.getItem() instanceof ItemChancePendant)
				{
					ItemChancePendant pendant = (ItemChancePendant) stack.getItem();
					pendant.damage(stack);
					chance += pendant.getChanceIncrease();
					if(chance>100)
						chance=100;
				}
			}
		}

		int lowerIndex = 0;
		int upperIndex = sortedRewards.size() - 1;
		int lowerRange = CHANCE_BOUND.clamp(chanceBounds.min + chance);
		int upperRange = CHANCE_BOUND.clamp(chanceBounds.max + chance);

		while (sortedRewards.get(lowerIndex).getChanceValue() < lowerRange)
		{
			lowerIndex++;
			if(lowerIndex >= sortedRewards.size())
			{
				lowerIndex--;
				break;
			}
		}
		while (sortedRewards.get(upperIndex).getChanceValue() > upperRange)
		{
			upperIndex--;
			if(upperIndex < 0)
			{
				upperIndex++;
				break;
			}
		}

		int pick = world.rand.nextInt(upperIndex - lowerIndex + 1) + lowerIndex;
		CCubesCore.logger.log(Level.INFO, "Triggered the reward with the name of: " +  sortedRewards.get(pick).getName());
		sortedRewards.get(pick).trigger(world, x, y, z, player);
	}

	private void redoSort(@Nullable IChanceCubeReward newReward)
	{
		if (newReward != null)
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
