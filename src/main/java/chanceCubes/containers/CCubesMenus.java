package chanceCubes.containers;

import chanceCubes.CCubesCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CCubesMenus {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, CCubesCore.MODID);

	public static final Supplier<MenuType<CreativePendantContainer>> CREATIVE_PENDANT_CONTAINER = MENU_TYPES.register("force_furnace", () ->
			IMenuTypeExtension.create((windowId, inv, data) -> new CreativePendantContainer(windowId, inv)));
}
