package chanceCubes.containers;

import chanceCubes.CCubesCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCubesMenus
{
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, CCubesCore.MODID);

	public static DeferredHolder<MenuType<?>, MenuType<CreativePendantContainer>> CREATIVE_PENDANT_CONTAINER = MENUS.register(
			"creative_pendant_container", () -> IMenuTypeExtension.create((windowId, inv, data) -> new CreativePendantContainer(windowId, inv)));

}
