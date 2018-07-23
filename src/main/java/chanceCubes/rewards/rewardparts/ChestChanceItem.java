package chanceCubes.rewards.rewardparts;

import java.util.Random;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.util.RewardsUtil;
import net.minecraft.item.ItemStack;

public class ChestChanceItem
{
	public static String[] elements = new String[] { "item:I", "chance:I", "meta:I", "amountMin:I", "amountMax:I" };

	private static Random rand = new Random();
	private String mod;
	private String item;
	private int meta;
	private int amountMin;
	private int amountMax;
	private int chance;

	public ChestChanceItem(String item, int meta, int chance, int amountMin, int amountMax)
	{
		this.mod = item.substring(0, item.indexOf(":"));
		this.item = item.substring(item.indexOf(":") + 1);
		this.meta = meta;
		this.chance = chance;
		this.amountMin = amountMin;
		this.amountMax = amountMax;
	}

	private ItemStack getItemStack(int amount, int meta)
	{
		ItemStack stack = RewardsUtil.getItemStack(mod, item, amount, meta);
		if(stack.isEmpty())
		{
			stack = new ItemStack(RewardsUtil.getBlock(mod, item), amount, meta);
			if(stack.isEmpty())
				stack = new ItemStack(CCubesBlocks.CHANCE_CUBE, 1, 0);
		}

		return stack;
	}

	public ItemStack getRandomItemStack()
	{
		return this.getItemStack(rand.nextInt(amountMax - amountMin) + amountMin, meta);
	}

	public int getChance()
	{
		return this.chance;
	}
}
