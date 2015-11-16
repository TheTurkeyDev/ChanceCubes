package chanceCubes.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;

public class ItemChancePendant extends Item
{
	public String itemNameID = "chance_Pendant_Tier";

	private int chanceIncrease;

	public ItemChancePendant(int tier, int chancebonus)
	{
		itemNameID += tier;
		this.setUnlocalizedName(itemNameID);
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
		this.setMaxDamage(CCubesSettings.pendantUses);
		super.showDurabilityBar(new ItemStack(this));
		chanceIncrease = chancebonus;
	}

	public int getChanceIncrease()
	{
		return chanceIncrease;
	}

	public void damage(ItemStack stack)
	{
		stack.setItemDamage(stack.getItemDamage() + 1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		list.add("Increases the chance of Chance Cubes by:");
		list.add("      +" + chanceIncrease + " when the block is broken");
		list.add("Only needs to be in the players inventory to work");
	}

	public boolean getIsRepairable(ItemStack stack, ItemStack repairStack)
	{
		if(stack.getItem() instanceof ItemChancePendant && repairStack.getItem().equals(Blocks.lapis_block))
			return true;
		return false;
	}
}
