package chanceCubes.items;

import chanceCubes.CCubesCore;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BaseChanceCubesItem extends Item
{
	private final List<String> lore = Lists.newArrayList();

	public BaseChanceCubesItem(Properties builder, String name)
	{
		super(builder.tab(CCubesCore.modTab));
		this.setRegistryName(CCubesCore.MODID, name);
	}

	public void addLore(String info)
	{
		lore.add(info);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag)
	{
		for(String s : lore)
			list.add(new TextComponent(s));
	}
}