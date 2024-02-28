package chanceCubes.mcwrapper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemWrapper
{
	public static String getItemIdStr(ItemStack stack)
	{
		return getItemIdStr(stack.getItem());
	}

	public static String getItemIdStr(Item item)
	{
		return getItemId(item).toString();
	}

	public static ResourceLocation getItemId(ItemStack stack)
	{
		return getItemId(stack.getItem());
	}

	public static ResourceLocation getItemId(Item item)
	{
		return BuiltInRegistries.ITEM.getKey(item);
	}
}
