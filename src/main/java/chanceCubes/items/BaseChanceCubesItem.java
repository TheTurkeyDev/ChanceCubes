package chanceCubes.items;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import chanceCubes.CCubesCore;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BaseChanceCubesItem extends Item
{
	private String itemName = "Chance_Cube_Unnamed";
	private List<String> lore = Lists.newArrayList();

	public BaseChanceCubesItem(Item.Builder builder, String name)
	{
		super(builder.group(CCubesCore.modTab));
		itemName = name;
		this.setRegistryName(CCubesCore.MODID, this.itemName);
	}

	public String getItemName()
	{
		return this.itemName;
	}

	public void addLore(String info)
	{
		lore.add(info);
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn)
	{
		for(String s : lore)
		{
			list.add(new TextComponentString(s));
		}
	}
}