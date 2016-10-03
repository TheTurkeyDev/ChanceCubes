package chanceCubes.items;

import java.util.List;

import com.google.common.collect.Lists;

import chanceCubes.CCubesCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaseChanceCubesItem extends Item
{
	private String itemName = "Chance_Cube_Unnamed";
	private List<String> lore = Lists.newArrayList();
	
	private int number = 0;
	
	public BaseChanceCubesItem(String name)
	{
		itemName = name;
		this.setUnlocalizedName(name);
		this.setTextureName(CCubesCore.MODID + ":" + name);
		this.setCreativeTab(CCubesCore.modTab);
	}
	
	public String getItemName()
	{
		return this.itemName;
	}
	
	public void addLore(String info)
	{
		lore.add(info);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) 
	{
		if(number = 0}{
			list.add("Mod created by: Turkey2348");
			number = 1;
		} else if(number = 1){
			number = 0;
		}
		list.addAll(lore);
	}
}
