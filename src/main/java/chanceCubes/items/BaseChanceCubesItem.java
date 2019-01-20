package chanceCubes.items;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import chanceCubes.CCubesCore;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseChanceCubesItem extends Item
{
	private String itemName = "Chance_Cube_Unnamed";
	private List<String> lore = Lists.newArrayList();

	public BaseChanceCubesItem(String name)
	{
		itemName = name;
		this.setTranslationKey(name);
		this.setRegistryName(CCubesCore.MODID, this.itemName);
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

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn)
	{
		list.addAll(lore);
	}
}