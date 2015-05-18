package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.config.ConfigLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemChancePendant extends Item {

	
	public ItemChancePendant()
	{
		super();
		this.setUnlocalizedName("chancePendant");
		this.hasSubtypes = true;
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setInteger("damage", ConfigLoader.pendantUses);
		stack.stackTagCompound.setInteger("luck", 0);
	}
	
	public void damage(ItemStack stack, int damageAmount)
	{
		stack.stackTagCompound.setInteger("damage", stack.stackTagCompound.getInteger("damage") - damageAmount > 0 ? stack.stackTagCompound.getInteger("damage") - damageAmount : 0);
	}
	
	public void damage(ItemStack stack)
	{
		damage(stack, 1);
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		if (stack.stackTagCompound != null)
			return getNumberUsesRemaining(stack) / (double) ConfigLoader.pendantUses;
		else
			return 0;
	}
	
	public int getNumberUsesRemaining(ItemStack stack)
	{
		return stack.stackTagCompound.getInteger("damage");
	}
	
	public void removeLuck(ItemStack stack, int amount)
	{
		stack.stackTagCompound.setInteger("luck", stack.stackTagCompound.getInteger("luck") - amount < 0 ? 0 : stack.stackTagCompound.getInteger("luck") - amount);
	}
	
	public void removeAllLuck(ItemStack stack)
	{
		stack.stackTagCompound.setInteger("luck", 0);
	}
	
	public int getLuck(ItemStack stack)
	{
		return stack.stackTagCompound.getInteger("luck");
	}
}
