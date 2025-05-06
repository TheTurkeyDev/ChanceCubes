package chanceCubes.items;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BaseChanceCubesItem extends Item
{
	private final List<String> lore = Lists.newArrayList();

	public BaseChanceCubesItem(Properties builder)
	{
		super(builder);
	}

	public void addLore(String info)
	{
		lore.add(info);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) 
	{
		super.appendHoverText(stack, context, list, flag);
	}
}