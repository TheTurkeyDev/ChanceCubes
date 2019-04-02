package chanceCubes.rewards.rewardparts;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.util.RewardsUtil;
import net.minecraft.item.ItemStack;

public class ChestChanceItem extends BasePart
{
	private String mod;
	private String item;
	private IntVar amount;
	private IntVar chance;

	public ChestChanceItem(String item, int meta, int chance, int amount)
	{
		this(item, new IntVar(meta), new IntVar(chance), new IntVar(amount));
	}

	public ChestChanceItem(String item, IntVar meta, IntVar chance, IntVar amount)
	{
		this.mod = item.substring(0, item.indexOf(":"));
		this.item = item.substring(item.indexOf(":") + 1);
		this.chance = chance;
		this.amount = amount;
	}

	private ItemStack getItemStack(int amount)
	{
		ItemStack stack = RewardsUtil.getItemStack(mod, item, amount);
		if(stack.isEmpty())
		{
			stack = new ItemStack(RewardsUtil.getBlock(mod, item), amount);
			if(stack.isEmpty())
				stack = new ItemStack(CCubesBlocks.CHANCE_CUBE, 1);
		}

		return stack;
	}

	public ItemStack getRandomItemStack()
	{
		return this.getItemStack(amount.getIntValue());
	}

	public int getChance()
	{
		return this.chance.getIntValue();
	}
}
