package chanceCubes.items;

import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemChancePendant extends Item
{
	private int chanceIncrease;

	public ItemChancePendant(int tier, int chancebonus)
	{
		this.setUnlocalizedName("chancePendantTier" + tier);
		this.setTextureName(CCubesCore.MODID + ":PendantT" + tier);
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
		list.add("Increases the chance of Chance Cubes by +" + chanceIncrease + " when the block is broken");
	}

	public boolean getIsRepairable(ItemStack stack, ItemStack repairStack)
	{
		if(stack.getItem() instanceof ItemChancePendant && repairStack.getItem().equals(Blocks.lapis_block))
			return true;
		return false;
	}
}
