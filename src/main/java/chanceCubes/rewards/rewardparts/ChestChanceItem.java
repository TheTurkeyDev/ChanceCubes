package chanceCubes.rewards.rewardparts;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ChestChanceItem
{
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
	
	public Item getItem()
	{
		Block b = GameRegistry.findBlock(mod,item);
		if(b != null)
			return Item.getItemFromBlock(b);
		else
			return GameRegistry.findItem(mod,item);
	}
	
	public ItemStack getRandomItemStack()
	{
		int amount = rand.nextInt(amountMax - amountMin) + amountMin;
		return new ItemStack(this.getItem(), amount, meta);
	}
	
	public int getChance()
	{
		return this.chance;
	}
}
