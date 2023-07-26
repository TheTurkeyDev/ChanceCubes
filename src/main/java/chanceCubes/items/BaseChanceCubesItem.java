package chanceCubes.items;

import chanceCubes.mcwrapper.ComponentWrapper;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag)
	{
		for(String s : lore)
			list.add(ComponentWrapper.string(s));
	}
}