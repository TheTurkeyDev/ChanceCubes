package chanceCubes.items;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import chanceCubes.CCubesCore;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BaseChanceCubesItem extends Item
{
	private List<String> lore = Lists.newArrayList();

	public BaseChanceCubesItem(Properties builder, String name)
	{
		super(builder.group(CCubesCore.modTab));
		this.setRegistryName(CCubesCore.MODID, name);
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
			list.add(new StringTextComponent(s));
		}
	}
}