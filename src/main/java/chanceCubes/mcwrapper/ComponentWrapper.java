package chanceCubes.mcwrapper;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

public class ComponentWrapper
{
	public static MutableComponent string(String str)
	{
		return Component.literal(str);
	}

	public static MutableComponent translatable(String str, Object... objs)
	{
		return MutableComponent.create(new TranslatableContents(str, null, objs));
	}
}
